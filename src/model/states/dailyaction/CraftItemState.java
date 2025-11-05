package model.states.dailyaction;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.*;
import model.items.books.BookItem;
import model.items.clothing.JustClothes;
import model.items.designs.CraftingDesign;
import model.items.weapons.UnarmedCombatWeapon;
import model.states.DailyActionState;
import model.states.GameState;
import util.MyRandom;
import util.MyTriplet;
import view.subviews.*;

import java.util.*;

public class CraftItemState extends GameState {

    private static final Skill SALVAGE_SKILL = Skill.Labor;

    public CraftItemState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        WorkbenchSubView subView = new WorkbenchSubView();
        CollapsingTransition.transition(model, subView);
        println("What would you like to do at the workbench?");
        do {
            int selected = multipleOptionArrowMenu(model, 24, 26,
                    List.of("Craft Item", "Upgrade Item", "Salvage Item", "Cancel"));
            if (selected == 1) {
                upgradeItem(model);
            } else if (selected == 2) {
                salvageItem(model);
            } else if (selected == 0) {
                craftItem(model);
            } else {
                return new DailyActionState(model);
            }
        } while (true);
    }

    private void craftItem(Model model) {
        model.getTutorial().crafting(model);
        List<Item> allItems = getAllItems(model);
        allItems.removeIf((Item it) -> it instanceof BookItem || it instanceof Scroll);
        allItems.addAll(model.getParty().getLearnedCraftingDesigns());
        allItems.sort((o1, o2) -> { // This should put crafting designs at the top of the list, important for later
            int i1 = o1 instanceof CraftingDesign ? 0 : 1;
            int i2 = o2 instanceof CraftingDesign ? 0 : 1;
            return i1 - i2;
        });
        if (allItems.isEmpty()) {
            println("You cannot craft since you do not have any suitable items or crafting designs.");
        }

        Set<String> optionNames = new HashSet<>();
        for (Item it : allItems) {
            if (it instanceof CraftingDesign) {
                int cost = calculateCost((CraftingDesign) it);
                if (model.getParty().getInventory().getMaterials() >= cost) {
                    optionNames.add(((CraftingDesign)it).getCraftableName() + " (d/" + cost + ")");
                }
            } else {
                int cost = calculateCost(it);
                if (model.getParty().getInventory().getMaterials() >= cost) {
                    optionNames.add(it.getName() + " (" + cost + ")");
                }
            }
        }
        if (optionNames.isEmpty()) {
            println("You do not have enough materials to craft anything.");
            return;
        }
        MyTriplet<Item, Integer, Boolean> triplet = getSelectedItem(model, optionNames, allItems);
        if (triplet == null) {
            return;
        }
        SubView previous = model.getSubView();
        model.setSubView(new CraftItemSubView(previous, triplet.first, triplet.second, triplet.third));
        print("Are you sure you want to spend " + triplet.second +
                " materials to attempt to craft " + triplet.first.getName() + "? (Y/N) ");
        if (yesNoInput() && makeItemFromMaterials(model, this, triplet.first, triplet.second, "craft", triplet.third)) {
            GameStatistics.incrementItemsCrafted(1);
            model.getParty().addToInventory(triplet.first.copy());
        }
        model.setSubView(previous);
    }

    public static boolean makeItemFromMaterials(Model model, GameState state, Item selectedItem, Integer materialCost,
                                                String actionName, boolean fromCraftingDesign) {
        GameCharacter crafter = null;
        if (model.getParty().size() > 1) {
            state.println("Which party member should attempt to " + actionName + " the " + selectedItem.getName() + "?");
            crafter = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));
        } else {
            crafter = model.getParty().getPartyMembers().get(0);
            state.print(crafter.getName() + " is preparing to " + actionName + " an item. Press enter to continue.");
            state.waitForReturn();
        }
        int difficulty = calculateDifficulty(selectedItem);
        Skill[] steps;
        if (fromCraftingDesign) {
             steps = new Skill[]{Skill.Logic, Skill.Labor};
        } else {
             steps = new Skill[]{Skill.Perception, Skill.Logic, Skill.Labor};
        }
        boolean failure = false;
        for (Skill step : steps) {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, crafter, step,
                    difficulty + MyRandom.randInt(-1, 1), 5, 0);
            if (!result.isSuccessful()) {
                failure = true;
                break;
            }
        }
        model.getParty().getInventory().addToMaterials(-materialCost);
        if (failure) {
            state.println(crafter.getName() + " has failed to " + actionName + " " + selectedItem.getName() +
                    " and wasted " + materialCost + " materials while doing so.");
            return false;
        }
        state.println(crafter.getName() + " used " + materialCost + " materials to " +
                actionName + " " + selectedItem.getName() + "!");
        return true;
    }

    public static int calculateDifficulty(Item selectedItem) {
        return Math.min(4 + selectedItem.getCost() / 10, 16);
    }

    private MyTriplet<Item, Integer, Boolean> getSelectedItem(Model model, Set<String> optionNames, List<Item> allItems) {
        List<String> options = new ArrayList<>(optionNames);
        options.add("Cancel");
        println("What item would you like to craft?");
        int chosen = showMenu(model, options);
        String selected = options.get(chosen);
        for (Item it : allItems) {
            if (it instanceof CraftingDesign) {
                if (selected.contains(((CraftingDesign) it).getCraftableName())) {
                    Item it2 = ((CraftingDesign) it).getCraftable();
                    return new MyTriplet<>(it2, calculateCost((CraftingDesign) it), true);
                }
            } else {
                if (selected.contains(it.getName())) {
                    return new MyTriplet<>(it, calculateCost(it), false);
                }
            }
        }
        return null; // Cancel chosen
    }

    private Integer calculateCost(Item it) {
        return it.getCost() / 2;
    }

    public static int calculateCost(CraftingDesign design) {
        return design.getCraftable().getCost() / 3;
    }

    private List<Item> getAllItems(Model model) {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(model.getParty().getInventory().getAllItems());
        allItems.removeIf((Item it) -> !it.isCraftable());
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!(gc.getEquipment().getWeapon() instanceof UnarmedCombatWeapon)) {
                allItems.add(gc.getEquipment().getWeapon());
            }
            if (!(gc.getEquipment().getClothing() instanceof JustClothes)) {
                allItems.add(gc.getEquipment().getClothing());
            }
            if (gc.getEquipment().getAccessory() != null) {
                allItems.add(gc.getEquipment().getAccessory());
            }
        }
        if (model.getParty().getInventory().getLockpicks() > 0) {
            allItems.add(new Lockpick());
        }
        return allItems;
    }


    private void salvageItem(Model model) {
        model.getTutorial().salvaging(model);
        List<Item> salvageableItems = new ArrayList<>(model.getParty().getInventory().getWeapons());
        salvageableItems.addAll(model.getParty().getInventory().getClothing());
        salvageableItems.addAll(model.getParty().getInventory().getAccessories());
        if (salvageableItems.isEmpty()) {
            println("You have no items in your inventory which are salvageable.");
            return;
        }
        println("Which item would you like to salvage?");
        List<String> names = new ArrayList<>();
        for (Item it : salvageableItems) {
            names.add(it.getName());
        }
        names.add("Cancel");
        int selected = showMenu(model, names);

        if (selected >= salvageableItems.size()) {
            return;
        }
        Item itemToSalvage = salvageableItems.get(selected);

        SubView previous = model.getSubView();
        SalvageItemSubView salvageView = new SalvageItemSubView(previous, itemToSalvage);
        model.setSubView(salvageView);
        print("Which party member should attempt to salvage the " + itemToSalvage.getName() + "? ");
        GameCharacter salvager = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        setMinMaxInView(salvageView, salvager, itemToSalvage);
        print("Are you sure you want " + salvager.getFirstName() + " to attempt to salvage materials from " +
                itemToSalvage.getName() + "? (Y/N) ");
        if (yesNoInput()) {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, salvager, SALVAGE_SKILL, 5, 5, 0);
            int materialsGained = materialsFromSalvage(itemToSalvage.getCost(), result.getModifiedRoll());
            println("The " + itemToSalvage.getName() + " was destroyed.");
            if (result.isSuccessful() && materialsGained > 0) {
                println(salvager.getFirstName() + " managed to salvage " + materialsGained +
                        " material" + (materialsGained == 1 ? "" : "s") + " from the " + itemToSalvage.getName() + ".");
                model.getParty().partyMemberSay(model, salvager, List.of("Why throw something away just because it's old?",
                        "We can probably use these materials for something.",
                        "These are good quality materials, let's save them for later.",
                        "Nice!"));
                model.getParty().getInventory().addToMaterials(materialsGained);
            } else {
                println(salvager.getFirstName() + " failed to salvage any materials.");
                model.getParty().partyMemberSay(model, salvager, List.of("Darn it!#", "Doh!#", "Phooey!#",
                        "That's too bad.", "What a waste...", "I'm sorry. This is ruined now.",
                        "Hmm. I really thought I could do it.", "Trash...", "What, it broke?"));
            }
            model.getParty().removeFromInventory(itemToSalvage);
        }
        model.setSubView(previous);
    }

    private void setMinMaxInView(SalvageItemSubView salvageView, GameCharacter salvager, Item itemToSalvage) {
        int min = 0;
        int salvagerRank = salvager.getRankForSkill(SALVAGE_SKILL);
        for (int roll = 1; roll <= 10 && min == 0; ++roll) {
            min = materialsFromSalvage(itemToSalvage.getCost(), salvagerRank + roll);
        }
        int max = materialsFromSalvage(itemToSalvage.getCost(), salvagerRank + 10);
        salvageView.setMinAndMax(min, max);
    }

    private int materialsFromSalvage(int cost, int modifiedRoll) {
        return Math.max(0, (cost + modifiedRoll - 5) / 5);
    }

    private int showMenu(Model model, List<String> names) {
        final int[] selected = new int[1];
        model.setSubView(new ArrowMenuSubView(model.getSubView(), names, 24, 40, ArrowMenuSubView.SOUTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        waitForReturnSilently();
        return selected[0];
    }

    private void upgradeItem(Model model) {
        model.getTutorial().upgrading(model);
        List<Item> upgradeableItems = new ArrayList<>(model.getParty().getInventory().getWeapons());
        upgradeableItems.addAll(model.getParty().getInventory().getClothing());
        upgradeableItems.addAll(model.getParty().getInventory().getAccessories());
        upgradeableItems.removeIf((Item it ) -> !it.supportsHigherTier());
        if (upgradeableItems.isEmpty()) {
            println("You have no items in your inventory which can be upgraded.");
            return;
        }
        println("Which item would you like to upgrade?");
        List<String> names = new ArrayList<>();
        for (Item it : upgradeableItems) {
            if (it.getCost() <= model.getParty().getInventory().getMaterials()) {
                names.add(it.getName() + " (" + it.getCost() + ")");
            }
        }
        if (names.isEmpty()) {
            println("You do not have enough materials to upgrade any items in your inventory.");
            return;
        }
        names.add("Cancel");
        int selected = showMenu(model, names);
        Item selectedItem = null;
        for (Item it : upgradeableItems) {
            if (names.get(selected).contains(it.getName())) {
                selectedItem = it;
            }
        }
        if (selectedItem == null) { // Cancel chosen
            return;
        }
        Item potentialItem;
        if (selectedItem instanceof HigherTierItem) {
            HigherTierItem higherItem = (HigherTierItem)selectedItem;
            potentialItem = higherItem.getInnerItem().makeHigherTierCopy(higherItem.getTier()+1);
        } else {
            potentialItem = selectedItem.makeHigherTierCopy(1);
        }
        SubView oldSubview = model.getSubView();
        UpgradeItemOverview overview = new UpgradeItemOverview(model, selectedItem, potentialItem);
        model.setSubView(overview);
        print("Are you sure you want to attempt to upgrade " + selectedItem.getName() + " to " +
                potentialItem.getName() + " (cost of " + selectedItem.getCost() + " materials)" +"? (Y/N) ");
        if (yesNoInput()) {
            if (makeItemFromMaterials(model, this, selectedItem, selectedItem.getCost(), "upgrade", false)) {
                GameStatistics.incrementItemsUpgraded(1);
                model.getParty().removeFromInventory(selectedItem);
                potentialItem.addYourself(model.getParty().getInventory());
            }
        }
        model.setSubView(oldSubview);
    }

    public interface UpgradeCallback {
        void doAction();
    }
}

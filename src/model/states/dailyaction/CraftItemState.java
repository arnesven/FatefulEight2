package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.PotionRecipe;
import model.items.clothing.JustClothes;
import model.items.designs.CraftingDesign;
import model.items.potions.Potion;
import model.items.spells.Spell;
import model.items.weapons.UnarmedCombatWeapon;
import model.states.DailyActionState;
import model.states.GameState;
import util.MyPair;
import util.MyRandom;
import view.subviews.ArrowMenuSubView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CraftItemState extends GameState {

    public CraftItemState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("What would you like to do at the workbench?");
        int selected = multipleOptionArrowMenu(model, 30, 20, List.of("Craft Item", "Salvage Item", "Cancel"));
        if (selected == 1) {
            salvageItem(model);
        }
        if (selected > 0) {
            return new DailyActionState(model);
        }
        model.getTutorial().crafting(model);
        List<Item> allItems = getAllItems(model);
        if (allItems.isEmpty()) {
            println("You cannot craft since you do not have any items.");
            return new DailyActionState(model);
        }

        Set<String> optionNames = new HashSet<>();
        for (Item it : allItems) {
            if (it instanceof CraftingDesign) {
                int cost = ((CraftingDesign) it).getCraftable().getCost() / 3;
                if (model.getParty().getInventory().getMaterials() > cost) {
                    optionNames.add(((CraftingDesign)it).getCraftableName() + " (" + cost + ")");
                }
            } else {
                int cost = it.getCost() / 2;
                if (model.getParty().getInventory().getMaterials() > cost) {
                    optionNames.add(it.getName() + " (" + cost + ")");
                }
            }
        }
        if (optionNames.isEmpty()) {
            println("You do not have enough materials to craft anything.");
            return new DailyActionState(model);
        }
         MyPair<Item, Integer> pair = getSelectedItem(model, optionNames, allItems);
        if (pair == null) {
            return new DailyActionState(model);
        }
        Item selectedItem = pair.first;

        GameCharacter crafter = null;
        if (model.getParty().size() > 1) {
            println("Which party member should attempt to craft the " + selectedItem.getName() + "?");
            crafter = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        } else {
            crafter = model.getParty().getPartyMembers().get(0);
            print(crafter.getName() + " is preparing to craft an item. Press enter to continue.");
            waitForReturn();
        }
        int difficulty = 4 + selectedItem.getCost() / 10;
        Skill[] steps = new Skill[]{Skill.Perception, Skill.Logic, Skill.Labor};
        boolean failure = false;
        for (Skill step : steps) {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, crafter, step,
                    difficulty + MyRandom.randInt(-1, 1), 5, 0);
            if (!result.isSuccessful()) {
                failure = true;
                break;
            }
        }
        int materialCost = pair.second;
        model.getParty().getInventory().addToMaterials(-materialCost);
        if (failure) {
            println(crafter.getName() + " has failed to craft " + selectedItem.getName() + " and wasted " + materialCost + " materials while doing so.");
        } else {
            println(crafter.getName() + " used " + materialCost + " materials to craft " + selectedItem.getName() + "!");
            model.getParty().getInventory().addItem(selectedItem.copy());
        }

        return new DailyActionState(model);
    }

    private MyPair<Item, Integer> getSelectedItem(Model model, Set<String> optionNames, List<Item> allItems) {
        List<String> options = new ArrayList<>(optionNames);
        options.add("Cancel");
        println("What item would you like to craft?");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 38, ArrowMenuSubView.SOUTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        waitForReturnSilently();
        for (Item it : allItems) {
            if (it instanceof CraftingDesign) {
                if (selected[0].contains(((CraftingDesign) it).getCraftableName())) {
                    Item it2 = ((CraftingDesign) it).getCraftable();
                    return new MyPair<>(it2, it2.getCost()/3);
                }
            } else {
                if (selected[0].contains(it.getName())) {
                    return new MyPair<>(it, it.getCost()/2);
                }
            }
        }
        return null; // Cancel chosen
    }

    private List<Item> getAllItems(Model model) {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(model.getParty().getInventory().getAllItems());
        allItems.removeIf((Item it ) -> it instanceof Spell || it instanceof Potion || it instanceof PotionRecipe);
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
        int selected = multipleOptionArrowMenu(model, 24, 38, names);
        Item itemToSalvage = salvageableItems.get(selected);
        print("Which party member should attempt to salvage the " + itemToSalvage.getName() + "? ");
        GameCharacter salvager = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, salvager, Skill.Labor, 5, 5, 0);
        int materialsGained = Math.max(0, (itemToSalvage.getCost() + result.getModifiedRoll() - 5) / 5);
        println("The " + itemToSalvage.getName() + " was destroyed.");
        if (result.isSuccessful() && materialsGained > 0) {
            println(salvager.getFirstName() + " managed to salvage " + materialsGained +
                    " material " + (materialsGained==1?"":"s") + " from the " + itemToSalvage.getName() + ".");
            model.getParty().partyMemberSay(model, salvager, List.of("Why throw something away just because it's old?",
                    "We can probably use these materials for something.",
                    "These are good quality materials, let's save them for later.",
                    "Nice!"));
        } else {
            println(salvager.getFirstName() + " failed to salvage any materials.");
            model.getParty().partyMemberSay(model, salvager, List.of("Darn it!#", "Doh!#", "Phooey!#",
                    "That's too bad.", "What a waste...", "I'm sorry. This is ruined now.",
                    "Hmm. I really thought I could do it.", "Trash..."));
        }
        model.getParty().getInventory().remove(itemToSalvage);
    }
}

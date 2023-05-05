package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.clothing.JustClothes;
import model.items.potions.Potion;
import model.items.spells.Spell;
import model.items.weapons.UnarmedCombatWeapon;
import model.states.GameState;
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
        model.getTutorial().crafting(model);
        List<Item> allItems = getAllItems(model);
        if (allItems.isEmpty()) {
            println("You cannot craft since you do not have any items!");
            return null;
        }

        Set<String> optionNames = new HashSet<>();
        for (Item it : allItems) {
            int cost = it.getCost() / 2;
            if (model.getParty().getInventory().getMaterials() > cost) {
                optionNames.add(it.getName() + " (" + cost + ")");
            }
        }
        if (optionNames.isEmpty()) {
            println("You do not have enough materials to craft anything.");
            return null;
        }
        Item selectedItem = getSelectedItem(model, optionNames, allItems);

        if (selectedItem == null){
            return null;
        }

        GameCharacter crafter = null;
        if (model.getParty().size() > 1) {
            println("Which party member should attempt to craft the " + selectedItem.getName() + "?");
            crafter = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        } else {
            crafter = model.getAllCharacters().get(0);
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
        int materialCost = selectedItem.getCost() / 2;
        model.getParty().getInventory().addToMaterials(-materialCost);
        if (failure) {
            println(crafter.getName() + " has failed to craft " + selectedItem.getName() + " and wasted " + materialCost + " materials while doing so.");
        } else {
            println(crafter.getName() + " used " + materialCost + " materials to craft " + selectedItem.getName() + "!");
            model.getParty().getInventory().addItem(selectedItem.copy());
        }

        return null;
    }

    private Item getSelectedItem(Model model, Set<String> optionNames, List<Item> allItems) {
        List<String> options = new ArrayList<>(optionNames);
        options.add("Return");
        println("What item would you like to craft?");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 28, 24 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        waitForReturnSilently();
        for (Item it : allItems) {
            if (selected[0].contains(it.getName())) {
                return it;
            }
        }
        return null; // Cancel chosen
    }

    private List<Item> getAllItems(Model model) {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(model.getParty().getInventory().getAllItems());
        allItems.removeIf((Item it ) -> it instanceof Spell || it instanceof Potion);
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
}

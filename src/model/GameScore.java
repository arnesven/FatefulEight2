package model;

import model.characters.GameCharacter;
import model.items.Item;
import model.items.spells.Spell;
import model.map.locations.FortressAtUtmostEdgeLocation;
import util.MyLists;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameScore extends HashMap<String, Integer> {

    public static GameScore calculate(Model model) {
        GameScore gs = new GameScore();
        gs.put("Reputation", model.getParty().getReputation() * 1000);
        gs.put("Days Remaining", (101 - model.getDay()) * 10);
        gs.put("Party Members", model.getParty().partyStrength() * 2);
        int totalGold = totalEquipmentValue(model);
        gs.put("Wealth", totalGold);
        gs.put("Spells Collected", spellsCollected(model) * 25);
        gs.put("Main Story Completed", model.getMainStory().isCompleted(model)?2500:0);
        gs.put("FatUE Cleared", isFatueCleared(model)?2500:0);
        return gs;
    }

    private static boolean isFatueCleared(Model model) {
        return model.getDungeon(FortressAtUtmostEdgeLocation.NAME, false).isCompleted();
    }

    private static Integer spellsCollected(Model model) {
        Set<String> spells = new HashSet<>();
        for (Spell sp : model.getParty().getInventory().getSpells()) {
            spells.add(sp.getName());
        }
        return spells.size();
    }

    private static int totalEquipmentValue(Model model) {
        int sum = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            sum += gc.getEquipment().getWeapon().getCost();
            sum += gc.getEquipment().getClothing().getCost();
            if (gc.getEquipment().getAccessory() != null) {
                sum += gc.getEquipment().getAccessory().getCost();
            }
        }
        sum += MyLists.intAccumulate(model.getParty().getInventory().getAllItems(), Item::getCost);
        sum += model.getParty().getGold();
        return sum;
    }

    public int getTotal() {
        int sum = 0;
        for (Integer i : values()) {
            sum += i;
        }
        return sum;
    }
}

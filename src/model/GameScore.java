package model;

import model.characters.GameCharacter;
import model.items.Item;

import java.util.HashMap;

public class GameScore extends HashMap<String, Integer> {

    public static GameScore calculate(Model model) {
        GameScore gs = new GameScore();
        gs.put("Reputation", model.getParty().getReputation() * 1000);
        gs.put("Party Members", model.getParty().partyStrength());
        int totalGold = totalEquipmentValue(model);
        gs.put("Wealth", totalGold);
        return gs;
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

        for (Item it : model.getParty().getInventory().getAllItems()) {
            sum += it.getCost();
        }

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

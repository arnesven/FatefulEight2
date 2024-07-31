package model.headquarters;

import model.Model;
import model.characters.GameCharacter;
import util.MyLists;

import java.util.ArrayList;

public class ShoppingAssignees extends ArrayList<GameCharacter> {

    private int foodLimit;

    public ShoppingAssignees(int foodLimit) {
        this.foodLimit = foodLimit;
    }

    public void performAssignments(Model model, Headquarters hq, StringBuilder logEntry) {
        if (!isEmpty()) {
            if (hq.getFood() < hq.getFoodLimit()) {
                if (hq.getGold() == 0) {
                    logEntry.append(MyLists.commaAndJoin(this, GameCharacter::getName));
                    logEntry.append(" could not go shopping, no gold in headquarters.\n");
                } else {
                    int goldToSpend = Math.min(hq.getGold(), (int) Math.ceil(hq.getMaxCharacters() / 5.0));
                    hq.addToGold(-goldToSpend);
                    hq.addToFood(goldToSpend * 5);
                    logEntry.append(MyLists.commaAndJoin(this, GameCharacter::getName));
                    logEntry.append(" went shopping, bought ").append(goldToSpend * 5);
                    logEntry.append(" rations for ").append(goldToSpend).append(" gold.\n");
                }
            }
        }
    }

    public int getFoodLimit() {
        return foodLimit;
    }

    public void setFoodLimit(int amount) {
        foodLimit = amount;
    }
}

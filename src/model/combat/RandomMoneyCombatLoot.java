package model.combat;

import model.Party;
import util.MyRandom;

public class RandomMoneyCombatLoot extends CombatLoot {
    private final int amount;

    public RandomMoneyCombatLoot(int amount) {
        this.amount = MyRandom.randInt(amount) + amount/2;
    }

    @Override
    public String getText() { return ""; }

    @Override
    protected void specificGiveYourself(Party party) { }

    @Override
    public int getGold() {
        return amount;
    }
}

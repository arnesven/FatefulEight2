package model.combat.loot;

import model.Party;

public class RationsCombatLoot extends CombatLoot {
    private final int amount;

    public RationsCombatLoot(int amount) {
        this.amount = amount;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    protected void specificGiveYourself(Party party) { }

    @Override
    public int getRations() {
        return amount;
    }
}

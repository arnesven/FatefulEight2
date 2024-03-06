package model.combat.loot;

import model.Party;

public class NoCombatLoot extends CombatLoot {
    @Override
    public String getText() {
        return "";
    }

    @Override
    protected void specificGiveYourself(Party party) { }
}

package model.combat;

import model.Party;

public class NoCombatLoot extends CombatLoot {
    @Override
    public String getText() {
        return "*Nothing*";
    }

    @Override
    protected void specificGiveYourself(Party party) { }
}

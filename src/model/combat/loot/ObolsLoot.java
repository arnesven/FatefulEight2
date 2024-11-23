package model.combat.loot;

import model.Party;

public class ObolsLoot extends CombatLoot {
    private final int obols;

    public ObolsLoot(int obols) {
        this.obols = obols;
    }

    @Override
    public String getText() {
        return obols + " Obols";
    }

    @Override
    public int getObols() {
        return obols;
    }

    @Override
    protected void specificGiveYourself(Party party) { }
}

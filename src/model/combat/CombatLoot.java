package model.combat;

import model.Party;

public abstract class CombatLoot {
    public abstract String getText();
    public final void giveYourself(Party party) {
        party.addToGold(getGold());
        party.addToFood(getRations());
        party.getInventory().addToMaterials(getMaterials());
        party.getInventory().addToIngredients(getIngredients());
        specificGiveYourself(party);
    }

    protected abstract void specificGiveYourself(Party party);
    public int getGold() { return 0; }
    public int getRations() { return 0; }
    public int getMaterials() { return 0; }
    public int getIngredients() { return 0; }
    public int getObols() { return 0; }
}

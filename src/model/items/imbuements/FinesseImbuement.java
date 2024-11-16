package model.items.imbuements;

public class FinesseImbuement extends WeaponImbuement {
    @Override
    public String getText() {
        return "+1 to Attacks";
    }

    @Override
    public int getAttackBonus() {
        return 1;
    }
}

package model.items.weapons;

public abstract class CrossbowWeapon extends BowWeapon {

    public CrossbowWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable);
    }

    @Override
    public int getReloadSpeed() {
        return 8;
    }
}

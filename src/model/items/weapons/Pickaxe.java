package model.items.weapons;

public abstract class Pickaxe extends AxeWeapon implements MiningTool {

    public Pickaxe(String name, int cost, int[] damage) {
        super(name, cost, damage, false);
    }
}

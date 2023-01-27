package model.items.accessories;

public abstract class ShieldItem extends Accessory {

    private final boolean heavyArmor;

    public ShieldItem(String name, int cost, boolean isHeavy) {
        super(name, cost);
        this.heavyArmor = isHeavy;
    }

    @Override
    public boolean isHeavy() {
        return heavyArmor;
    }

    @Override
    public String getSound() {
        return "wood";
    }
}

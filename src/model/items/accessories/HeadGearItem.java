package model.items.accessories;

public abstract class HeadGearItem extends Accessory {
    public HeadGearItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public String getSound() {
        return "chainmail1";
    }

    @Override
    public int getWeight() {
        return 500;
    }
}

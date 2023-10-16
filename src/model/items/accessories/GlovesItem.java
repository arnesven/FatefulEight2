package model.items.accessories;

public abstract class GlovesItem extends Accessory {
    public GlovesItem(String name, int cost) {
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

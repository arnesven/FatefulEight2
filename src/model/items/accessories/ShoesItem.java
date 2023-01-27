package model.items.accessories;

public abstract class ShoesItem extends Accessory {
    public ShoesItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public String getSound() {
        return "chainmail1";
    }
}

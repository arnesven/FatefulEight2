package model.items.accessories;

public abstract class JewelryItem extends Accessory {

    public JewelryItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public String getSound() {
        return "metal-ringing";
    }
}

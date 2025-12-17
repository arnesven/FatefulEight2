package model.items;

public abstract class InventoryDummyItem extends Item {
    public InventoryDummyItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public String getSound() {
        return "";
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    public boolean keepInStock() {
        return true;
    }

    public String getDescription() {
        return getName();
    }
}

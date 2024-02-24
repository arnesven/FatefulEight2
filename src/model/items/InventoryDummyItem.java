package model.items;

public abstract class InventoryDummyItem extends Item {
    public InventoryDummyItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public String getSound() {
        throw new IllegalStateException("Should not be called!");
    }
}

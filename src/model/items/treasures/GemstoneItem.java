package model.items.treasures;

public abstract class GemstoneItem extends TreasureItem {

    public GemstoneItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}

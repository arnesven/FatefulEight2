package model.items.treasures;

public abstract class GemstoneItem extends TreasureItem {
    // TODO: Make stackable
    public GemstoneItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public int getWeight() {
        return 1;
    }
}

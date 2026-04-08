package model.items.treasures;

public abstract class GemstoneItem extends TreasureItem {

    public GemstoneItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public int getWeight() {
        return 50;
    }
}

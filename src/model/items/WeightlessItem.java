package model.items;

public abstract class WeightlessItem extends Item {
    public WeightlessItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public int getWeight() {
        return 0;
    }
}

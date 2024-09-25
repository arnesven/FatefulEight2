package model.items;

public class IngredientsForSale extends IngredientsDummyItem {
    private static final int FACTOR = 5;
    private final int cost;

    public IngredientsForSale(int amount) {
        super(amount);
        this.cost = amount * FACTOR;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public boolean keepInStock() {
        return false;
    }
}

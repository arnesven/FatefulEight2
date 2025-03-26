package model.states.dailyaction.shops;

import model.Model;
import model.items.Item;
import model.states.ShopState;
import util.MyRandom;

import java.util.List;

public class GeneralShopNode extends ShoppingNode {
    private final int column;
    private final int row;

    protected GeneralShopNode(Model model, int column, int row, String name) {
        super(model, name);
        this.column = column;
        this.row = row;
    }

    public GeneralShopNode(Model model, int column, int row) {
        this(model, column, row, "General Shop");
    }

    protected List<Item> makeInventory(Model model) {
        return ShopState.makeGeneralShopInventory(model,
                MyRandom.randInt(7, 12), MyRandom.randInt(10, 20), MyRandom.randInt(6));
    }

    @Override
    protected int getShopSecurity() {
        return 8;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}

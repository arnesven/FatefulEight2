package model.states.dailyaction;

import model.Model;

public class GeneralShopNode extends ShoppingNode {
    private final int column;
    private final int row;
    public GeneralShopNode(Model model, int column, int row) {
        super(model);
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}

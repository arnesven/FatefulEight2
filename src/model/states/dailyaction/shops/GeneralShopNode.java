package model.states.dailyaction.shops;

import model.Model;
import model.items.Item;
import model.states.ShopState;
import util.MyRandom;
import view.MyColors;
import view.sprites.MiniItemSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.List;

public class GeneralShopNode extends ShoppingNode {
    protected static final Sprite GENERAL_SHOP_SIGN =
            new Sprite32x32("shopsign", "world_foreground.png", 0x7D,
                MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE);

    private static final Sprite[] SHOP_DECORATIONS = new Sprite[]{
            GENERAL_SHOP_SIGN,
            new MiniItemSprite(0, MyColors.LIGHT_GRAY, MyColors.BROWN),
            new MiniItemSprite(3, MyColors.LIGHT_GRAY, MyColors.RED),
    };

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
    protected Sprite[] getShopDecorations() {
        return SHOP_DECORATIONS;
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

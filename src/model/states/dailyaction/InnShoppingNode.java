package model.states.dailyaction;

import model.Model;
import model.items.Item;
import model.states.ShopState;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.List;

public class InnShoppingNode extends ShoppingNode {
    public InnShoppingNode(Model model) {
        super(model, "Merchant");
    }

    protected List<Item> makeInventory(Model model) {
        return ShopState.makeGeneralShopInventory(model,
                MyRandom.randInt(5, 9), MyRandom.randInt(4, 6), MyRandom.randInt(2));
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return null;
    }
}

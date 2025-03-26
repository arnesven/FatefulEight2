package model.states.dailyaction.tavern;

import model.Model;
import model.items.Item;
import model.states.ShopState;
import model.states.dailyaction.shops.ShoppingNode;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.ArrayList;
import java.util.List;

public class InnShoppingNode extends ShoppingNode {
    public InnShoppingNode(Model model) {
        super(model, "Merchant");
    }

    @Override
    protected boolean supportsBreakIn() {
        return false;
    }

    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>(ShopState.makeGeneralShopInventory(model,
                MyRandom.randInt(5, 9), MyRandom.randInt(4, 6), MyRandom.randInt(2)));
        return inventory;
    }

    @Override
    protected int getShopSecurity() {
        return 7;
    }

    @Override
    protected int[] getSpecialPrices(List<Item> inventory) {
        int[] prices = new int[inventory.size()];
        for (int i = 0; i < inventory.size(); ++i) {
            prices[i] = inventory.get(i).getCost();
        }
        return prices;
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

package model.states.dailyaction;

import model.Model;
import model.items.Item;
import model.items.Lockpick;
import model.states.ShopState;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.ArrayList;
import java.util.List;

public class InnShoppingNode extends ShoppingNode {
    public InnShoppingNode(Model model) {
        super(model, "Merchant");

    }

    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>(ShopState.makeGeneralShopInventory(model,
                MyRandom.randInt(5, 9), MyRandom.randInt(4, 6), MyRandom.randInt(2)));
        if (MyRandom.flipCoin()) {
            inventory.add(new Lockpick());
        }
        inventory.add(model.getItemDeck().getRandomSpell());
        inventory.add(model.getItemDeck().getRandomSpell());
        return inventory;
    }

    @Override
    protected int[] getSpecialPrices(List<Item> inventory) {
        int[] prices = new int[inventory.size()];
        for (int i = 0; i < inventory.size(); ++i) {
            prices[i] = inventory.get(i).getCost();
        }
        prices[inventory.size()-1] = prices[inventory.size()-1] / 2;
        prices[inventory.size()-2] = prices[inventory.size()-2] / 2;
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

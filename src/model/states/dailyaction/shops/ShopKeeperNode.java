package model.states.dailyaction.shops;

import model.Model;
import model.items.Item;
import model.states.GameState;
import model.states.ShopState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.ShopInteriorState;
import view.sprites.Sprite;
import view.subviews.ShopInteriorSubView;

import java.awt.*;
import java.util.List;

public class ShopKeeperNode extends DailyActionNode {
    private final String shopName;
    private final List<Item> inventory;
    private final int[] prices;
    private final boolean[] haggle;

    public ShopKeeperNode(String name, List<Item> shopInventory, int[] specialPrices, boolean[] haggleFlag) {
        super("Shop Keeper");
        this.shopName = name;
        this.inventory = shopInventory;
        this.prices = specialPrices;
        this.haggle = haggleFlag;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        state.leaderSay("Got anything good?");
        String line = "Lot's of good stuff. Have a look.";
        if (model.getSubView() instanceof ShopInteriorSubView) {
            model.getLog().waitForAnimationToFinish();
            ((ShopInteriorSubView)model.getSubView()).addCalloutAtMerchant(line.length());
        }
        state.printQuote("Shopkeeper", line);
        model.getLog().waitForReturn();
        return new ShopState(model, shopName, inventory, prices, haggle);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {

    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }
}

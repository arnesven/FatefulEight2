package model.states.dailyaction.shops;

import model.Model;
import model.enemies.behaviors.BleedAttackBehavior;
import model.items.Item;
import model.states.GameState;
import model.states.ShopState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.ShopInteriorSubView;

import java.awt.*;
import java.util.List;

public class ShopKeeperNode extends DailyActionNode {
    private final String shopName;
    private final List<Item> inventory;
    private final int[] prices;
    private final boolean[] haggle;
    private final ShopSupplier supplier;

    public ShopKeeperNode(String name, List<Item> shopInventory,
                          int[] specialPrices, boolean[] haggleFlag,
                          ShopSupplier supplier) {
        super("Shop Keeper");
        this.shopName = name;
        this.inventory = shopInventory;
        this.prices = specialPrices;
        this.haggle = haggleFlag;
        this.supplier = supplier;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (supplier != null && supplier.isDealMade()) {
            return new ShopKeeperRefusesToTradeState(model, false);
        }
        if (model.getDay() - ShopSupplier.getDealDay(model) < 2) {
            return new ShopKeeperRefusesToTradeState(model, true);
        }
        state.leaderSay(MyRandom.sample(List.of("Got anything good?", "Can I see your wares?",
                "How is business?", "I want to do some shopping.")));
        String line = MyRandom.sample(List.of("Lot's of good stuff. Have a look.",
                "I have some new stuff, and all the usual things as well.",
                "You've come to the right place.", "Please, browse freely.",
                "Business is always good, when you have loyal customers.",
                "The finest merchandise!"));
        shopKeeperSay(model, state, line);
        model.getLog().waitForReturn();
        return new ShopState(model, shopName, inventory, prices, haggle);
    }

    private void shopKeeperSay(Model model, GameState state, String line) {
        if (model.getSubView() instanceof ShopInteriorSubView) {
            model.getLog().waitForAnimationToFinish();
            ((ShopInteriorSubView)model.getSubView()).addCalloutAtMerchant(line.length());
        }
        state.printQuote(shopName, line);
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

    private class ShopKeeperRefusesToTradeState extends GameState {
        private final boolean fromColleague;

        public ShopKeeperRefusesToTradeState(Model model, boolean fromColleague) {
            super(model);
            this.fromColleague = fromColleague;
        }

        @Override
        public GameState run(Model model) {
            if (fromColleague) {
                shopKeeperSay(model, this, "I colleague of mine told me not to deal with you.");
                leaderSay("Come on...");
                shopKeeperSay(model, this, "Please leave now.");
                return null;
            }

            if (MyRandom.flipCoin()) {
                String itemName = supplier.getItem().getName();
                if (!itemName.endsWith("s")) {
                    itemName += "s";
                }
                leaderSay("Hey, wanna buy some " + itemName+ "?");
                shopKeeperSay(model, this, "Think you're being funny? Get out!");
            } else {
                shopKeeperSay(model, this, "Hey! I saw you doing business with my supplier. " +
                        "I'm not doing any business with you.");
                leaderSay("What? That's not fair.");
                shopKeeperSay(model, this, "My shop, my rules.");
            }
            return null;
        }
    }
}

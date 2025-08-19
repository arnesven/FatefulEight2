package model.states.dailyaction.tavern;

import model.Model;
import model.TimeOfDay;
import model.items.Item;
import model.states.GameState;
import model.states.ShopState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.shops.ShoppingNode;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InnShoppingNode extends DailyActionNode {
    private static final Point CURSOR_SHIFT = new Point(0, -8);
    private final List<Item> inventory;
    private boolean[] haggleFlag = new boolean[]{true};

    public InnShoppingNode(Model model) {
        super("Merchant");
        this.inventory = makeInventory(model);
    }

    @Override
    public Point getCursorShift() {
        return CURSOR_SHIFT;
    }

    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>(ShopState.makeGeneralShopInventory(model,
                MyRandom.randInt(5, 9), MyRandom.randInt(4, 6), MyRandom.randInt(2)));
        return inventory;
    }

    protected int[] getSpecialPrices(List<Item> inventory) {
        int[] prices = new int[inventory.size()];
        for (int i = 0; i < inventory.size(); ++i) {
            prices[i] = inventory.get(i).getCost();
        }
        return prices;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new ShopState(model, getName(), inventory, getSpecialPrices(inventory), haggleFlag);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return null;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}

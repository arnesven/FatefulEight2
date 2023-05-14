package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.items.Item;
import model.states.GameState;
import model.states.ShopState;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;
import java.util.List;

public abstract class ShoppingNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SIGN = new SignSprite("generalsign", 0x06, MyColors.BLUE, MyColors.WHITE);
    private List<Item> shopInventory;
    public ShoppingNode(Model model, String name) {
        super(name);
        shopInventory = makeInventory(model);
    }

    protected abstract List<Item> makeInventory(Model model);

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new ShopState(model, getName(), shopInventory, getSpecialPrices(shopInventory));
    }

    protected int[] getSpecialPrices(List<Item> inventory) {
        return null;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg, 1);
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        if (townDailyActionState.isEvening()) {
            townDailyActionState.println("The shop is closed. Please come again tomorrow.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}

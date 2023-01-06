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

import java.util.List;

class ShoppingNode extends DailyActionNodeWithSign {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
            TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SIGN = new SignSprite("generalsign", 0x06, MyColors.BLUE, MyColors.WHITE);
    private List<Item> shopInventory;
    public ShoppingNode(Model model) {
        super("General Store");
         shopInventory = ShopState.makeRandomShopInventory(model,
                MyRandom.randInt(5, 9), MyRandom.randInt(4, 6), MyRandom.randInt(2));
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new ShopState(model, getName(), shopInventory, null);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
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

package model.states.dailyaction;

import model.Model;
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

class ShoppingNode extends DailyActionNode {
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
    public GameState getDailyAction(Model model) {
        return new ShopState(model, "general store", shopInventory, null);
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
    public boolean canBeDoneDuring(Model model, TownDailyActionState townDailyActionState, int timeOfDay) {
        if (timeOfDay == TownDailyActionState.MORNING) {
            return true;
        }
        townDailyActionState.println("The shop is closed. Please come again tomorrow.");
        return false;
    }

    @Override
    public boolean isFreeAction() {
        return true;
    }
}

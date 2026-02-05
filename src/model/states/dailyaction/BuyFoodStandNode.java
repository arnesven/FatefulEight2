package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class BuyFoodStandNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("buyfoodstand", "quest.png", 0x69,
            MyColors.GREEN, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.BEIGE);

    public BuyFoodStandNode() {
        super("Buy Rations");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new BuyRationsState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {}
}

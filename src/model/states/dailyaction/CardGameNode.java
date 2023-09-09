package model.states.dailyaction;

import model.Model;
import model.states.CardGameState;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

public class CardGameNode extends DailyActionNode {
    public static final Sprite TABLE = new Sprite32x32("cardstable", "world_foreground.png", 0x88,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN, MyColors.WHITE);

    public CardGameNode() {
        super("Play Cards");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CardGameState(model);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return TABLE;
    }
}

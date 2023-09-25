package model.states.dailyaction;

import model.Model;
import model.states.cardgames.CardGameState;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

public class CardGameNode extends DailyActionNode {
    public static final Sprite TABLE = new Sprite32x32("cardstable", "world_foreground.png", 0x88,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN, MyColors.WHITE);
    private CardGameState cardGameState = null;

    public CardGameNode() {
        super("Play Cards");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (cardGameState == null) {
            cardGameState = new CardGameState(model);
        }
        return cardGameState;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (cardGameState == null) {
            return true;
        }
        if (cardGameState.getNumberOfPlayers() < 2) {
            state.println("It seems like nobody is interested in playing any more.");
            return false;
        }
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

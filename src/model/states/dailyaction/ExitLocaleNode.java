package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ExitLocaleNode extends DailyActionNode {
    private final Sprite bgSprite;

    public ExitLocaleNode(String label, Sprite bgSprite) {
        super(label);
        this.bgSprite = bgSprite;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        model.getParty().unbenchAll();
        return null;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return bgSprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public boolean exitsCurrentLocale() {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}

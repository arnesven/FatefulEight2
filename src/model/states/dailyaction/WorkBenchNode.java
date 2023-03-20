package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CastleSubView;
import view.subviews.TownSubView;

public class WorkBenchNode extends DailyActionNode {
    private static final Sprite SPRITE1 = new Sprite32x32("workbenchtown", "world_foreground.png", 0x71,
            TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.TAN);
    private static final Sprite SPRITE2 = new Sprite32x32("workbenchcastle", "world_foreground.png", 0x71,
            CastleSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.TAN);
    private final Sprite sprite;

    public WorkBenchNode(MyColors groundColor) {
        super("Work Bench");
        if (groundColor == TownSubView.GROUND_COLOR) {
            sprite = SPRITE1;
        } else {
            sprite = SPRITE2;
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CraftItemState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return sprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}

package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ExitTavernNode extends DailyActionNode {
    private static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.DARK_RED);

    public ExitTavernNode() {
        super("Leave Tavern");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new ExitTavernState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return DOOR;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public boolean isFreeAction() {
        return true;
    }

    @Override
    public boolean exitsTown() {
        return true;
    }

    private class ExitTavernState extends GameState {
        public ExitTavernState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            return null;
        }
    }
}

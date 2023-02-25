package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import model.states.events.SilentNoEventState;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.TavernSubView;
import view.subviews.TownHallSubView;

import java.awt.*;

public class TownHallDailyActionState extends AdvancedDailyActionState {
    public TownHallDailyActionState(Model model) {
        super(model);
        addNode(4, 3, new TalkToLordNode());
        addNode(3, 7, new ExitLocaleNode("Leave Town Hall"));
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new TownHallSubView(advancedDailyActionState, matrix);
    }

    private static class TalkToLordNode extends DailyActionNode {
        public TalkToLordNode() {
            super("Talk to Lord");
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new AnswerSummonDailyAction(model);
        }

        @Override
        public Sprite getBackgroundSprite() {
            return TownHallSubView.RUG;
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            return true;
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
    }

    private static class AnswerSummonDailyAction extends GameState {
        public AnswerSummonDailyAction(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            println("Lord: \"Hello there, I am the lord of this town.\"");
            return new SilentNoEventState(model);
        }
    }
}

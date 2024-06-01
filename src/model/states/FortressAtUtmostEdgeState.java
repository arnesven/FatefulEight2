package model.states;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.FortressAtUtmostEdgeSubView;

import java.awt.*;

public class FortressAtUtmostEdgeState extends AdvancedDailyActionState {
    private static final Point STARTING_POINT = new Point(7, 8);
    private static final Point CASTLE_PROPER_POSITION = new Point(4, 5);

    public FortressAtUtmostEdgeState(Model model) {
        super(model);
        addNode(7, 8, new LeaveFatueNode());
        addNode(CASTLE_PROPER_POSITION.x, CASTLE_PROPER_POSITION.y, new EnterCastleProperNode());
        addNode(2, 5, new WestWingNode());
    }

    @Override
    protected Point getStartingPosition() {
        return STARTING_POINT;
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new FortressAtUtmostEdgeSubView(advancedDailyActionState, matrix);
    }


    private abstract static class FatueDailyActionNode extends DailyActionNode {
        public FatueDailyActionNode(String name) {
            super(name);
        }

        @Override
        public Sprite getBackgroundSprite() {
            return null;
        }

        @Override
        public void drawYourself(Model model, Point p) { }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            return true;
        }
    }

    private static class LeaveFatueNode extends FatueDailyActionNode {
        public LeaveFatueNode() {
            super("Leave Fortress at the Ultimate Edge");
        }

        @Override
        public boolean exitsCurrentLocale() {
            return true;
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new NoLodgingState(model, false);
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { model.setTimeOfDay(TimeOfDay.EVENING); }

    }


    private static class EnterCastleProperNode extends FatueDailyActionNode {

        public EnterCastleProperNode() {
            super("Enter Castle Proper");
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new GameState(model) {
                @Override
                public GameState run(Model model) {
                    return null;
                }
            };
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
    }

    private class WestWingNode extends FatueDailyActionNode {
        public WestWingNode() {
            super("Enter West Wing");
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new GameState(model) {
                @Override
                public GameState run(Model model) {
                    return null;
                }
            };
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            return getCurrentPosition().equals(CASTLE_PROPER_POSITION);
        }
    }
}

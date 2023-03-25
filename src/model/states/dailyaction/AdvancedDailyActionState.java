package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.GameState;
import model.tutorial.TutorialHandler;
import view.help.TutorialStartDialog;
import view.subviews.*;

import java.awt.*;

public abstract class AdvancedDailyActionState extends GameState {

    public static final int TOWN_MATRIX_ROWS = 9;
    public static final int TOWN_MATRIX_COLUMNS = 8;
    private DailyActionSubView subView;
    private Point currentPosition;
    private SteppingMatrix<DailyActionNode> matrix;

    public AdvancedDailyActionState(Model model) {
        super(model);
        matrix = new SteppingMatrix<>(TOWN_MATRIX_COLUMNS, TOWN_MATRIX_ROWS);
        currentPosition = getStartingPosition();
    }

    protected abstract Point getStartingPosition();

    protected abstract DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                           SteppingMatrix<DailyActionNode> matrix);

    protected void addNode(int col, int row, DailyActionNode node) {
        if (matrix.getElementAt(col, row) != null) {
            System.err.println("WARNING, overwriting stepping matrix cell with " + node.getName());
        }
        matrix.addElement(col, row, node);
    }

    @Override
    public GameState run(Model model) {
        subView = makeSubView(model, this, matrix);
        DailyActionNode daily = null;
        while (!model.gameExited()) {
            if (model.getSubView() != subView) {
                CollapsingTransition.transition(model, subView);
            }
            model.getTutorial().start(model);
            String place = model.getCurrentHex().getPlaceName();
            print("You are " + place + ". ");
            if (!isEvening()) {
                print("Please select your daily action.");
            } else {
                print("Please select how you will spend the evening.");
            }
            model.getTutorial().theInn(model);
            waitForReturn();
            daily = matrix.getSelectedElement();
            if (daily.canBeDoneRightNow(this, model)) {
                Point destination = new Point(matrix.getSelectedPoint());
                subView.animateMovement(model, new Point(currentPosition.x, currentPosition.y), destination);
                currentPosition = destination;
                if (daily.exitsCurrentLocale()) {
                    break;
                } else {
                    subView.setCursorEnabled(false);
                    GameState nextState = daily.getDailyAction(model, this).run(model);
                    subView.setCursorEnabled(true);
                    daily.setTimeOfDay(model, this);
                    if (daily.returnNextState() && nextState != null) {
                        return nextState;
                    }
                }
            }
            System.out.println("Advanced daily action loop");
        }

        return daily.getDailyAction(model, this);
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public boolean isMorning() {
        return getModel().getTimeOfDay() == TimeOfDay.MORNING;
    }

    public boolean isEvening() { return getModel().getTimeOfDay() == TimeOfDay.EVENING; }

}

package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import view.subviews.*;

import java.awt.*;

public abstract class AdvancedDailyActionState extends GameState {

    public static final int TOWN_MATRIX_ROWS = 9;
    public static final int TOWN_MATRIX_COLUMNS = 8;
    public static final int MORNING = 0;
    public static final int EVENING = 1;
    private DailyActionSubView subView;
    private Point currentPosition;
    private int timeOfDay;
    private SteppingMatrix<DailyActionNode> matrix;

    public AdvancedDailyActionState(Model model, boolean isEvening) {
        super(model);
        matrix = new SteppingMatrix<>(TOWN_MATRIX_COLUMNS, TOWN_MATRIX_ROWS);
        if (!isEvening) {
            timeOfDay = MORNING;
        } else {
            timeOfDay = EVENING;
        }
        currentPosition = getStartingPosition();
    }

    protected abstract Point getStartingPosition();

    protected abstract DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                           SteppingMatrix<DailyActionNode> matrix);

    protected void addNode(int col, int row, DailyActionNode node) {
        matrix.addElement(col, row, node);
    }

    @Override
    public GameState run(Model model) {
        subView = makeSubView(model, this, matrix);
        DailyActionNode daily;
        while (true) {
            if (model.getSubView() != subView) {
                CollapsingTransition.transition(model, subView);
            }
            String place = model.getCurrentHex().getPlaceName();
            print("You are " + place + ". ");
            if (isMorning()) {
                print("Please select your daily action.");
            } else {
                print("Please select how you will spend the evening.");
            }
            waitForReturn();
            daily = matrix.getSelectedElement();
            if (daily.canBeDoneRightNow(this)) {
                Point destination = new Point(matrix.getSelectedPoint());
                subView.animateMovement(model, new Point(currentPosition.x, currentPosition.y), destination);
                if (daily.exitsTown()) {
                    break;
                } else {
                    daily.getDailyAction(model).run(model);
                    if (!daily.isFreeAction()) {
                        timeOfDay = EVENING;
                    }
                    currentPosition = destination;
                }
            }
        }

        return daily.getDailyAction(model);
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public boolean isMorning() {
        return timeOfDay == MORNING;
    }
}

package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.GameState;
import model.tutorial.TutorialHandler;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.help.TutorialStartDialog;
import view.subviews.*;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class AdvancedDailyActionState extends GameState {

    public static final int TOWN_MATRIX_ROWS = 9;
    public static final int TOWN_MATRIX_COLUMNS = 8;
    private DailyActionSubView subView;
    private Point currentPosition;
    private SteppingMatrix<DailyActionNode> matrix;
    private boolean firstTimeEvening = true;
    private boolean firstTimeDayTime = true;
    private final Set<Integer> blockedPositions = new HashSet<>();

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

    public void addNodeInFreeSlot(DailyActionNode node, int seed) {
        if (!matrix.isFull()) {
            Random random = new Random(seed);
            for (int i = 0; i < 1000; i++) {
                int col = random.nextInt(matrix.getColumns()-(node.getWidth()-1));
                int row = random.nextInt(matrix.getRows());
                if (matrix.getElementAt(col, row) == null) {
                    matrix.addElement(col, row, node);
                    return;
                }
            }
            throw new IllegalStateException("Could not add node in free slot after 1000 iterations!");
        }
        throw new IllegalStateException("Cannot add another node to advanced daily action state, matrix is full!");
    }

    @Override
    public GameState run(Model model) {
        subView = makeSubView(model, this, matrix);
        DailyActionNode daily = null;
        if (hasSound()) {
            ClientSoundManager.playBackgroundMusic(getSound());
        }
        while (!model.gameExited()) {
            if (model.getSubView() != subView) {
                CollapsingTransition.transition(model, subView);
            }
            model.getTutorial().start(model);
            printPrompt(model);
            model.getTutorial().theInn(model);
            waitForReturnSilently();
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
        }

        return daily.getDailyAction(model, this);
    }

    protected BackgroundMusic getSound() {
        return null;
    }

    private boolean hasSound() {
        return getSound() != null;
    }

    private void printPrompt(Model model) {
        if (isEvening()) {
            if (!firstTimeEvening) {
                return;
            }
        } else {
            if (!firstTimeDayTime) {
                return;
            }
        }
        String place = model.getCurrentHex().getPlaceName();
        print("You are " + place + ". ");
        model.getTutorial().basicControls(model);
        if (!isEvening()) {
            println("Please select your daily action.");
            firstTimeDayTime = false;
        } else {
            println("Please select how you will spend the evening.");
            firstTimeEvening = false;
        }
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public boolean isMorning() {
        return getModel().getTimeOfDay() == TimeOfDay.MORNING;
    }

    public boolean isEvening() { return getModel().getTimeOfDay() == TimeOfDay.EVENING; }

    public boolean isPositionBlocked(int col, int row) {
        return blockedPositions.contains(row * 1000 + col);
    }

    protected void blockPosition(int col, int row) {
        blockedPositions.add(row * 1000 + col);
    }
}

package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import view.subviews.CollapsingTransition;
import view.subviews.TownSubView;

import java.awt.*;

public class TownDailyActionState extends GameState {
    
    public static final int TOWN_MATRIX_ROWS = 9;
    public static final int TOWN_MATRIX_COLUMNS = 8;
    public static final int MORNING = 0;
    public static final int EVENING = 1;
    private final TownSubView subView;
    private Point currentPosition;
    private int timeOfDay;

    private SteppingMatrix<DailyActionNode> matrix;

    public TownDailyActionState(Model model, boolean isCoastal, boolean isEvening, boolean freeLodging, boolean freeRations) {
        super(model);
        matrix = new SteppingMatrix<>(TOWN_MATRIX_COLUMNS, TOWN_MATRIX_ROWS);
        if (!isEvening) {
            timeOfDay = MORNING;
        } else {
            timeOfDay = EVENING;
        }

        matrix.addElement(3, 4, new StayHereNode());
        matrix.addElement(6, 1, new ShoppingNode(model));
        matrix.addElement(1, 4, new TavernNode());
        matrix.addElement(3, 2, new TownHallNode());
        matrix.addElement(2, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations));
        matrix.addElement(TOWN_MATRIX_COLUMNS-1, TOWN_MATRIX_ROWS-2, new TravelNode());
        this.subView = new TownSubView(this, matrix, isCoastal);
        currentPosition = new Point(3, 5);
    }

    public TownDailyActionState(Model model, boolean isCoastal, boolean isEvening) {
        this(model, isCoastal, isEvening, false, false);
    }

    @Override
    public GameState run(Model model) {
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
            if (daily.canBeDoneDuring(model, this, timeOfDay)) {
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

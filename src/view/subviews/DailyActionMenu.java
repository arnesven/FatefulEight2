package view.subviews;

import model.Model;
import model.actions.DailyAction;
import util.MyPair;

import java.awt.*;
import java.util.List;

public class DailyActionMenu extends ArrowMenuSubView {
    public static final MyPair<Point, Integer> LOWER_RIGHT_CORNER = new MyPair<>(new Point(SubView.X_MAX-1, SubView.Y_MAX-1), SOUTH_EAST);
    public static final MyPair<Point, Integer> LOWER_LEFT_CORNER = new MyPair<>(new Point(SubView.X_OFFSET, SubView.Y_MAX-1), SOUTH_WEST);
    public static final MyPair<Point, Integer> UPPER_RIGHT_CORNER = new MyPair<>(new Point(SubView.X_MAX-1, SubView.Y_OFFSET), NORTH_EAST);
    public static final MyPair<Point, Integer> UPPER_LEFT_CORNER = new MyPair<>(new Point(SubView.X_OFFSET, SubView.Y_OFFSET), DailyActionMenu.NORTH_WEST);;

    private final List<DailyAction> actions;
    private DailyAction selectedAction;

    public DailyActionMenu(SubView previous, List<DailyAction> actions, List<String> actionNames, int xPos, int yPos, int anchor) {
        super(previous, actionNames, xPos, yPos, anchor);
        this.actions = actions;
        selectedAction = actions.get(0);
    }

    @Override
    protected void enterPressed(Model model, int cursorPos) {
        selectedAction = actions.get(cursorPos);
    }

    public DailyAction getSelectedAction() {
        return selectedAction;
    }

}

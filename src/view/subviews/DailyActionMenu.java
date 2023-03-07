package view.subviews;

import model.Model;
import model.actions.DailyAction;
import util.MyPair;
import view.ArrowMenuGameView;
import view.GameView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class DailyActionMenu extends SubView {
    public static final int NORTH_WEST = 0;
    public static final int NORTH_EAST = 1;
    public static final int SOUTH_WEST = 2;
    public static final int SOUTH_EAST = 3;
    public static final MyPair<Point, Integer> LOWER_RIGHT_CORNER = new MyPair<>(new Point(SubView.X_MAX-1, SubView.Y_MAX-1), SOUTH_EAST);
    public static final MyPair<Point, Integer> LOWER_LEFT_CORNER = new MyPair<>(new Point(SubView.X_OFFSET, SubView.Y_MAX-1), SOUTH_WEST);
    public static final MyPair<Point, Integer> UPPER_RIGHT_CORNER = new MyPair<>(new Point(SubView.X_MAX-1, SubView.Y_OFFSET), NORTH_EAST);
    public static final MyPair<Point, Integer> UPPER_LEFT_CORNER = new MyPair<>(new Point(SubView.X_OFFSET, SubView.Y_OFFSET), DailyActionMenu.NORTH_WEST);;

    private final List<DailyAction> actions;
    private final SubView previous;
    private DailyAction selectedAction;
    private ArrowMenuGameView arrowMenuGameView;

    public DailyActionMenu(SubView previous, List<DailyAction> actions, List<String> actionNames, int xPos, int yPos, int anchor) {
        this.previous = previous;
        this.actions = actions;
        selectedAction = actions.get(0);

        int xStart = xPos;
        int yStart = yPos;
        int width = 4+findMax(actionNames);
        int height = actionNames.size()+5;

        if (anchor == NORTH_EAST) {
            xStart -= width;
        } else if (anchor == SOUTH_WEST) {
            yStart -= height;
        } else if (anchor == SOUTH_EAST) {
            xStart -= width;
            yStart -= height;
        }

        arrowMenuGameView = new ArrowMenuGameView(false, xStart, yStart, width, height, actionNames) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedAction = actions.get(cursorPos);
            }

            @Override
            protected boolean optionEnabled(Model model, int i) {
                return true;
            }

            @Override
            public GameView getNextView(Model model) {
                throw new IllegalStateException("Should not call getNextView here!");
            }
        };
        arrowMenuGameView.setQuitSoundEnabled(false);
    }

    private int findMax(List<String> actionNames) {
        int result = 0;
        for (String s : actionNames) {
            if (s.length() > result) {
                result = s.length();
            }
        }
        return result;
    }

    public DailyAction getSelectedAction() {
        return selectedAction;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        arrowMenuGameView.transitionedTo(model);
        arrowMenuGameView.update(model);
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        arrowMenuGameView.handleKeyEvent(keyEvent, model);
        return false;
    }
}

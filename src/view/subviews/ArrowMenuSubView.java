package view.subviews;

import model.Model;
import view.ArrowMenuGameView;
import view.GameView;

import java.awt.event.KeyEvent;
import java.util.List;

public abstract class ArrowMenuSubView extends SubView {
    public static final int NORTH_WEST = 0;
    public static final int NORTH_EAST = 1;
    public static final int SOUTH_WEST = 2;
    public static final int SOUTH_EAST = 3;

    private final ArrowMenuGameView arrowMenuGameView;
    private final SubView previous;

    public ArrowMenuSubView(SubView previous, List<String> labels, int xPos, int yPos, int anchor) {
        super(previous.getCenterTextHeight());
        this.previous = previous;

        int xStart = xPos;
        int yStart = yPos;
        int width = 4+findMax(labels);
        int height = labels.size()*2 + 2;
        if (labels.size() > ArrowMenuGameView.MAX_LABELS) {
            height = 2*ArrowMenuGameView.MAX_LABELS + 2;
        }

        if (anchor == NORTH_EAST) {
            xStart -= width;
        } else if (anchor == SOUTH_WEST) {
            yStart -= height;
        } else if (anchor == SOUTH_EAST) {
            xStart -= width;
            yStart -= height;
        }

        yStart = Math.max(0, yStart);
        xStart = Math.max(0, xStart);

        arrowMenuGameView = new ArrowMenuGameView(false, xStart, yStart, width, height, labels) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                ArrowMenuSubView.this.enterPressed(model, cursorPos);
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

    protected abstract void enterPressed(Model model, int cursorPos);

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

    private static int findMax(List<String> labels) {
        int result = 0;
        for (String s : labels) {
            if (s.length() > result) {
                result = s.length();
            }
        }
        return result;
    }

    protected SubView getPrevious() {
        return previous;
    }
}

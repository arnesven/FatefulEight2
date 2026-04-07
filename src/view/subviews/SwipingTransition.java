package view.subviews;

import model.Model;
import model.states.mine.LogicalMine;
import view.sprites.AnimationManager;
import view.sprites.FilledBlockSprite;

public class SwipingTransition extends TransitionView {

    private static final int HORIZONTAL_STEPS = X_MAX - X_OFFSET - 1;
    private static final int VERTICAL_STEPS = Y_MAX - Y_OFFSET - 1;
    private final Action action;
    private final int direction;
    private final int maxSteps;
    private boolean flipped;

    public static abstract class Action {
        public abstract void doAction();
    }

    private SwipingTransition(SubView fromView, SubView toView, String title, int direction, Action act, int stepsStart) {
        super(fromView, toView, title, stepsStart);
        this.direction = direction;
        this.action = act;
        this.flipped = false;
        this.maxSteps = getStepsForDirection(direction);
    }

    @Override
    protected void drawAnimation(Model model, int steps) {
        if (steps == 0) {
            action.doAction();
            flipped = true;
        }
        steps = (steps / 4) * 4;
        int stepsInverted = maxSteps - steps;

        if (flipped) {
            if (direction == LogicalMine.EAST) {
                paintBlack(model, X_OFFSET + steps, X_MAX, Y_OFFSET, Y_MAX - 1);
            } else if (direction == LogicalMine.WEST) {
                paintBlack(model, X_OFFSET, X_OFFSET + stepsInverted, Y_OFFSET, Y_MAX - 1);
            } else if (direction == LogicalMine.NORTH) {
                paintBlack(model, X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET + stepsInverted);
            } else {
                paintBlack(model, X_OFFSET, X_MAX, Y_OFFSET + steps, Y_MAX);
            }

        } else {
            if (direction == LogicalMine.EAST) {
                paintBlack(model, X_OFFSET, X_OFFSET + stepsInverted, Y_OFFSET, Y_MAX - 1);
            } else if (direction == LogicalMine.WEST) {
                paintBlack(model, X_OFFSET + steps, X_MAX, Y_OFFSET, Y_MAX - 1);
            } else if (direction == LogicalMine.NORTH) {
                paintBlack(model, X_OFFSET, X_MAX, Y_OFFSET + steps, Y_MAX);
            } else { // SOUTH
                paintBlack(model, X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET + stepsInverted);
            }
        }
    }

    private void paintBlack(Model model, int xStart, int xEnd, int yStart, int yEnd) {
        model.getScreenHandler().clearForeground(xStart, xEnd, yStart, yEnd);
        model.getScreenHandler().fillSpace(xStart, xEnd, yStart, yEnd,
                FilledBlockSprite.BLACK_BLOCK);
    }

    @Override
    protected boolean stepsAreDone(int steps) {
        return steps >= maxSteps;
    }

    public static void transition(Model model, SubView nextSubview, int direction, Action act) {
        SwipingTransition swipe = new SwipingTransition(model.getSubView(), nextSubview,
                model.getSubView().getTitleText(model), direction, act, getStepsForDirection(direction));
        model.setSubView(swipe);
        swipe.waitToBeOver();
        model.setSubView(nextSubview);
        AnimationManager.unregister(swipe);
    }

    private static int getStepsForDirection(int direction) {

        return direction == LogicalMine.NORTH || direction == LogicalMine.SOUTH ? VERTICAL_STEPS : HORIZONTAL_STEPS;
    }


}

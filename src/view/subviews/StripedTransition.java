package view.subviews;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.sprites.AnimationManager;
import view.sprites.FilledBlockSprite;

public class StripedTransition extends TransitionView {

    public static final boolean VERTICAL = true;
    public static final boolean HORIZONTAL = false;
    private final boolean vertical;

    public StripedTransition(SubView fromView, SubView toView, String title, boolean isVertical) {
        super(fromView, toView, title, isVertical ? ( Y_MAX - Y_OFFSET) : (X_MAX - X_OFFSET));
        this.vertical = isVertical;
    }

    protected void drawAnimation(Model model, int steps) {
        model.getScreenHandler().clearForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        if (vertical) {
            for (int col = X_OFFSET; col < X_MAX; ++col) {
                if (col % 2 == 0) {
                    model.getScreenHandler().fillForeground(col, col + 1, Y_OFFSET, Y_MAX - steps, blackBlock, 3);
                } else {
                    model.getScreenHandler().fillForeground(col, col + 1, Y_OFFSET + steps, Y_MAX, blackBlock, 3);
                }
            }
        } else {
            for (int row = Y_OFFSET; row < Y_MAX; ++row) {
                if (row % 2 == 0) {
                    model.getScreenHandler().fillForeground(X_OFFSET + steps, X_MAX, row, row + 1, blackBlock, 3);
                } else {
                    model.getScreenHandler().fillForeground(X_OFFSET, X_MAX - steps + 1, row, row + 1, blackBlock, 3);
                }
            }
        }
    }

    @Override
    protected boolean stepsAreDone(int steps) {
        if (vertical) {
            if (steps >= (Y_MAX - Y_OFFSET)) {
                return true;
            }
        } else {
            if (steps >= (X_MAX - X_OFFSET)) {
                return true;
            }
        }
        return false;
    }

    public static void transition(Model model, SubView subView) {
        StripedTransition transition;
        if (MyRandom.randInt(2) == 0) {
            transition = new StripedTransition(model.getSubView(), subView, subView.getTitleText(model),
                    StripedTransition.VERTICAL);
        } else {
            transition = new StripedTransition(model.getSubView(), subView, subView.getTitleText(model),
                    StripedTransition.HORIZONTAL);
        }
        model.setSubView(transition);
        transition.waitToBeOver();
        model.setSubView(subView);
        AnimationManager.unregister(transition);
    }
}

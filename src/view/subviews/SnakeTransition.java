package view.subviews;

import model.Model;

public class SnakeTransition extends TransitionView {

    private static final int BLOCK_SIZE = 4;
    private static final int X_WIDTH = (X_MAX - X_OFFSET) / BLOCK_SIZE;
    private static final int Y_WIDTH = (Y_MAX - Y_OFFSET) / BLOCK_SIZE;
    private static final int STEPS_START = X_WIDTH * Y_WIDTH;
    private boolean hasTurned;

    public SnakeTransition(SubView fromView, SubView toView, String title, int stepsStart) {
        super(fromView, toView, title, stepsStart);
        this.hasTurned = false;
    }

    @Override
    protected void drawAnimation(Model model, int steps) {
        if (steps == 0) {
            hasTurned = true;
        }
        int invStep = STEPS_START - steps;
        int x = invStep % X_WIDTH;
        int y = invStep / X_WIDTH;
        if (hasTurned) {
            x = steps % X_WIDTH;
            y = steps / X_WIDTH;
            model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET + (y+1) * BLOCK_SIZE, Y_MAX,
                    blackBlock, 3);
            model.getScreenHandler().fillForeground(X_OFFSET + x * BLOCK_SIZE, X_MAX, Y_OFFSET + y * BLOCK_SIZE, Y_OFFSET + (y + 1) * BLOCK_SIZE,
                    blackBlock, 3);

        } else {
            model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET + y * BLOCK_SIZE,
                    blackBlock, 3);
            model.getScreenHandler().fillForeground(X_OFFSET, X_OFFSET + x * BLOCK_SIZE, Y_OFFSET + y * BLOCK_SIZE, Y_OFFSET + (y + 1) * BLOCK_SIZE,
                    blackBlock, 3);
        }
    }

    @Override
    protected boolean stepsAreDone(int steps) {
        return steps >= STEPS_START;
    }


    public static void transition(Model model, SubView nextSubView) {
        SnakeTransition spiral = new SnakeTransition(model.getSubView(), nextSubView, nextSubView.getTitleText(model), STEPS_START);
        model.setSubView(spiral);
        spiral.waitToBeOver();
        model.setSubView(nextSubView);
    }
}

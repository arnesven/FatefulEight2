package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.AnimationManager;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

public class CollapsingTransition extends TransitionView {

    private static final int STEPS_START = (Y_MAX - Y_OFFSET) / 2;
    private final int xStart;
    private final int xEnd;

    public CollapsingTransition(SubView fromView, SubView toView, String title, int stepsStart, int xStart, int xEnd) {
        super(fromView, toView, title, stepsStart);
        this.xStart = xStart;
        this.xEnd = xEnd;
    }

    public CollapsingTransition(SubView fromView, SubView toView, String title, int stepsStart) {
        this(fromView, toView, title, stepsStart, X_OFFSET, X_MAX);
    }

    @Override
    protected void drawAnimation(Model model, int steps) {
        model.getScreenHandler().clearForeground(xStart, xEnd, Y_OFFSET, Y_OFFSET+(STEPS_START-steps));
        model.getScreenHandler().clearForeground(xStart, xEnd, Y_MAX-(STEPS_START-steps), Y_MAX);

        if (steps < STEPS_START - 1) {
            model.getScreenHandler().clearForeground(xStart, xStart+(STEPS_START-steps-1), Y_OFFSET, Y_MAX);
            model.getScreenHandler().clearForeground(xEnd-(STEPS_START-steps-1), xEnd, Y_OFFSET, Y_MAX);
        }


        model.getScreenHandler().fillForeground(xStart, xEnd, Y_OFFSET, Y_OFFSET+(STEPS_START-steps),
                blackBlock, 33);
        model.getScreenHandler().fillForeground(xStart, xEnd, Y_MAX-(STEPS_START-steps), Y_MAX,
                blackBlock, 33);

        if (steps < STEPS_START - 1) {
            model.getScreenHandler().fillForeground(xStart, xStart+(STEPS_START-steps-1), Y_OFFSET, Y_MAX,
                    blackBlock, 33);
            model.getScreenHandler().fillForeground(xEnd-(STEPS_START-steps-1), xEnd, Y_OFFSET, Y_MAX,
                    blackBlock, 33);
        }
    }

    @Override
    protected boolean stepsAreDone(int steps) {
        return steps >= STEPS_START;
    }


    public static void transition(Model model, SubView nextSubView) {
        CollapsingTransition spiral = new CollapsingTransition(model.getSubView(), nextSubView, nextSubView.getTitleText(model), STEPS_START);
        model.setSubView(spiral);
        spiral.waitToBeOver();
        model.setSubView(nextSubView);
        AnimationManager.unregister(spiral);
    }

    public static void wideTransition(Model model, SubView nextSubView) {
        CollapsingTransition spiral = new CollapsingTransition(model.getSubView(), nextSubView, nextSubView.getTitleText(model), STEPS_START, X_OFFSET-8, X_MAX+8);
        model.setSubView(spiral);
        spiral.waitToBeOver();
        model.setSubView(nextSubView);
        AnimationManager.unregister(spiral);
    }

}

package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

public class CollapsingTransition extends TransitionView {

    private static final int STEPS_START = (Y_MAX - Y_OFFSET) / 2;

    public CollapsingTransition(SubView fromView, SubView toView, String title, int stepsStart) {
        super(fromView, toView, title, stepsStart);
    }

    @Override
    protected void drawAnimation(Model model, int steps) {
        model.getScreenHandler().clearForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+(STEPS_START-steps));
        model.getScreenHandler().clearForeground(X_OFFSET, X_MAX, Y_MAX-(STEPS_START-steps), Y_MAX);

        if (steps < STEPS_START - 1) {
            model.getScreenHandler().clearForeground(X_OFFSET, X_OFFSET+(STEPS_START-steps-1), Y_OFFSET, Y_MAX);
            model.getScreenHandler().clearForeground(X_MAX-(STEPS_START-steps-1), X_MAX, Y_OFFSET, Y_MAX);
        }


        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+(STEPS_START-steps),
                blackBlock, 3);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-(STEPS_START-steps), Y_MAX,
                blackBlock, 3);

        if (steps < STEPS_START - 1) {
            model.getScreenHandler().fillForeground(X_OFFSET, X_OFFSET+(STEPS_START-steps-1), Y_OFFSET, Y_MAX,
                    blackBlock, 3);
            model.getScreenHandler().fillForeground(X_MAX-(STEPS_START-steps-1), X_MAX, Y_OFFSET, Y_MAX,
                    blackBlock, 3);
        }
    }

    @Override
    protected boolean stepsAreDone(int steps) {
        return steps >= STEPS_START;
    }


    public static void transition(Model model, SubView mapSubView) {
        CollapsingTransition spiral = new CollapsingTransition(model.getSubView(), mapSubView, mapSubView.getTitleText(model), STEPS_START);
        model.setSubView(spiral);
        spiral.waitToBeOver();
        model.setSubView(mapSubView);
    }

}

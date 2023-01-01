package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

public abstract class TransitionView extends SubView implements Animation {
    private final String title;
    private boolean done;
    private SubView innerView;
    private final SubView toView;
    private boolean doingFrom;
    private int steps;
    protected static final Sprite blackBlock = new FilledBlockSprite(MyColors.BLACK);

    public TransitionView(SubView fromView, SubView toView, String title, int stepsStart) {
        this.title = title;
        this.done = false;
        this.innerView = fromView;
        this.toView = toView;
        this.doingFrom = true;
        this.steps = stepsStart;
        AnimationManager.registerPausable(this);
    }

    @Override
    public void drawYourself(Model model) {
        super.drawYourself(model);
        innerView.drawYourself(model);
        model.getScreenHandler().clearForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        drawAnimation(model, steps);
    }

    protected abstract void drawAnimation(Model model, int steps);

    @Override
    protected void drawArea(Model model) {

    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return title;
    }

    public void waitToBeOver() {
        while (!done) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (doingFrom) {
            this.steps--;
            if (this.steps == 0) {
                doingFrom = false;
                innerView = toView;
            }
        } else {
            this.steps++;
            done = stepsAreDone(steps);
        }
    }

    protected abstract boolean stepsAreDone(int steps);

    @Override
    public void synch() { }
}

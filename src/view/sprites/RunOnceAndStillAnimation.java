package view.sprites;

import model.Model;
import view.MyColors;

public class RunOnceAndStillAnimation extends RunOnceAnimationSprite {
    private final int maxStillTime;
    private int stillTime;
    private boolean isCancelled = false;

    public RunOnceAndStillAnimation(String name, String map, int col, int row, int width, int height, int frames, MyColors color, int maxStillTime) {
        super(name, map, col, row, width, height, frames, color);
        this.maxStillTime = maxStillTime;
        this.stillTime = 0;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        if (getCurrentFrame() == getMaximumFrames()) {
            setCurrentFrame(getMaximumFrames()-1);
            stillTime++;
        }
    }

    @Override
    public boolean isDone() {
        return stillTime >= maxStillTime || isCancelled;
    }

    public boolean firstPartDone() {
        return stillTime >= 1;
    }

    public void cancel() {
        this.isCancelled = true;
    }
}

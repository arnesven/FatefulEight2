package view;

import model.Model;

import java.awt.event.KeyEvent;

public abstract class GameView {

    private boolean changes = true;
    private boolean timeToTransition = false;
    private boolean isBlocking;

    public GameView(boolean doesPauseGame) {
        this.isBlocking = doesPauseGame;
    }

    public abstract void transitionedTo(Model model);

    public abstract void transitionedFrom(Model model);

    public final void update(Model model) {
        changes = false;
        internalUpdate(model);
    }

    protected abstract void internalUpdate(Model model);

    public abstract GameView getNextView(Model model);

    public abstract void handleKeyEvent(KeyEvent keyEvent, Model model);

    protected void madeChanges() {
        this.changes = true;
    }

    public boolean hasChanges() {
        return changes;
    }

    public boolean timeToTransition() {
        return timeToTransition;
    }

    protected void setTimeToTransition(boolean b) {
        timeToTransition = b;
        madeChanges();
    }

    public boolean pausesGame() {
        return isBlocking;
    }

    protected boolean isValid(Model model) {
        return true;
    }
}

package view;

import model.Model;

import java.awt.event.KeyEvent;

public class DummyView extends GameView {
    public DummyView() {
        super(false);
    }

    @Override
    public void transitionedTo(Model model) {
        throw new IllegalStateException("DummyView: Should never be called");
    }

    @Override
    public void transitionedFrom(Model model) {
        throw new IllegalStateException("DummyView: Should never be called");
    }

    @Override
    protected void internalUpdate(Model model) {
        throw new IllegalStateException("DummyView: Should never be called");
    }

    @Override
    public GameView getNextView(Model model) {
        throw new IllegalStateException("DummyView: Should never be called");
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        throw new IllegalStateException("DummyView: Should never be called");
    }
}

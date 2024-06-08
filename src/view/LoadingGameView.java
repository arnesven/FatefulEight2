package view;

import model.Model;

import java.awt.event.KeyEvent;

public class LoadingGameView extends GameView {
    public LoadingGameView() {
        super(false);
    }

    @Override
    public void transitionedTo(Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        BorderFrame.drawCentered(model.getScreenHandler(), "Loading...", 22,
                MyColors.WHITE, MyColors.BLACK);
    }

    @Override
    public GameView getNextView(Model model) {
        return new MainGameView();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {

    }
}

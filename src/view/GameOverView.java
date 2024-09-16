package view;

import model.Model;

import java.awt.event.KeyEvent;

public class GameOverView extends GameView {
    public GameOverView() {
        super(false);
    }

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearAll();
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void internalUpdate(Model model) {
        BorderFrame.drawCentered(model.getScreenHandler(), "GAME OVER", 22, MyColors.WHITE);
    }

    @Override
    public GameView getNextView(Model model) {
        return new IntroGameView();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (model.getLog().isAcceptingInput()) {
            model.getLog().keyTyped(keyEvent, model);
        }
        model.setGameAbandoned(true);
        model.setExitGame(true);
        setTimeToTransition(true);
    }
}

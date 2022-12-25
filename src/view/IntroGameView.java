package view;

import model.Model;

import java.awt.event.KeyEvent;

public class IntroGameView extends GameView {
    public IntroGameView() {
        super(true);
    }

    @Override
    public void transitionedTo(Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {
        model.getScreenHandler().clearAll();
    }

    @Override
    public void internalUpdate(Model model) {
        String message = "Fateful Eight - Intro Screen";
        BorderFrame.drawCentered(model.getScreenHandler(), message, 20, MyColors.WHITE);
        BorderFrame.drawCentered(model.getScreenHandler(), "- Press any key -", 22, MyColors.WHITE);
    }

    @Override
    public GameView getNextView(Model model) {
        return new StartGameMenu();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        super.setTimeToTransition(true);
    }
}

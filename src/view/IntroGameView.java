package view;

import model.Model;
import view.sprites.Sprite;

import java.awt.event.KeyEvent;

public class IntroGameView extends GameView {

    private static Sprite splashSprite = makeSprite();

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
        BorderFrame.drawCentered(model.getScreenHandler(), "- Press any key -", 40, MyColors.WHITE);
        model.getScreenHandler().put(0, 0, splashSprite);
    }

    @Override
    public GameView getNextView(Model model) {
        return new StartGameMenu();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        super.setTimeToTransition(true);
    }


    private static Sprite makeSprite() {
        Sprite sp = new Sprite("splash", "splash.png", 0, 0, 640, 400);
        MyColors.transformImage(sp);
        return sp;
    }
}

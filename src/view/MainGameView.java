package view;

import model.Model;
import view.help.HelpView;
import view.widget.CenterText;
import view.widget.MiniLog;
import view.widget.TitleText;
import view.widget.TopText;

import java.awt.event.KeyEvent;

public class MainGameView extends GameView {

    private TopText topText = new TopText();
    private MiniLog miniLog = new MiniLog();
    private GameView nextView;

    public MainGameView() {
        super(false);
    }

    @Override
    public void transitionedTo(Model model) {
        update(model);
    }

    @Override
    public void transitionedFrom(Model model) {
        setTimeToTransition(false);
    }

    public void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        if (model.isGameOver()) {
            setTimeToTransition(true);
            nextView = new GameOverView();
        } else {
            BorderFrame.drawFrame(model.getScreenHandler(), model.getSubView().getCenterTextHeight());
            ScreenHandler screenHandler = model.getScreenHandler();
            screenHandler.clearForeground();
            model.getParty().drawYourself(screenHandler);
            model.getSubView().drawYourself(model);
            topText.drawYourself(model);
            miniLog.drawYourself(model);
        }
    }

    @Override
    public GameView getNextView(Model model) {
        return nextView;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyChar() == '§') {
            setTimeToTransition(true);
            nextView = new LogView(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
            nextView = new MenuView(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_F1) {
            setTimeToTransition(true);
            nextView = new HelpView(this);
        } else if (model.getSubView().handleKeyEvent(keyEvent, model)) {
            //
        } else if (model.getLog().isAcceptingInput()) {
            model.getLog().keyTyped(keyEvent, model);
        }
    }
}

package view;

import control.FatefulEight;
import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import view.help.HelpView;
import view.widget.MiniLog;
import view.widget.TopText;

import java.awt.event.KeyEvent;

public class MainGameView extends GameView {

    private TopText topText = new TopText();
    private MiniLog miniLog = new MiniLog();
    private GameView nextView;
    private boolean allowLogChange = true;

    public MainGameView() {
        super(false);
    }

    protected void setAllowLogChange(boolean b) {
        allowLogChange = b;
    }

    @Override
    public void transitionedTo(Model model) {
        miniLog.setState(model.getParty().isDrawVertically());
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
            drawFrame(model);
            ScreenHandler screenHandler = model.getScreenHandler();
            screenHandler.clearForeground();
            drawParty(model);
            topText.drawYourself(model);
            drawSubView(model);
            miniLog.drawYourself(model);
        }
    }

    protected void drawFrame(Model model) {
        BorderFrame.drawFrame(model.getScreenHandler(), model.getSubView().getCenterTextHeight());
    }

    protected void drawParty(Model model) {
        model.getParty().drawYourself(model.getScreenHandler());
    }

    private void drawSubView(Model model) {
        model.getSubView().drawYourself(model);
    }

    @Override
    public GameView getNextView(Model model) {
        return nextView;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F2 && allowLogChange) {
            if (miniLog.isFinalStage()) {
                setTimeToTransition(true);
                nextView = new LogView(this);
            }
            miniLog.toggleSize();
            model.getParty().setDrawPartyVertically(miniLog.isOnRight());
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
            nextView = new MenuView(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_F1) {
            setTimeToTransition(true);
            nextView = new HelpView(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_INSERT) {
            setTimeToTransition(true);
            nextView = new PauseMenu(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_F7 && FatefulEight.inDebugMode()) {
            for (GameCharacter target : model.getParty().getPartyMembers()) {
                VampirismCondition vampCond = (VampirismCondition) target.getCondition(VampirismCondition.class);
                if (vampCond == null) {
                    target.addCondition(new VampirismCondition(VampirismCondition.INITIAL_STAGE, model.getDay()));
                } else {
                    //vampCond.progress(model, target);
                }
            }
        } else if (keyEvent.isControlDown()) {
            GameView next = MainGameViewHotKeyHandler.handle(this, keyEvent);
            if (next != null) {
                setTimeToTransition(true);
                nextView = next;
            }
        } else if (model.getSubView().handleKeyEvent(keyEvent, model)) {
            //
        } else if (model.getLog().isAcceptingInput()) {
            model.getLog().keyTyped(keyEvent, model);
        }
    }
}

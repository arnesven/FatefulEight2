package view;

import model.Model;
import model.map.World;
import view.widget.FullMapTopText;
import view.widget.TopText;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FullMapView extends GameView {

    private static final int MAP_WIDTH_HEXES = 20;
    private static final int MAP_HEIGHT_HEXES = 12;
    private static final int Y_OFFSET = 2;
    private Point cursorPos;
    private final GameView previousView;
    private TopText topText = new FullMapTopText();

    public FullMapView(GameView previous) {
        super(true);
        this.previousView = previous;
    }

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearAll();
        cursorPos = new Point(model.getParty().getPosition());
        update(model);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected boolean isValid(Model model) {
        return model.getParty().size() > 0;
    }

    @Override
    protected void internalUpdate(Model model) {
        ScreenHandler screenHandler = model.getScreenHandler();
        BorderFrame.drawFrameTop(screenHandler);
        BorderFrame.drawFrameHorizontalLine(screenHandler, 48);
        topText.drawYourself(model);
        screenHandler.clearForeground();
        model.getWorld().drawYourself(model, cursorPos, model.getParty().getPosition(),
                MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, null, true);
        screenHandler.clearSpace(0, 80,49, 50);
        BorderFrame.drawString(screenHandler, model.getHexInfo(cursorPos), 0, 49, MyColors.WHITE);
    }

    @Override
    public GameView getNextView(Model model) {
        return previousView;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (!handleMapMovement(keyEvent, model)) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                setTimeToTransition(true);
            }
        }
    }

    public boolean handleMapMovement(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            moveCursor(0, 1);
            madeChanges();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveCursor(1, 0);
            madeChanges();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            moveCursor(-1, 0);
            madeChanges();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            moveCursor(0, -1);
            madeChanges();
            return true;
        }

        // TODO: Remove this hack
        if (keyEvent.getKeyCode() == KeyEvent.VK_F4) {
            model.getParty().setPosition(cursorPos);
            model.transitionToDialog(new SimpleMessageView(model.getView(), "You have teleported to " + cursorPos.x + ", " + cursorPos.y + "!"));
        }

        return false;
    }

    private void moveCursor(int dx, int dy) {
        World.move(cursorPos, dx, dy);
    }
}

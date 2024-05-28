package view;

import control.FatefulEight;
import model.Model;
import model.map.CaveSystem;
import model.map.World;
import view.help.SpecificTerrainHelpDialog;
import view.widget.FullMapTopText;
import view.widget.TopText;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FullMapView extends GameView {

    private static final int MAP_WIDTH_HEXES = 20;
    private static final int MAP_HEIGHT_HEXES = 12;
    private static final int Y_OFFSET = 2;
    private Point cursorPos = null;
    private final GameView previousView;
    private TopText topText = new FullMapTopText();
    private World worldToDraw;

    public FullMapView(GameView previous, Point startCursorPos) {
        super(true);
        this.previousView = previous;
        if (startCursorPos != null) {
            cursorPos = new Point(startCursorPos.x, startCursorPos.y);
        }
    }

    public FullMapView(GameView previousView) {
        this(previousView, null);
    }

    @Override
    public void transitionedTo(Model model) {
        if (model.isInCaveSystem()) {
            worldToDraw = model.getCaveSystem();
        } else {
            worldToDraw = model.getWorld();
        }
        model.getScreenHandler().clearAll();
        ensureCursorPosSet(model);
        update(model);
    }

    private void ensureCursorPosSet(Model model) {
        if (cursorPos == null) {
            cursorPos = new Point(model.getParty().getPosition());
        }
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
        ensureCursorPosSet(model);
        ScreenHandler screenHandler = model.getScreenHandler();
        BorderFrame.drawFrameTop(screenHandler);
        BorderFrame.drawFrameHorizontalLine(screenHandler, 48);
        topText.drawYourself(model);
        screenHandler.clearForeground();
        worldToDraw.drawYourself(model, cursorPos, model.getParty().getPosition(),
                MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, null, true);
        screenHandler.clearSpace(0, 80,49, 50);
        if (worldToDraw == model.getWorld()) {
            BorderFrame.drawString(screenHandler, model.getHexInfo(cursorPos), 0, 49, MyColors.WHITE);
        } else {
            BorderFrame.drawString(screenHandler, "(" + cursorPos.x + "," + cursorPos.y + ") ",
                    0, 49, MyColors.WHITE);
        }
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
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_F3) {
                model.transitionToDialog(new SpecificTerrainHelpDialog(model.getView(),
                        worldToDraw.getHex(cursorPos), model.getMapObjects(cursorPos), true));
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_F4) {
                if (worldToDraw == model.getWorld()) {
                    worldToDraw = model.getCaveSystem();
                } else {
                    worldToDraw = model.getWorld();
                }
                madeChanges();
            }
        }
    }

    public boolean handleMapMovement(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            model.getWorld().move(cursorPos, 0, 1);
            madeChanges();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            model.getWorld().move(cursorPos,1, 0);
            madeChanges();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            model.getWorld().move(cursorPos,-1, 0);
            madeChanges();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            model.getWorld().move(cursorPos,0, -1);
            madeChanges();
            return true;
        }

        if (FatefulEight.inDebugMode()) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_F8) {
                model.getParty().setPosition(cursorPos);
                model.transitionToDialog(new SimpleMessageView(model.getView(), "You have teleported to " + cursorPos.x + ", " + cursorPos.y + "!"));
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_F9) {
                model.cycleWorldState();
                madeChanges();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_F10) {
                if (!model.isInCaveSystem()) {
                    model.transitionToDialog(new SimpleMessageView(model.getView(), "You have moved into the cave system."));
                    model.enterCaveSystem(false);
                } else {
                    model.transitionToDialog(new SimpleMessageView(model.getView(), "You have moved into the overworld."));
                    model.exitCaveSystem(false);
                }
            }
        }

        return false;
    }
}

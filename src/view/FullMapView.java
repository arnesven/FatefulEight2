package view;

import control.FatefulEight;
import model.Model;
import model.map.World;
import model.map.WorldBuilder;
import model.map.WorldType;
import model.map.objects.*;
import util.Arithmetics;
import view.help.SpecificTerrainHelpDialog;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.widget.FullMapTopText;
import view.widget.TopText;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FullMapView extends GameView {

    private static final int MAP_WIDTH_HEXES = 20;
    private static final int MAP_HEIGHT_HEXES = 12;
    private static final int Y_OFFSET = 2;
    private static final int MAX_VIEW_TYPES = 4;
    private Point cursorPos = null;
    private final GameView previousView;
    private TopText topText = new FullMapTopText();
    private World worldToDraw;
    private int viewType = 0;
    private MapFilter currentFilter = null;
    private int maxViews;

    public FullMapView(GameView previous, Point startCursorPos) {
        super(true);
        maxViews = MAX_VIEW_TYPES;
        if (FatefulEight.inDebugMode()) {
            maxViews++;
        }
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
        AnimationManager.synchAnimations();
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
                MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, null, true,
                currentFilter);
        if (currentFilter != null) {
            currentFilter.drawLegend(screenHandler, 0, 2);
        }
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
                cycleView(model);
                madeChanges();
            }
        }
    }

    private void cycleView(Model model) {
        viewType = Arithmetics.incrementWithWrap(viewType, maxViews);
        if (viewType == 1) {
            //currentFilter = new WaterPathDistancesFilter();
            currentFilter = new ShowLocationNamesFilter();
            worldToDraw = model.getWorld();
        } else if (viewType == 2) {
            currentFilter = new UnderworldLegendFilter();
            worldToDraw = model.getCaveSystem();
        } else if (viewType == 3) {
            currentFilter = new KingdomFilter();
            worldToDraw = model.getWorld();
        } else if (viewType == 4) {
            currentFilter = new DiscoveredTravelRoutesFilter();
            worldToDraw = model.getWorld();
        } else { // 0
            currentFilter = null;
            worldToDraw = model.getWorld();
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
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_F11) {
                if (model.isInOriginalWorld()) {
                    model.goBetweenWorlds(WorldType.thePast, new Point(5, 5));
                    model.transitionToDialog(new SimpleMessageView(model.getView(), "You have traveled to the past."));
                } else {
                    model.goBetweenWorlds(WorldType.original, WorldBuilder.CROSSROADS_INN_POSITION);
                    model.transitionToDialog(new SimpleMessageView(model.getView(), "You have traveled back to the future."));
                }
            }

        }

        return false;
    }
}

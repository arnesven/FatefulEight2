package view.subviews;

import model.Model;
import model.states.GameState;
import model.states.warehouse.SpecialWarehouseCrate;
import model.states.warehouse.Warehouse;
import model.states.warehouse.WarehouseCrate;
import model.states.warehouse.WarehouseObject;
import view.BorderFrame;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.CrateAndAvatarSprite;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static view.subviews.TavernSubView.FLOOR;

public class WarehouseSubView extends AvatarSubView {

    private static final Map<Integer, Point> DXDYS_FOR_KEYS = Map.of(
                KeyEvent.VK_LEFT,  new Point(-1, 0),
                KeyEvent.VK_RIGHT, new Point(1, 0),
                KeyEvent.VK_UP,    new Point(0, -1),
                KeyEvent.VK_DOWN,  new Point(0, 1));

    private final Map<MyColors, Map<Integer, CrateAndAvatarSprite>> combinedSprites;
    private final Warehouse warehouse;
    private final AvatarSprite avatar;

    private final List<KeyEvent> moveQueue = new ArrayList<>();
    private final int tries;
    private boolean avatarEnabled = true;
    private final Point avatarPos;
    private int moveCount = 0;
    private boolean telekinesisOn = false;

    public WarehouseSubView(Model model, Warehouse warehouse, int triesRemaining) {
        this.avatar = model.getParty().getLeader().getAvatarSprite();
        this.combinedSprites = makeCombinedSprites(avatar);
        this.warehouse = warehouse;
        this.avatarPos = warehouse.getPlayerStartingPosition();
        this.tries = triesRemaining;
    }

    @Override
    protected void specificDrawArea(Model model) {
        drawFloor(model);
        warehouse.drawObjects(model.getScreenHandler());
        if (avatarEnabled) {
            drawAvatar(model);
        }
        BorderFrame.drawString(model.getScreenHandler(), "Moves: " + moveCount,
                X_OFFSET, Y_OFFSET, MyColors.WHITE);
        BorderFrame.drawString(model.getScreenHandler(), "Tries Left: " + tries,
                X_MAX - 13, Y_OFFSET, MyColors.WHITE);

    }

    private void drawFloor(Model model) {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = convertToScreen(new Point(col, row));
                model.getScreenHandler().put(p.x, p.y, FLOOR);
            }
        }
    }

    private void drawAvatar(Model model) {
        if (model.getParty().getLeader() != null) { // If party has been wiped out and this is just before game over screen
            Point p = convertToScreen(avatarPos);
            model.getScreenHandler().register("warehouse", p, model.getParty().getLeader().getAvatarSprite(), 2);
        }
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (DXDYS_FOR_KEYS.containsKey(keyEvent.getKeyCode())) {
            addToMoveQueue(keyEvent);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            addToMoveQueue(keyEvent);
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    private synchronized void addToMoveQueue(KeyEvent keyEvent) {
        moveQueue.add(0, keyEvent);
    }

    public synchronized boolean hasMovesToHandle() {
        return !moveQueue.isEmpty();
    }

    @Override
    protected String getUnderText(Model model) {
        String extra = "";
        if (telekinesisOn) {
            extra = " (Telekinesis)";
        }
        return "Move with the arrow keys, SPACE to quit." + extra;
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - WAREHOUSE";
    }

    public boolean handleMove() {
        KeyEvent key = removeFromQueue();
        if (key.getKeyCode() == KeyEvent.VK_SPACE) {
            return false;
        }
        if (!avatarEnabled) {
            return true;
        }
        Point dxdy = DXDYS_FOR_KEYS.get(key.getKeyCode());
        if (telekinesisOn) {
            moveSpecialBox(dxdy);
        } else {
            moveAvatar(key, dxdy);
        }
        return true;
    }

    private void moveSpecialBox(Point dxdy) {
        Point boxPosition = warehouse.getSpecialBoxPosition();
        Point newPosition = new Point(boxPosition.x + dxdy.x, boxPosition.y + dxdy.y);
        if (warehouse.isFree(newPosition) && !newPosition.equals(avatarPos)) {
            WarehouseObject wobj = warehouse.removeObject(boxPosition);
            addMovementAnimation(wobj.getSprite(),
                    convertToScreen(boxPosition), convertToScreen(newPosition));
            waitForAnimation();
            warehouse.addObject(newPosition, wobj);
            removeMovementAnimation();
        }
    }

    private void moveAvatar(KeyEvent key, Point dxdy) {
        Point newPosition = new Point(avatarPos.x + dxdy.x, avatarPos.y + dxdy.y);
        if (warehouse.canMoveInto(newPosition, dxdy)) {
            moveCount++;
            avatarEnabled = false;
            if (warehouse.canMoveBox(newPosition, dxdy)) {
                int xExtra = (dxdy.x - 1) / 2;
                int yExtra = (dxdy.y + 1) / 2;
                Point fromPosition = new Point(avatarPos.x + xExtra, avatarPos.y + yExtra);
                Point toPosition = new Point(newPosition.x + xExtra, newPosition.y + yExtra);
                WarehouseObject wobj = warehouse.removeObject(newPosition);
                addMovementAnimation(combinedSprites.get(wobj.getColor()).get(key.getKeyCode()),
                        convertToScreen(fromPosition), convertToScreen(toPosition));
                waitForAnimation();
                Point objectPosition = new Point(newPosition.x + dxdy.x,
                        newPosition.y + dxdy.y);
                warehouse.addObject(objectPosition, wobj);
            } else {
                addMovementAnimation(avatar,
                        convertToScreen(avatarPos), convertToScreen(newPosition));
                waitForAnimation();
            }
            removeMovementAnimation();
            avatarPos.x = newPosition.x;
            avatarPos.y = newPosition.y;
            avatarEnabled = true;
        }
    }

    private synchronized KeyEvent removeFromQueue() {
        return moveQueue.remove(moveQueue.size() - 1);
    }

    private Map<MyColors, Map<Integer, CrateAndAvatarSprite>> makeCombinedSprites(AvatarSprite avatarSprite) {
        return Map.of(
                WarehouseCrate.COLOR,
                Map.of(
                        KeyEvent.VK_LEFT,  WarehouseCrate.makeCombinedSprite(avatarSprite, 0, 0, 64, 32),
                        KeyEvent.VK_RIGHT, WarehouseCrate.makeCombinedSprite(avatarSprite,0, 1, 64, 32),
                        KeyEvent.VK_UP,    WarehouseCrate.makeCombinedSprite(avatarSprite.getAvatarBack(), 0, 0, 32, 64),
                        KeyEvent.VK_DOWN,  WarehouseCrate.makeCombinedSprite(avatarSprite,1, 0, 32, 64)),
                SpecialWarehouseCrate.COLOR,
                Map.of(
                        KeyEvent.VK_LEFT,  SpecialWarehouseCrate.makeCombinedSprite(avatarSprite, 0, 0, 64, 32),
                        KeyEvent.VK_RIGHT, SpecialWarehouseCrate.makeCombinedSprite(avatarSprite,0, 1, 64, 32),
                        KeyEvent.VK_UP,    SpecialWarehouseCrate.makeCombinedSprite(avatarSprite.getAvatarBack(), 0, 0, 32, 64),
                        KeyEvent.VK_DOWN,  SpecialWarehouseCrate.makeCombinedSprite(avatarSprite,1, 0, 32, 64))
                );
    }

    public boolean gameWon() {
        return warehouse.gameIsWon();
    }

    public boolean checkForRemove() {
        return warehouse.checkForOtherBoxRemove();
    }

    public void setTelekinesisEnabled(boolean on) {
        this.telekinesisOn = on;
    }

    public boolean isTelekinesisActivated() {
        return telekinesisOn;
    }
}

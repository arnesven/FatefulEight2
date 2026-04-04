package view.subviews;

import model.Model;
import model.states.warehouse.WarehouseObject;
import view.BorderFrame;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FreeMoveAvatarSubView extends AvatarSubView {

    private static final Map<Integer, Point> DXDYS_FOR_KEYS = Map.of(
            KeyEvent.VK_LEFT,  new Point(-1, 0),
            KeyEvent.VK_RIGHT, new Point(1, 0),
            KeyEvent.VK_UP,    new Point(0, -1),
            KeyEvent.VK_DOWN,  new Point(0, 1));

    private final List<KeyEvent> moveQueue = new ArrayList<>();
    private boolean avatarEnabled = true;
    private final Point avatarPos;

    public FreeMoveAvatarSubView(Point playerStartingPosition) {
        avatarPos = playerStartingPosition;
    }

    @Override
    protected void specificDrawArea(Model model) {
        drawBackground(model);
        if (isAvatarEnabled()) {
            drawAvatar(model);
        }
        drawOverlay(model);
    }

    protected abstract void drawOverlay(Model model);

    protected abstract void drawBackground(Model model);

    protected abstract Point moveAvatar(KeyEvent key, Point avatarPosition, Point dxdy);

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

    private synchronized KeyEvent removeFromQueue() {
        return moveQueue.remove(moveQueue.size() - 1);
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

        Point newPosition = moveAvatar(key, avatarPos, dxdy);
        avatarPos.x = newPosition.x;
        avatarPos.y = newPosition.y;
        return true;
    }

    protected boolean isAvatarEnabled() {
        return avatarEnabled;
    }

    protected void setAvatarEnabled(boolean b) {
        avatarEnabled = b;
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    private void drawAvatar(Model model) {
        if (model.getParty().getLeader() != null) { // If party has been wiped out and this is just before game over screen
            Point p = convertToScreen(avatarPos);
            model.getScreenHandler().register("warehouse", p, model.getParty().getLeader().getAvatarSprite(), 2);
        }
    }
}

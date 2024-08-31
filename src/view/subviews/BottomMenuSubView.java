package view.subviews;

import java.awt.event.KeyEvent;

public abstract class BottomMenuSubView extends BorderMenuSubView {
    public BottomMenuSubView(int centerRows, int[] cursorPositions) {
        super(centerRows, Y_MAX-1, cursorPositions);
    }

    @Override
    protected boolean isMoveFromBorderMenu(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_UP;
    }

    @Override
    protected boolean isMoveToBorderMenu(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_DOWN && isOnBorderToBottomMenu();
    }

    protected abstract boolean isOnBorderToBottomMenu();
}

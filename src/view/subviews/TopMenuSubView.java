package view.subviews;

import java.awt.event.KeyEvent;

public abstract class TopMenuSubView extends BorderMenuSubView {

    public TopMenuSubView(int centerRows, int[] cursorPositions) {
        super(centerRows, Y_OFFSET, cursorPositions);
    }

    protected abstract boolean cursorOnBorderToTop();

    public int getTopIndex() {
        return getBorderIndex();
    }

    public void setTopCursorIndex(int index) {
        setBorderCursorIndex(index);
    }

    @Override
    protected boolean isMoveToBorderMenu(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_UP && cursorOnBorderToTop();
    }

    @Override
    protected boolean isMoveFromBorderMenu(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_DOWN;
    }
}

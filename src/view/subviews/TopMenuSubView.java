package view.subviews;

import model.Model;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;

import java.awt.event.KeyEvent;

public abstract class TopMenuSubView extends SubView {

    private final int[] cursorPositions;
    private boolean isInTopRow = false;
    private int topCursorIndex;

    public TopMenuSubView(int centerRows, int[] cursorPositions) {
        super(centerRows);
        topCursorIndex = 0;
        this.cursorPositions = cursorPositions;
    }

    protected void setTopCursorIndex(int topCursorIndex) {
        this.topCursorIndex = topCursorIndex;
    }

    public int getTopIndex() {
        if (!isInTopRow) {
            return -1;
        }
        return topCursorIndex;
    }

    @Override
    protected final void drawArea(Model model) {
        drawInnerArea(model);
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+1, blackBlock);
        for (int i = 0; i < cursorPositions.length; ++i) {
            BorderFrame.drawString(model.getScreenHandler(), getTitle(i),  cursorPositions[i]+1, 4, getTitleColor(model, i));
        }
        if (!isInTopRow) {
            drawCursor(model);
            model.getScreenHandler().put(cursorPositions[topCursorIndex], 4, ArrowSprites.RIGHT_BLACK);
        } else {
            model.getScreenHandler().put(cursorPositions[topCursorIndex], 4, ArrowSprites.RIGHT_BLACK_BLINK);
        }
    }

    protected abstract void drawCursor(Model model);

    protected abstract void drawInnerArea(Model model);

    protected abstract MyColors getTitleColor(Model model, int i);

    protected abstract String getTitle(int i);

    @Override
    public final boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (isInTopRow) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                isInTopRow = false;
                topCursorIndex = getDefaultIndex();
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                topCursorIndex = (topCursorIndex + 1) % 3;
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                topCursorIndex--;
                if (topCursorIndex < 0) {
                    topCursorIndex = 2;
                }
                return true;
            }
            return false;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP && cursorOnBorderToTop()) {
            isInTopRow = true;
            return true;
        }
        return innerHandleKeyEvent(keyEvent, model);
    }

    protected abstract boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model);

    protected abstract int getDefaultIndex();

    protected abstract boolean cursorOnBorderToTop();
}

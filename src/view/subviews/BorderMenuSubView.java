package view.subviews;

import model.Model;
import util.Arithmetics;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;

import java.awt.event.KeyEvent;

public abstract class BorderMenuSubView extends SubView {
    private final int[] cursorPositions;
    private final int menuYPosition;
    private boolean inBorderMenu = false;
    private int borderCursorIndex;

    public BorderMenuSubView(int centerRows, int menuYPosition, int[] cursorPositions) {
        super(centerRows);
        borderCursorIndex = 0;
        this.menuYPosition = menuYPosition;
        this.cursorPositions = cursorPositions;
        setBorderCursorIndex(getDefaultIndex());
    }

    protected void setInBorderMenu(boolean b) {
        this.inBorderMenu = b;
    }

    protected void setBorderCursorIndex(int cursorIndex) {
        this.borderCursorIndex = cursorIndex;
    }

    public int getBorderIndex() {
        if (!inBorderMenu) {
            return -1;
        }
        return borderCursorIndex;
    }

    @Override
    protected final void drawArea(Model model) {
        drawInnerArea(model);
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, menuYPosition, menuYPosition+1, blackBlock);
        for (int i = 0; i < cursorPositions.length; ++i) {
            BorderFrame.drawString(model.getScreenHandler(), getTitle(i),  cursorPositions[i]+1, menuYPosition, getTitleColor(model, i));
        }
        if (!inBorderMenu) {
            drawCursor(model);
            if (showArrowWhileNotInBorderMenu()) {
                model.getScreenHandler().put(cursorPositions[borderCursorIndex], menuYPosition, ArrowSprites.RIGHT_BLACK);
            }
        } else {
            model.getScreenHandler().put(cursorPositions[borderCursorIndex], menuYPosition, ArrowSprites.RIGHT_BLACK_BLINK);
        }
    }

    protected boolean showArrowWhileNotInBorderMenu() {
        return true;
    }

    protected abstract void drawCursor(Model model);

    protected abstract void drawInnerArea(Model model);

    protected abstract MyColors getTitleColor(Model model, int i);

    protected abstract String getTitle(int i);

    @Override
    public final boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (inBorderMenu) {
            if (isMoveFromBorderMenu(keyEvent)) {
                inBorderMenu = false;
                borderCursorIndex = getDefaultIndex();
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                borderCursorIndex = Arithmetics.incrementWithWrap(borderCursorIndex, cursorPositions.length);
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                borderCursorIndex = Arithmetics.decrementWithWrap(borderCursorIndex, cursorPositions.length);
                return true;
            }
            return false;
        }
        if (isMoveToBorderMenu(keyEvent)) {
            inBorderMenu = true;
            return true;
        }
        return innerHandleKeyEvent(keyEvent, model);
    }

    protected abstract boolean isMoveFromBorderMenu(KeyEvent keyEvent);

    protected abstract boolean isMoveToBorderMenu(KeyEvent keyEvent);

    protected abstract boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model);

    protected abstract int getDefaultIndex();
}

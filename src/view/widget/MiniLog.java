package view.widget;

import model.Model;
import model.characters.GameCharacter;
import util.Arithmetics;
import view.*;
import view.sprites.CharSprite;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import static view.BorderFrame.CHARACTER_WINDOW_COLUMNS;
import static view.DrawingArea.WINDOW_COLUMNS;
import static view.DrawingArea.WINDOW_ROWS;
import static view.sprites.BorderSpriteConstants.HORIZONTAL_UP;
import static view.sprites.BorderSpriteConstants.LOWER_RIGHT_CORNER;

public class MiniLog {

    private static final int SMALL = 0;
    private static final int LARGE_BOTTOM = 1;
    private static final int LARGE_RIGHT = 2;
    private static final int NO_OF_STAGES = 3;

    private static final Sprite FILLED_BLACK = new FilledBlockSprite(MyColors.BLACK);
    private int currentSize = 0;

    public void drawYourself(Model model) {
        if (model.getLog() != null) {
            model.getScreenHandler().clearSpace(getColOffset(), DrawingArea.WINDOW_COLUMNS,
                    getYStart(), DrawingArea.WINDOW_ROWS);
            model.getScreenHandler().fillSpace(getColOffset(), DrawingArea.WINDOW_COLUMNS,
                    getYStart(), DrawingArea.WINDOW_ROWS, FILLED_BLACK);
            model.getScreenHandler().clearForeground(getColOffset()+1, DrawingArea.WINDOW_COLUMNS,
                    getYStart(), DrawingArea.WINDOW_ROWS);
            if (currentSize == LARGE_RIGHT) {
                LogView.drawLogHalf(model, getTotalRows(), getYStart(), getColOffset(), 0);
                BorderFrame.fixLogRightCorners(model.getScreenHandler());
            } else {
                LogView.drawLog(model, getTotalRows(), getYStart(), 0);
                if (currentSize == LARGE_BOTTOM) {
                    BorderFrame.fixLogHalfBottomCorners(model.getScreenHandler());
                }
            }
        }
    }

    private int getColOffset() {
        if (currentSize == LARGE_RIGHT) {
            return WINDOW_COLUMNS-CHARACTER_WINDOW_COLUMNS;
        }
        return 0;
    }

    private int getYStart() {
        if (currentSize == LARGE_BOTTOM) {
            return 3*(BorderFrame.CHARACTER_WINDOW_ROWS+1) + 2;
        }
        if (currentSize == LARGE_RIGHT) {
            return 2;
        }
        return 4*(BorderFrame.CHARACTER_WINDOW_ROWS+1) + 2;
    }

    private int getTotalRows() {
        return DrawingArea.WINDOW_ROWS - getYStart();
    }

    public void toggleSize() {
        currentSize = Arithmetics.incrementWithWrap(currentSize, NO_OF_STAGES);
    }

    public boolean isFinalStage() {
        return currentSize == NO_OF_STAGES - 1;
    }

    public boolean isOnRight() {
        return currentSize == LARGE_RIGHT;
    }

    public void setState(boolean drawVertically) {
        if (drawVertically) {
            currentSize = LARGE_RIGHT;
        }
    }
}

package view.widget;

import model.Model;
import view.BorderFrame;
import view.DrawingArea;
import view.LogView;
import view.MyColors;
import view.sprites.FilledBlockSprite;

public class MiniLog {
    private boolean large = false;

    public void drawYourself(Model model) {
        if (model.getLog() != null) {
            model.getScreenHandler().clearSpace(0, DrawingArea.WINDOW_COLUMNS,
                    getYStart(), DrawingArea.WINDOW_ROWS);
            model.getScreenHandler().fillSpace(0, DrawingArea.WINDOW_COLUMNS,
                    getYStart(), DrawingArea.WINDOW_ROWS, new FilledBlockSprite(MyColors.BLACK));
            model.getScreenHandler().clearForeground(0, DrawingArea.WINDOW_COLUMNS,
                    getYStart(), DrawingArea.WINDOW_ROWS);
            LogView.drawLog(model, getTotalRows(), getYStart(), 0);
        }
    }

    private int getYStart() {
        if (large) {
            return 3*(BorderFrame.CHARACTER_WINDOW_ROWS+1) + 2;
        }
        return 4*(BorderFrame.CHARACTER_WINDOW_ROWS+1) + 2;
    }

    private int getTotalRows() {
        return DrawingArea.WINDOW_ROWS - getYStart();
    }

    public void toggleSize() {
        large = !large;
    }

    public boolean isLarge() {
        return large;
    }
}

package view.widget;

import model.Model;
import view.BorderFrame;
import view.DrawingArea;
import view.LogView;

public class MiniLog {
    private static final int Y_START = 4*(BorderFrame.CHARACTER_WINDOW_ROWS+1) + 2;

    public void drawYourself(Model model) {
        if (model.getLog() != null) {
            model.getScreenHandler().clearSpace(0, DrawingArea.WINDOW_COLUMNS,
                    Y_START, DrawingArea.WINDOW_ROWS);
            LogView.drawLog(model, DrawingArea.WINDOW_ROWS - Y_START, Y_START);
        }
    }
}

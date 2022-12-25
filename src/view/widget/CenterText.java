package view.widget;

import model.Model;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;

public class CenterText {

    private static final int AREA_WIDTH =  80 - 2*(BorderFrame.CHARACTER_WINDOW_COLUMNS) - 1;
    private final int centerTextRows;

    //private String upper;
    //private String lower;

    private String[] parts;

    public CenterText(int centerTextRows) {
        this.centerTextRows = centerTextRows;
    }

    public void drawYourself(Model model) {
        ScreenHandler screenHandler = model.getScreenHandler();
        screenHandler.clearSpace(BorderFrame.CHARACTER_WINDOW_COLUMNS+1,
                80-BorderFrame.CHARACTER_WINDOW_COLUMNS-1,
                BorderFrame.CENTER_TEXT_BOTTOM - centerTextRows,
                BorderFrame.CENTER_TEXT_BOTTOM);

        for (int i = 0; i < parts.length; ++i) {
            BorderFrame.drawString(screenHandler, parts[i],
                    BorderFrame.CHARACTER_WINDOW_COLUMNS + 1,
                    BorderFrame.CENTER_TEXT_BOTTOM - centerTextRows + i, MyColors.WHITE);
        }
    }

    public void setContents(String contents) {
        parts = MyStrings.partition(contents, AREA_WIDTH);

    }

    public int getRows() {
        return centerTextRows;
    }
}

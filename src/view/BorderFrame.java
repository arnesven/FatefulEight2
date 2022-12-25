package view;

import model.Model;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import static view.DrawingArea.WINDOW_COLUMNS;
import static view.DrawingArea.WINDOW_ROWS;
import static view.sprites.BorderSpriteConstants.*;

public class BorderFrame {
    public static final int CHARACTER_WINDOW_COLUMNS = 23;
    public static final int CHARACTER_WINDOW_ROWS = 10;
    public static final int CENTER_TEXT_BOTTOM = 45;
    public static final int TITLE_TEXT_HEIGHT = 2;

    public static void drawFrameTop(ScreenHandler screenHandler) {
        drawFrameHorizontalLine(screenHandler, 1);
    }

    public static void drawFrameBottom(ScreenHandler screenHandler) {
        drawFrameHorizontalLine(screenHandler, 45);
    }

    public static void drawFrame(ScreenHandler screenHandler, int centerTextHeight) {
        drawFrameTop(screenHandler);
        drawFrameBottom(screenHandler);
        for (int i = 0; i < WINDOW_COLUMNS; ++i) {
            screenHandler.put(i,1, CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
            screenHandler.put(i,45, CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        }

        for (int i=0; i < CHARACTER_WINDOW_COLUMNS; ++i) {
            for (int y=1; y < 4; ++y ) {
                screenHandler.put(i, 1+y*(CHARACTER_WINDOW_ROWS+1), CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
                screenHandler.put(79-i, 1+y*(CHARACTER_WINDOW_ROWS+1), CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
            }
        }

        for (int r=1; r < WINDOW_ROWS-4; ++r) {
            screenHandler.put(CHARACTER_WINDOW_COLUMNS, r, CharSprite.make(VERTICAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
            screenHandler.put(80-CHARACTER_WINDOW_COLUMNS-1, r, CharSprite.make(VERTICAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        }

        for (int y=1; y < 4; ++y ) {
            screenHandler.put(CHARACTER_WINDOW_COLUMNS, 1+y*(CHARACTER_WINDOW_ROWS+1), CharSprite.make(VERTICAL_LEFT, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
            screenHandler.put(WINDOW_COLUMNS-CHARACTER_WINDOW_COLUMNS-1, 1+y*(CHARACTER_WINDOW_ROWS+1), CharSprite.make(VERTICAL_RIGHT, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        }

        screenHandler.put(CHARACTER_WINDOW_COLUMNS, 1, CharSprite.make(HORIZONTAL_DOWN, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        screenHandler.put(80-CHARACTER_WINDOW_COLUMNS-1, 1, CharSprite.make(HORIZONTAL_DOWN, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        screenHandler.put(CHARACTER_WINDOW_COLUMNS, WINDOW_ROWS-5, CharSprite.make(HORIZONTAL_UP, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        screenHandler.put(80-CHARACTER_WINDOW_COLUMNS-1, WINDOW_ROWS-5, CharSprite.make(HORIZONTAL_UP, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));

        for (int x = CHARACTER_WINDOW_COLUMNS+1; x < 80-CHARACTER_WINDOW_COLUMNS-1; ++x) {
            screenHandler.put(x, TITLE_TEXT_HEIGHT+1, CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
            screenHandler.put(x, CENTER_TEXT_BOTTOM-centerTextHeight-1, CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        }

        screenHandler.put(CHARACTER_WINDOW_COLUMNS, TITLE_TEXT_HEIGHT+1, CharSprite.make(VERTICAL_RIGHT, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        screenHandler.put(80-CHARACTER_WINDOW_COLUMNS-1, TITLE_TEXT_HEIGHT+1, CharSprite.make(VERTICAL_LEFT, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        screenHandler.put(CHARACTER_WINDOW_COLUMNS, CENTER_TEXT_BOTTOM-centerTextHeight-1, CharSprite.make(VERTICAL_RIGHT, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        screenHandler.put(80-CHARACTER_WINDOW_COLUMNS-1, CENTER_TEXT_BOTTOM-centerTextHeight-1, CharSprite.make(VERTICAL_LEFT, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));

    }

    public static void drawString(ScreenHandler screenHandler, String s, int col, int row, MyColors color, MyColors bgColor) {
        for (int i = 0; i < s.length(); ++i) {
            Sprite charSprite = CharSprite.make(s.charAt(i), color, MyColors.CYAN, bgColor);
            screenHandler.put(col+i, row, charSprite);
        }
    }

    public static void drawString(ScreenHandler screenHandler, String s, int col, int row, MyColors color) {
        drawString(screenHandler, s, col, row, color, MyColors.BLACK);
    }

    public static void DrawCharSet(ScreenHandler screenHandler) {
        for (int i = '!'; i < 128; ++i) {
            char theChar = (char)i;
            screenHandler.put(i % 16, i / 16, CharSprite.make(theChar, MyColors.GREEN));
        }
    }

    public static void drawCentered(ScreenHandler screenHandler, String message, int row, MyColors color, MyColors bg) {
        BorderFrame.drawString(screenHandler, message,
                (DrawingArea.WINDOW_COLUMNS - message.length()) / 2,
                row, color, bg);
    }

    public static void drawCentered(ScreenHandler screenHandler, String message, int row, MyColors color) {
        drawCentered(screenHandler, message, row, color, MyColors.BLACK);
    }

    public static void drawFrameTop(Model model) {

    }

    public static void drawFrame(ScreenHandler screenHandler, int xStart, int yStart, int width, int height,
                                 MyColors bgColor, MyColors borderColor, MyColors fgColor, boolean fill) {
        screenHandler.put(xStart, yStart, CharSprite.make(UPPER_LEFT_CORNER, bgColor, borderColor, fgColor));
        screenHandler.put(xStart + width, yStart, CharSprite.make(UPPER_RIGHT_CORNER, bgColor, borderColor, fgColor));
        screenHandler.put(xStart, yStart + height, CharSprite.make(LOWER_LEFT_CORNER, bgColor, borderColor, fgColor));
        screenHandler.put(xStart + width, yStart + height, CharSprite.make(LOWER_RIGHT_CORNER, bgColor, borderColor, fgColor));

        for (int x = xStart+1; x < xStart + width; ++x) {
            screenHandler.put(x, yStart, CharSprite.make(UPPER_HORIZONTAL, bgColor, borderColor, fgColor));
            screenHandler.put(x, yStart + height, CharSprite.make(HORIZONTAL, bgColor, borderColor, fgColor));
        }
        for (int y = yStart+1; y < yStart + height; ++y) {
            screenHandler.put(xStart, y, CharSprite.make(VERTICAL_EAST, bgColor, borderColor, fgColor));
            screenHandler.put(xStart + width, y, CharSprite.make(VERTICAL, bgColor, borderColor, fgColor));
        }
        if (fill) {
            for (int y = yStart + 1; y < yStart + height; ++y) {
                for (int x = xStart + 1; x < xStart + width; ++x) {
                    screenHandler.put(x, y, CharSprite.make((char) 0xFF, fgColor));
                }
            }
        }
    }

    public static void drawMenuFrame(ScreenHandler screenHandler) {
        drawFrame(screenHandler, MenuView.X_START, MenuView.Y_START,
                MenuView.MENU_WIDTH, MenuView.MENU_HEIGHT,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
    }

    public static void drawFrameHorizontalLine(ScreenHandler screenHandler, int row) {
        for (int i = 0; i < WINDOW_COLUMNS; ++i) {
            screenHandler.put(i, row, CharSprite.make(HORIZONTAL, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK));
        }
    }
}

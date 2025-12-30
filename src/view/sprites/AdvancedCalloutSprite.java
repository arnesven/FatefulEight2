package view.sprites;

import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;

import java.awt.*;


public class AdvancedCalloutSprite extends CalloutSprite {
    private static final int MAX_CALLOUT_WIDTH = 23;
    private static final int MAX_ROWS = 5;
    private static final int OVERFLOW_SHIFT = 5;
    private final boolean shiftRight;
    private String[] textRows;
    private String[] overflowRows = null;

    private static final Sprite[][] calloutBorders = makeBorderSprites();
    private int maxWidth;
    private int overflowMaxWidth;

    public AdvancedCalloutSprite(String text) {
        this(text, MAX_CALLOUT_WIDTH, MAX_ROWS, true);
    }

    public AdvancedCalloutSprite(String text, int maxWidth, int maxRows, boolean shiftRight) {
        super(0, text.length()*4);
        this.shiftRight = shiftRight;
        if (text.endsWith("#") || text.endsWith("3")) {
            text = text.substring(0, text.length()-1);
        } else if (text.endsWith("^")) {
            text = "Level Up!";
        }
        this.textRows = partitionAndTrim(text, maxWidth);

        if (textRows.length > maxRows) {
            StringBuilder extraContent = new StringBuilder();
            for (int i = maxRows; i < textRows.length; ++i) {
                extraContent.append(textRows[i] + " ");
            }
            this.overflowRows = partitionAndTrim(extraContent.toString(), maxWidth - OVERFLOW_SHIFT);
            this.overflowMaxWidth = fixTextRows(overflowRows);

            String[] oldTextRows = textRows;
            textRows = new String[maxRows];
            for (int i = 0; i < textRows.length; ++i) {
                textRows[i] = oldTextRows[i];
            }
        }
        this.maxWidth = fixTextRows(textRows);
    }

    public void drawYourself(ScreenHandler screenHandler, Point location) {
        int shift = shiftRight ? 2 : ((maxWidth - 1) / 2);
        int startX = location.x - shift;
        int startY = location.y - textRows.length + 1;
        int overflowY = startY + textRows.length + 1;
        if (overflowRows != null) {
            drawBorder(screenHandler, startX - 1 + OVERFLOW_SHIFT, overflowY-1, 0, 0);
            for (int x = 0; x < overflowMaxWidth; ++x) {
                drawBorder(screenHandler,startX + x + OVERFLOW_SHIFT, overflowY-1, 1, 0);
            }
            drawBorder(screenHandler,startX + overflowMaxWidth + OVERFLOW_SHIFT, overflowY-1, 3, 0);
        }

        // TOP ROW
        drawBorder(screenHandler, startX - 1, startY, 0, 0);
        for (int x = 0; x < maxWidth; ++x) {
            drawBorder(screenHandler,startX + x, startY, 1, 0);
        }
        drawBorder(screenHandler,startX + maxWidth, startY, 3, 0);

        // MID ROWS
        for (int y = 0; y < textRows.length; ++y) {
            drawBorder(screenHandler,startX - 1, startY + y + 1, 0, 1);
            BorderFrame.drawStringInForeground(screenHandler, textRows[y], startX, startY + y + 1,
                    MyColors.BLACK, MyColors.WHITE);
            drawBorder(screenHandler,startX + textRows[y].length(), startY + y + 1, 3, 1);
        }

        // DRAW BOTTOM ROW
        drawBorder(screenHandler,startX - 1, startY + textRows.length + 1, 0, 2);
        for (int x = 0; x < maxWidth; ++x) {
            drawArrow(screenHandler, startX + x, startY, location.x);
            drawBorder(screenHandler,startX + x, startY + textRows.length + 1, 1, 1);
        }
        drawBorder(screenHandler,startX + maxWidth, startY + textRows.length + 1, 3, 2);

        if (overflowRows != null) {
            for (int y = 0; y < overflowRows.length; ++y) {
                drawBorder(screenHandler,startX - 1 + OVERFLOW_SHIFT, overflowY + y, 0, 1);
                BorderFrame.drawStringInForeground(screenHandler, overflowRows[y], startX + OVERFLOW_SHIFT, overflowY + y,
                        MyColors.BLACK, MyColors.WHITE);
                drawBorder(screenHandler,OVERFLOW_SHIFT + startX + overflowRows[y].length(), overflowY + y, 3, 1);
            }

            drawBorder(screenHandler,startX - 1 + OVERFLOW_SHIFT, overflowY + overflowRows.length, 0, 2);
            for (int x = 0; x < overflowMaxWidth; ++x) {
                drawBorder(screenHandler,startX + x + OVERFLOW_SHIFT, overflowY + overflowRows.length, 1, 1);
            }
            drawBorder(screenHandler,startX + overflowMaxWidth + OVERFLOW_SHIFT, overflowY + overflowRows.length, 3, 2);
        }
    }

    private void drawArrow(ScreenHandler screenHandler, int currentX, int startY, int locationX) {
        if (overflowRows == null) {
            if (currentX == locationX + 2) {
                drawBorder(screenHandler, currentX, startY + textRows.length + 2, 1, 2);
            } else if (currentX == locationX + 3) {
                drawBorder(screenHandler, currentX, startY + textRows.length + 2, 2, 2);
            }
        } else {
            if (currentX == locationX - 2) {
                drawBorder(screenHandler, currentX, startY + textRows.length + 2, 1, 3);
            } else if (currentX == locationX - 1) {
                drawBorder(screenHandler, currentX, startY + textRows.length + 2, 2, 3);
            }
        }
    }

    private void drawBorder(ScreenHandler screenHandler, int x, int y, int col, int row) {
        screenHandler.register("", new Point(x, y), calloutBorders[col][row], 3);
    }

    public static Sprite[][] makeBorderSprites() {
        Sprite[][] result = new Sprite[4][4];
        for (int y = 0; y < 3; ++y) {
            result[0][y] = new Sprite8x8("calloutborder" + y, "callouts.png", 0x10 * y);
            result[3][y] = new Sprite8x8("calloutborder" + y, "callouts.png", 0x10 * y + 0x03);
        }
        result[1][0] =  new Sprite8x8("calloutbordertopleft", "callouts.png", 0x09);
        result[2][0] =  new Sprite8x8("calloutbordertopright", "callouts.png", 0x0A);
        result[1][1] =  new Sprite8x8("calloutborderbottomleft", "callouts.png", 0x29);
        result[2][1] =  new Sprite8x8("calloutborderbottomright", "callouts.png", 0x2A);
        result[1][2] =  new Sprite8x8("calloutborderarrowleft", "callouts.png", 0x39);
        result[2][2] =  new Sprite8x8("calloutborderarrowright", "callouts.png", 0x3A);

        result[1][3] = new Sprite8x8("calloutborderarrowflright", "callouts.png", 0x3A);
        result[1][3].setFlipHorizontal(true);
        result[2][3] = new Sprite8x8("calloutborderarrowflleft", "callouts.png", 0x39);
        result[2][3].setFlipHorizontal(true);
        return result;
    }

    private static String[] partitionAndTrim(String text, int maxWidth) {
        String[] rows = MyStrings.partition(text, maxWidth);
        for (int i = 0; i < rows.length; ++i) {
            rows[i] = rows[i].trim();
        }
        return rows;
    }

    private static int fixTextRows(String[] textRows) {
        int maxWidth = 0;
        for (String s : textRows) {
            if (s.length() > maxWidth) {
                maxWidth = s.length();
            }
        }
        maxWidth = Math.max(maxWidth, 6);
        for (int i = 0; i < textRows.length; ++i) {
            textRows[i] = MyStrings.padRight(textRows[i], ' ', maxWidth);
        }
        return maxWidth;
    }
}

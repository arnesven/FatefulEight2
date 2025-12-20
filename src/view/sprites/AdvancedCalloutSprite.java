package view.sprites;

import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;

import java.awt.*;


public class AdvancedCalloutSprite extends CalloutSprite {
    private static final int MAX_CALLOUT_WIDTH = 22;
    private static final int MAX_ROWS = 5;
    private final boolean shiftRight;
    private String[] textRows;

    private static final Sprite[][] calloutBorders = makeBorderSprites();
    private int maxWidth;

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
        this.textRows = MyStrings.partition(text, maxWidth);
        for (int i = 0; i < textRows.length; ++i) {
            textRows[i] = textRows[i].trim();
        }
        fixTextRows(textRows);

        // Abbreviate if too long.
        if (textRows.length > maxRows) {
            String[] oldTextRows = textRows;
            textRows = new String[maxRows];
            for (int i = 0; i < textRows.length; ++i) {
                textRows[i] = oldTextRows[i];
            }
            StringBuffer lastRow = new StringBuffer(textRows[textRows.length-1]);
            boolean letterFound = false;
            for (int j = lastRow.length()-1; j >= 0; --j) {
                if (lastRow.charAt(j) != ' ' && !letterFound) {
                    letterFound = true;
                }
                if (lastRow.charAt(j) == ' ' && letterFound) {
                    if (j > 0 && isPunctuation(lastRow.charAt(j-1))) {

                    }
                    for (int i = j; i < lastRow.length(); ++i) {
                        if (i - j < 3) {
                            lastRow.setCharAt(i, '.');
                        } else {
                            lastRow.setCharAt(i, ' ');
                        }
                    }
                    textRows[textRows.length-1] = lastRow.toString();
                    break;
                }
            }

        }

    }

    private boolean isPunctuation(char c) {
        return c == ',' || c == '!' || c == '.' || c == '?';
    }

    private void fixTextRows(String[] textRows) {
        this.maxWidth = 0;
        for (String s : this.textRows) {
            if (s.length() > maxWidth) {
                maxWidth = s.length();
            }
        }
        maxWidth = Math.max(maxWidth, 8);
        for (int i = 0; i < this.textRows.length; ++i) {
            this.textRows[i] = MyStrings.padRight(this.textRows[i], ' ', maxWidth);
        }
    }

    public void drawYourself(ScreenHandler screenHandler, Point location) {
        int shift = shiftRight ? 1 : ((maxWidth - 1) / 2);
        int startX = location.x - shift;
        int startY = location.y - textRows.length + 1;

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
            if (startX + x == location.x + 2) {
                drawBorder(screenHandler,startX + x, startY + textRows.length + 2, 1, 2);
            } else if (startX + x == location.x + 3) {
                drawBorder(screenHandler,startX + x, startY + textRows.length + 2, 2, 2);
            }
            drawBorder(screenHandler,startX + x, startY + textRows.length + 1, 1, 1);
        }
        drawBorder(screenHandler,startX + maxWidth, startY + textRows.length + 1, 3, 2);
    }

    private void drawBorder(ScreenHandler screenHandler, int x, int y, int col, int row) {
        screenHandler.register("", new Point(x, y), calloutBorders[col][row], 3);
    }

    public static Sprite[][] makeBorderSprites() {
        Sprite[][] result = new Sprite[4][3];
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
        return result;
    }
}

package view.widget;

import model.Model;
import model.states.duel.PowerGauge;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

public class PowerGaugeWidget implements Animation {

    private static final MyColors FILL_COLOR = MyColors.LIGHT_BLUE;
    private static final MyColors BACKGROUND_COLOR = MyColors.DARK_BROWN;
    private static final MyColors LINE_COLOR = MyColors.LIGHT_GRAY;

    private static final Sprite[] SPRITES               = makeSprites(0, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_WITH_LINE     = makeSprites(1, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_ANI           = makeSprites(2, MyColors.WHITE, MyColors.YELLOW);
    private static final Sprite[] SPRITES_ANI_WITH_LINE = makeSprites(3, MyColors.WHITE, MyColors.YELLOW);

    private static final Sprite BOTTOM = makeGaugeSprite(4, 0, MyColors.BEIGE, MyColors.BLUE);
    private static final Sprite TOP = makeGaugeSprite(5, 0, MyColors.CYAN, MyColors.BLUE);

    private static final Sprite GAUGE_TYPE = new Sprite16x16("gaugetype", "gauge.png", 6, MyColors.WHITE,
            MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);
    private static final Sprite GAUGE_LEFT = new Sprite16x16("gaugetype", "gauge.png", 7, MyColors.WHITE,
            MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);
    private static final Sprite GAUGE_RIGHT = new Sprite16x16("gaugetype", "gauge.png", 8, MyColors.WHITE,
            MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);

    private final PowerGauge gauge;
    private int aniShift = 0;
    private int internalCount = 0;

    public PowerGaugeWidget(PowerGauge powerGauge) {
        this.gauge = powerGauge;
        AnimationManager.registerPausable(this);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        internalCount++;
        if (internalCount == 5) {
            internalCount = 0;
            aniShift++;
        }
    }

    @Override
    public void synch() {

    }


    public void drawYourself(ScreenHandler screenHandler, int xOffset, int yOffset) {
        int xStart = xOffset + 4;
        int yStart = 3 + yOffset;

        screenHandler.put(xStart, yStart - 1, TOP);
        if (gauge.isFull()) {
            drawSegmentsAnimated(screenHandler, xStart, yStart);
        } else {
            drawSegments(screenHandler, xStart, yStart);
        }
        screenHandler.put(xStart, yStart + gauge.getNoOfSegments(), BOTTOM);
        drawLevelLabels(screenHandler, xStart, yStart);

        BorderFrame.drawString(screenHandler, String.format("%3d", gauge.getCurrentLevel()),
                xStart - 1, yStart + gauge.getNoOfSegments() + 1,
                MyColors.WHITE, MyColors.BLACK);

        drawGaugeLogo(screenHandler, xStart, yStart);
    }

    private void drawSegmentsAnimated(ScreenHandler screenHandler, int xStart, int yStart) {
        for (int segment = 0; segment < gauge.getNoOfSegments(); ++segment) {
            Sprite[] spriteSetToUse = SPRITES_ANI;
            if (segment == gauge.getLevelIndices(0) || segment == gauge.getLevelIndices(1)) {
                spriteSetToUse = SPRITES_ANI_WITH_LINE;
            }

            Sprite spriteToUse = spriteSetToUse[aniShift % 4];
            screenHandler.put(xStart, yStart + gauge.getNoOfSegments() - segment - 1, spriteToUse);
        }
    }

    private void drawSegments(ScreenHandler screenHandler, int xStart, int yStart) {
        for (int segment = 0; segment < gauge.getNoOfSegments(); ++segment) {
            Sprite[] spriteSetToUse = SPRITES;
            if (segment == gauge.getLevelIndices(0) || segment == gauge.getLevelIndices(1)) {
                spriteSetToUse = SPRITES_WITH_LINE;
            }
            Sprite spriteToUse;
            if (segment == gauge.getCurrentLevel() / 8) {
                spriteToUse = spriteSetToUse[(gauge.getCurrentLevel()/2) % 4];
            } else if (segment < gauge.getCurrentLevel() / 8) {
                spriteToUse = spriteSetToUse[4];
            } else {
                spriteToUse = spriteSetToUse[0];
            }
            screenHandler.put(xStart, yStart + gauge.getNoOfSegments() - segment - 1, spriteToUse);
        }
    }

    private void drawLevelLabels(ScreenHandler screenHandler, int xStart, int yStart) {
        for (int i = 0; i < gauge.getLevels(); ++i) {
            MyColors color = gauge.getCurrentLevel() / 8 > gauge.getLevelIndices(i) ? MyColors.YELLOW : MyColors.GRAY;
            BorderFrame.drawString(screenHandler, String.format("%1d", (i+1)),
                    xStart - 1, yStart + gauge.getNoOfSegments() - gauge.getLevelIndices(i) - 1,
                    color, MyColors.BLACK);
        }
    }

    private void drawGaugeLogo(ScreenHandler screenHandler, int xStart, int yStart) {
        screenHandler.put(xStart-3, yStart-3, GAUGE_TYPE);
        screenHandler.put(xStart-1, yStart-3, GAUGE_LEFT);
        screenHandler.put(xStart+1, yStart-3, GAUGE_RIGHT);
    }

    private static Sprite[] makeSprites(int row, MyColors bgColor, MyColors fillColor) {
        Sprite[] result = new Sprite[5];
        for (int col = 0; col < result.length; ++col) {
            int column = col < result.length - 1 ? col : 0;
            result[col] = makeGaugeSprite(column, row, bgColor, fillColor);
            if (col == result.length - 1) {
                result[col].setColor3(bgColor);
                result[col].setColor1(fillColor);
            }
        }

        return result;
    }

    private static Sprite makeGaugeSprite(int column, int row, MyColors bgColor, MyColors fillColor) {
        Sprite sp = new Sprite("gauge", "gauge.png", column, row, 16, 8);
        sp.setColor1(bgColor);
        sp.setColor2(LINE_COLOR);
        sp.setColor3(fillColor);
        return sp;
    }

}

package view.widget;

import model.Model;
import model.states.duel.gauges.PowerGauge;
import model.states.duel.gauges.PowerGaugeSegment;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;

public abstract class PowerGaugeWidget implements Animation {

    protected static final Sprite GAUGE_LEFT = new Sprite16x16("gaugetype", "gauge.png", 7, MyColors.WHITE,
            MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);
    protected static final Sprite GAUGE_RIGHT = new Sprite16x16("gaugetype", "gauge.png", 8, MyColors.WHITE,
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
    public void synch() { }


    public void drawYourself(ScreenHandler screenHandler, int xOffset, int yOffset) {
        int xStart = xOffset + 4;
        int yStart = 3 + yOffset;

        if (gauge.isFull()) {
            drawSegmentsAnimated(screenHandler, xStart, yStart);
        } else {
            drawSegments(screenHandler, xStart, yStart);
        }

        drawLevelLabels(screenHandler, xStart, yStart);

        BorderFrame.drawString(screenHandler, String.format("%3d", gauge.getCurrentLevel()),
                xStart - 1, yStart + gauge.getNoOfSegments() + 1,
                MyColors.WHITE, MyColors.BLACK);
        drawGaugeLogo(screenHandler, xStart, yStart);
    }

    public void drawSegmentsOnly(ScreenHandler screenHandler, int xOffset, int yOffset) {
        screenHandler.clearSpace(xOffset-2, xOffset+2, yOffset-1, yOffset+gauge.getNoOfSegments()+1);
        drawSegments(screenHandler, xOffset, yOffset);
        drawLevelLabels(screenHandler, xOffset, yOffset);
    }

    public abstract void drawGaugeLogo(ScreenHandler screenHandler, int xStart, int yStart);

    private void drawLevelLabels(ScreenHandler screenHandler, int xStart, int yStart) {
        for (int i = 0; i < gauge.getLevels(); ++i) {
            PowerGaugeSegment seg = gauge.getSegment(gauge.getLevelIndices(i));
            MyColors color = gauge.getCurrentSegmentIndex() > gauge.getLevelIndices(i) ? MyColors.YELLOW : MyColors.GRAY;
            int extraForShift = seg.getXShift() < 0 ? -1 : 0;
            BorderFrame.drawString(screenHandler, gauge.getLabelForLevel(i+1),
                    xStart - 1 + extraForShift,
                    yStart + gauge.getNoOfSegments() - gauge.getLevelIndices(i) - 1,
                    color, MyColors.BLACK);
        }
    }

    private void drawSegmentsAnimated(ScreenHandler screenHandler, int xStart, int yStart) {
        drawTop(screenHandler, xStart, yStart);
        for (int segment = 0; segment < gauge.getNoOfSegments(); ++segment) {
            PowerGaugeSegment seg = gauge.getSegment(segment);
            Sprite[] spriteSetToUse = seg.getAnimatedSpriteSet();
            Sprite spriteToUse = spriteSetToUse[aniShift % 4];
            screenHandler.register(spriteToUse.getName(),
                    new Point(xStart, yStart + gauge.getNoOfSegments() - segment - 1),
                    spriteToUse, 0, seg.getXShift(), 0);
        }
        drawBottom(screenHandler, xStart, yStart);
    }

    protected abstract void drawBottom(ScreenHandler screenHandler, int xStart, int yStart);

    protected abstract void drawTop(ScreenHandler screenHandler, int xStart, int yStart);

    private void drawSegments(ScreenHandler screenHandler, int xStart, int yStart) {
        drawTop(screenHandler, xStart, yStart);

        int currentSegment = gauge.getCurrentSegmentIndex();
        int remaining = gauge.getCurrentLevel();
        for (int segment = 0; segment < gauge.getNoOfSegments(); ++segment) {
            PowerGaugeSegment seg = gauge.getSegment(segment);
            Sprite[] spriteSetToUse = seg.getNormalSpriteSet();
            Sprite spriteToUse;
            if (segment == currentSegment) {
                spriteToUse = spriteSetToUse[(remaining/seg.getWidth()) % 4];
            } else if (segment < currentSegment) {
                remaining -= gauge.getPowerPerSegment(segment);
                spriteToUse = spriteSetToUse[4];
            } else {
                spriteToUse = spriteSetToUse[0];
            }
            screenHandler.register(spriteToUse.getName(),
                    new Point(xStart, yStart + gauge.getNoOfSegments() - segment - 1),
                    spriteToUse, 0, seg.getXShift(), 0);
        }
        drawBottom(screenHandler, xStart, yStart);
    }
}

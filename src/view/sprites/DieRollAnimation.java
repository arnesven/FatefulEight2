package view.sprites;

import model.Model;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DieRollAnimation extends RunOnceAnimationSprite {

    private final int number;
    private final Sprite16x16 stillFrame;
    private int stillFrameCount = 0;
    private int xShift = 0;

    public DieRollAnimation(int faceNumber) {
        super("dierollanimation", "lotto.png", 0, 2, 16, 16, 7, MyColors.DARK_GRAY);
        setColor2(MyColors.BEIGE);
        stillFrame = new Sprite16x16("stillframedie"+faceNumber, "lotto.png", 0x10 + faceNumber - 1);
        stillFrame.setColor2(MyColors.BEIGE);
        stillFrame.setColor3(MyColors.BLACK);
        this.number = faceNumber;
        setAnimationDelay(10);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        if (super.isDone()) {
            stillFrameCount++;
        } else {
            xShift++;
        }
    }

    @Override
    public boolean isDone() {
        if (super.isDone()) {
            return stillFrameCount > 200;
        }
        return false;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        if (!super.isDone()) {
            return super.getImage();
        }
        return stillFrame.getImage();
    }

    @Override
    public int getXShift() {
        return xShift/4 * 2;
    }

    @Override
    public int getYShift() {
        int amp = 0;
        if (xShift < 30) {
            amp = 8;
        } else if (xShift < 60) {
            amp = 4;
        }

        int base = xShift/6;
        if (base % 4 == 1 || base % 4 == 3) {
            return amp/2;
        }
        if (base % 4 == 2) {
            return amp;
        }
        return 0;
    }
}

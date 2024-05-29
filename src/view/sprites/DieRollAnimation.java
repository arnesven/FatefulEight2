package view.sprites;

import model.Model;
import view.MyColors;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DieRollAnimation extends RunOnceAnimationSprite {

    private static final int MAX_SHIFT = 55;
    private final int number;
    private final Sprite16x16 stillFrame;
    private int stillFrameCount = 0;
    private int xShift = 0;

    public DieRollAnimation(int faceNumber) {
        super("dierollanimation", "lotto.png", 0, 2, 16, 16, 7, MyColors.DARK_GRAY);
        setColor2(MyColors.WHITE);
        stillFrame = new Sprite16x16("stillframedie"+faceNumber, "lotto.png", 0x10 + faceNumber - 1);
        stillFrame.setColor2(MyColors.WHITE);
        stillFrame.setColor3(MyColors.RED);
        this.number = faceNumber;
        setAnimationDelay(8);
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
            return stillFrameCount > 220;
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
        return xShift/3 * 2 - 4;
    }

    @Override
    public int getYShift() {
        double amplitude = (MAX_SHIFT - xShift) / 4.0;
        return -(int)Math.abs(Math.cos(Math.toRadians(xShift*8))*amplitude);
    }

    public boolean blocksGame() {
        return !super.isDone();
    }
}

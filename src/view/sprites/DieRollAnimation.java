package view.sprites;

import model.Model;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DieRollAnimation extends RunOnceAnimationSprite {

    private static final int MAX_SHIFT = 55;
    private static boolean animationBocks = true;
    private final int number;
    private final Sprite16x16 stillFrame;
    private RunOnceAnimationSprite sparkAnimation = null;
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

    public static void setAnimationBlocks(boolean blocking) {
        animationBocks = blocking;
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
        return getCompoundedImage();
    }

    private BufferedImage getCompoundedImage() throws IOException {
        BufferedImage img = stillFrame.internalGetImage();
        BufferedImage toReturn = new BufferedImage(stillFrame.getWidth(), stillFrame.getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        g.drawImage(img,0, 0, null);
        if (sparkAnimation != null) {
            g.drawImage(sparkAnimation.getImage(), 0, 0, null);
        }
        return toReturn;
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
        return animationBocks && !super.isDone();
    }

    public void startTwinkle() {
        if (sparkAnimation == null && number == 10) {
            sparkAnimation = new RunOnceAnimationSprite("dierollspark", "lotto.png",
                    7, 2, 16, 16, 3, MyColors.LIGHT_YELLOW);
        }
    }

    public void unregisterYourself() {
        AnimationManager.unregister(this);
        AnimationManager.unregister(sparkAnimation);
    }
}

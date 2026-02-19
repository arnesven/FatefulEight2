package model.characters.appearance;

import util.Arithmetics;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.LoopingSprite;

import java.awt.*;
import java.io.Serializable;

public class WeepingAnimation implements Serializable {
    private final Point locationLeft;
    private final WeepingSprite leftSprite;
    private final Point locationRight;
    private final WeepingSprite rightSprite;
    private final WeepingAmount amount;
    private WeepingSprite leftExtra = null;
    private WeepingSprite rightExtra = null;

    public WeepingAnimation(Point location, WeepingAmount amount) {
        this.locationLeft = new Point(location.x+2, location.y+3);
        this.locationRight = new Point(location.x+4, location.y+3);
        this.leftSprite = new WeepingSprite(false, amount.ordinal(), false);
        this.rightSprite = new WeepingSprite(true, amount.ordinal(), true);
        if (amount == WeepingAmount.veryMuch) {
            this.leftExtra = new WeepingSprite(false, amount.ordinal(), true);
            this.rightExtra = new WeepingSprite(true, amount.ordinal(), false);
        }
        this.amount = amount;
    }

    public void drawYourself(ScreenHandler screenHandler) {
        screenHandler.register(leftSprite.getName(), locationLeft, leftSprite, 1, -3, 3);
        if (amount.ordinal() > WeepingAmount.singleTear.ordinal()) {
            screenHandler.register(rightSprite.getName(), locationRight, rightSprite, 1, +3, 3);
        }
        if (amount == WeepingAmount.veryMuch) {
            screenHandler.register(leftExtra.getName(), locationLeft, leftExtra, 1, -3, 3);
            screenHandler.register(rightExtra.getName(), locationRight, rightExtra, 1, +3, 3);
        }
    }

    private static class WeepingSprite extends LoopingSprite {
        private final boolean flip;

        public WeepingSprite(boolean flip, int speed, boolean startLate) {
            super("weepingsprite", "mouth.png", 0x40, 8, 16);
            setFrames(8);
            speed = Math.max(1, speed - 1);
            setDelay(20 - speed * 4);
            setColor1(MyColors.WHITE);
            setColor2(MyColors.LIGHT_BLUE);
            setFlipHorizontal(flip);
            this.flip = flip;
            if (startLate) {
                if (flip) {
                    setCurrentFrame(3);
                } else {
                    setCurrentFrame(6);
                }
            } else {
                if (flip) {
                    setCurrentFrame(1);
                }
            }
        }

        @Override
        protected int stepToNextFrame(int currentFrame) {
            if (flip) {
                return Arithmetics.decrementWithWrap(currentFrame, getFrames());
            }
            return super.stepToNextFrame(currentFrame);
        }
    }
}

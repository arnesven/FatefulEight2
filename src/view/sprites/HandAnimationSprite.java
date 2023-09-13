package view.sprites;

import model.Model;
import model.states.cardgames.TableSeating;
import view.MyColors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HandAnimationSprite extends Sprite implements Animation {
    private static final int SLICE_WIDTH = 16;
    private static final int SLICE_HEIGHT = 64;
    private final TableSeating seating;
    private int count = 0;
    private int currentFrame = 0;
    private int animationSpeed = 3;
    private static final int MAX_FRAMES = 15;

    public HandAnimationSprite(MyColors skinColor, TableSeating rotationAndOffset) {
        super("handanimation", "cardgame.png", 0, 1, SLICE_WIDTH * MAX_FRAMES, SLICE_HEIGHT);
        setColor2(skinColor);
        this.seating = rotationAndOffset;
        AnimationManager.registerPausable(this);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        count++;
        if (count % animationSpeed == 0) {
            currentFrame++;
        }
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = super.internalGetImage();
        BufferedImage slice = new BufferedImage(SLICE_HEIGHT, SLICE_HEIGHT, img.getType());
        {
            Graphics g = slice.getGraphics();
            g.drawImage(img,
                    (SLICE_HEIGHT - SLICE_WIDTH) / 2, 0, (SLICE_HEIGHT - SLICE_WIDTH) / 2 + SLICE_WIDTH, SLICE_HEIGHT,
                    currentFrame * SLICE_WIDTH, 0, (currentFrame + 1) * SLICE_WIDTH, SLICE_HEIGHT, null);
        }
        BufferedImage toReturn = new BufferedImage(calcWidth(), calcHeight(), img.getType());
        {
            Graphics2D g2d = (Graphics2D) (toReturn.getGraphics());
            double rotationRequired = Math.toRadians(getTrueRotation());
            double locationX = SLICE_HEIGHT / 2;
            double locationY = SLICE_HEIGHT / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            if (getTrueRotation() % 180 == 0) {
                g2d.drawImage(op.filter(slice, null),
                        0, 0, calcWidth(), calcHeight(),
                        (SLICE_HEIGHT - SLICE_WIDTH) / 2, 0, (SLICE_HEIGHT - SLICE_WIDTH) / 2 + SLICE_WIDTH, SLICE_HEIGHT,
                        null);
            } else {
                g2d.drawImage(op.filter(slice, null),
                        0, 0, calcWidth(), calcHeight(),
                        0, (SLICE_HEIGHT - SLICE_WIDTH) / 2,
                        SLICE_HEIGHT,(SLICE_HEIGHT - SLICE_WIDTH) / 2 + SLICE_WIDTH,
                        null);
            }
        }
        return toReturn;
    }

    private double getTrueRotation() {
        return seating == null ? 0 : seating.rotation;
    }

    private int calcWidth() {
        if (seating == null) {
            return 1;
        }
        return seating.rotation % 180 == 0 ? 16 : 64;
    }

    private int calcHeight() {
        if (seating == null) {
            return 1;
        }
        return seating.rotation % 180 == 0 ? 64 : 16;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return internalGetImage();
    }

    public boolean isDone() {
        return currentFrame >= MAX_FRAMES;
    }
    public TableSeating getSeating() {
        return seating;
    }

    @Override
    public void synch() { }
}

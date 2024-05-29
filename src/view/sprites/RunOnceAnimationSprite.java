package view.sprites;

import model.Model;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RunOnceAnimationSprite extends Sprite implements Animation {
    private int count = 0;
    private int currentFrame = 0;
    private int animationSpeed = 3;

    public RunOnceAnimationSprite(String name, String map, int col, int row, int width, int height, int frames, MyColors color) {
        super(name, map, col, row, width, height);
        setFrames(frames);
        setColor1(color);
        AnimationManager.registerPausable(this);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        count++;
        if (count % animationSpeed == 0) {
            currentFrame++;
        }
    }

    public void setAnimationDelay(int animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    @Override
    public void synch() {

    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = super.internalGetImage();
        BufferedImage toReturn = new BufferedImage(getWidth(), getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        g.drawImage(img,
                0, 0, getWidth(), getHeight(), currentFrame*getWidth(), 0, (currentFrame+1)*getWidth(), getHeight(),null);
        return toReturn;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return internalGetImage();
    }

    public boolean isDone() {
        return currentFrame >= getMaximumFrames();
    }

    public int getXShift() { return 0; }
    public int getYShift() { return 0; }

    public int getCurrentFrame() {
        return currentFrame;
    }
}

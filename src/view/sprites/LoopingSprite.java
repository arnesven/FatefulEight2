package view.sprites;

import model.Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class LoopingSprite extends Sprite implements Animation {

    private int currentFrame = 0;
    private int count = 0;
    private int delay = 16;

    public LoopingSprite(String name, String mapPath, int num, int width, int height) {
        super(name, mapPath, num % 16, num / 16, width, height);
        AnimationManager.register(this);
    }

    public LoopingSprite(String name, String mapPath, int num, int size) {
        this(name, mapPath, num, size, size);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        count++;
        if (count % delay == 0) {
            currentFrame = (currentFrame + 1) % getFrames();
        }
    }

    @Override
    public void synch() {
        currentFrame = 0;
        count = 0;
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = super.internalGetImage();
        BufferedImage toReturn = new BufferedImage(getWidth(), getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        g.drawImage(img,
                0, 0, getWidth(), getHeight(), getCurrentFrameIndex(currentFrame)*getWidth(), 0, (getCurrentFrameIndex(currentFrame)+1)*getWidth(), getHeight(),null);
        return toReturn;
    }

    protected int getCurrentFrameIndex(int currentFrame) {
        return currentFrame;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return internalGetImage();
    }

    public void setDelay(int i) {
        this.delay = i;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        AnimationManager.register(this);
    }
}


package view.sprites;

import model.Model;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MouthMovementSprite extends Sprite8x8 implements Animation {
    private static final int MAX_FRAMES = 9;
    private static final int TALK_DELAY = 6;
    private int frame;
    private int count;
    private int steps;

    public MouthMovementSprite(int length, MyColors skinColor, MyColors lipColor, boolean withTusks, boolean vampireTeeth) {
        super("mouthani", "mouth.png", vampireTeeth ? 0x40 : (withTusks ? 0x10 : 0x00),
                MyColors.BLACK, lipColor, MyColors.BEIGE, skinColor);
        setFrames(MAX_FRAMES);
        this.count = 0;
        this.steps = length*2;
        setRandomFrame();
        AnimationManager.registerPausable(this);
    }

    private void setRandomFrame() {
        frame = MyRandom.randInt(getFrames());
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (this.count < steps) {
            this.count++;
            if (count % TALK_DELAY == 0) {
                setRandomFrame();
            }
        }
    }

    @Override
    public void synch() { }

    public boolean isDone() {
        return count >= steps;
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = super.internalGetImage();
        BufferedImage toReturn = new BufferedImage(getWidth(), getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        g.drawImage(img,
                0, 0, getWidth(), getHeight(),
                frame * getWidth(), 0,
                (frame + 1) * getWidth(), getHeight(), null);
        return toReturn;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return internalGetImage();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        AnimationManager.register(this);
    }

}
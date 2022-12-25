package view.sprites;

import model.Model;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AnimatedCharSprite extends Sprite8x8 implements Animation {
    private int count = 0;
    private int currentFrame = 0;

    public AnimatedCharSprite(char c, MyColors color1, MyColors color2, MyColors color3, int frames) {
        super("animatedchar" + c, "charset.png", c);
        this.setColor1(color1);
        this.setColor2(color2);
        this.setColor3(color3);
        setFrames(frames);
        AnimationManager.register(this);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        count++;
        if (count % 16 == 0) {
            currentFrame = (currentFrame + 1) % getFrames();
        }
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        AnimationManager.register(this);
    }
}

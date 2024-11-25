package view.sprites;

import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CrateAndAvatarSprite extends Sprite {
    private final Sprite avatar;

    public CrateAndAvatarSprite(Sprite avatar, MyColors color2, MyColors color3, int column, int row, int width, int height) {
        super("crateandavatar", "warehouse.png", column, row, width, height);
        this.avatar = avatar;
        setColor1(MyColors.DARK_GRAY);
        setColor2(color2);
        setColor3(color3);
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return internalGetImage();
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage base = super.internalGetImage();
        if (avatar != null) {
            Graphics g = base.getGraphics();
            BufferedImage other = avatar.internalGetImage();

            int xShift = (getWidth()/64) - getRow();
            int yShift = (getHeight()/64) - getColumn();

            g.drawImage(avatar.internalGetImage(), xShift * 32, yShift * 32,
                    other.getWidth(), other.getHeight(), null);
        }
        return base;
    }
}

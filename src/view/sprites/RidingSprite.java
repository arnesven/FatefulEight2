package view.sprites;

import model.characters.GameCharacter;
import model.horses.Horse;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class RidingSprite extends LoopingSprite {
    private final AvatarSprite avatar;
    private boolean isShort = false;

    public RidingSprite(GameCharacter leader, Horse horse) {
        super("riding", "riding.png", 0, 32, 48);
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setColor3(horse.getAvatarColor());
        if (leader.getRace().isShort()) {
            this.isShort = true;
        }
        this.avatar = leader.getAvatarSprite();
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = super.internalGetImage();
        BufferedImage toReturn = new BufferedImage(getWidth(), getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        if (avatar != null) {
            avatar.synch();
            BufferedImage avatarImg = avatar.internalGetImage();
            int yOffset = 0;
            if (isShort) {
                yOffset = -2;
            }
            g.drawImage(avatarImg, 0, yOffset, avatarImg.getWidth(), avatarImg.getHeight(), null);
        }
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        return toReturn;
    }
}

package view.sprites;

import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.horses.Horse;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class RidingSprite extends LoopingSprite {
    private final LoopingSprite avatar;
    private boolean isShort = false;

    public RidingSprite(GameCharacter leader, Horse horse, int version) {
        super("riding", "riding.png", 0x10*version, 32, 48);
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setColor3(horse.getAvatarColor());
        if (leader.getRace().isShort()) {
            this.isShort = true;
        }
        this.avatar = leader.getAvatarSprite();
    }

    public RidingSprite(GameCharacter leader, Horse horse) {
        this(leader, horse, 0);
    }

    public RidingSprite(Enemy enemy, Horse horse) {
        super("riding", "riding.png", 0x0, 32, 48);
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setColor3(horse.getAvatarColor());
        this.isShort = false;
        if (enemy.getAvatar() instanceof LoopingSprite) {
            this.avatar = (LoopingSprite) enemy.getAvatar();
        } else {
            throw new IllegalStateException("RidingSprite only supports enemies with LoopingSprite avatars!");
        }
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

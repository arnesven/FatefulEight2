package view.sprites;

import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WeaponPairOnAvatarSprite extends AvatarItemSprite {
    private final AvatarItemSprite sprite1;
    private final AvatarItemSprite sprite2;

    public WeaponPairOnAvatarSprite(AvatarItemSprite sprite1, AvatarItemSprite sprite2) {
        super(0, MyColors.BEIGE, MyColors.DARK_BLUE, MyColors.GRAY_RED, MyColors.DARK_GRAY);
        this.sprite1 = sprite1.copy();
        this.sprite2 = sprite2.copy();
        this.sprite2.setFlipHorizontal(true);
        SpriteCache.invalidate(this);
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        if (sprite1 == null) {
            return super.internalGetImage();
        }

        BufferedImage img = sprite2.internalGetImage();
        BufferedImage toReturn = new BufferedImage(getWidth(), getHeight(), img.getType());
        Graphics g = toReturn.getGraphics();
        g.drawImage(img, 0, 0,null);
        g.drawImage(sprite1.internalGetImage(), 0, 0, null);
        return toReturn;
    }
}

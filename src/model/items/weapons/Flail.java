package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Flail extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(6, 1);

    protected static final AvatarItemSprite ON_AVATAR_SPRITES =
            new AvatarItemSprite(0x2C, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.TRANSPARENT);


    public Flail() {
        super("Flail", 21, new int[]{5,6,10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Flail();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return ON_AVATAR_SPRITES;
    }
}

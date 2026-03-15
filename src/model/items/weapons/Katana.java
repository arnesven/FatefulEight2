package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Katana extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(10, 0);
    private static final AvatarItemSprite AVATAR_SPRITES =
            new FixedAvatarItemSprite(0x0D, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.TRANSPARENT);

    public Katana() {
        super("Katana", 20, new int[]{9, 10, 11, 12, 13}, true, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Katana();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITES;
    }
}

package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.*;

public class Claymore extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(12, 0);

    private static final AvatarItemSprite AVATAR_SPRITES =
            new FixedAvatarItemSprite(0x0E, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.DARK_RED, MyColors.TRANSPARENT);

    public Claymore() {
        super("Claymore", 24, new int[]{7,9,12,13}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Claymore();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITES;
    }
}

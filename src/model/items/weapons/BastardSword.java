package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.*;

public class BastardSword extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(13, 0);

    protected static final AvatarItemSprite TWO_HANDED_SWORD_SPRITES =
            new FixedAvatarItemSprite(0x0C, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.GOLD);

    public BastardSword() {
        super("Bastard Sword", 24, new int[]{5,6,13,13,14}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BastardSword();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return TWO_HANDED_SWORD_SPRITES;
    }
}

package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Zweihander extends BladedWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(12, 1);

    private static final AvatarItemSprite AVATAR_SPRITES =
            new FixedAvatarItemSprite(0x0E, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.GOLD, MyColors.LIGHT_GRAY);

    public Zweihander() {
        super("Zweihander", 30, new int[]{6, 8, 11, 14, 14}, true, 1);
    }

    @Override
    public int getWeight() {
        return 5000; // TODO:
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwoHandedSword();
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

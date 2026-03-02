package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Naginata extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 12);

    private static final AvatarItemSprite POLEARM_SPRITES =
            new FixedAvatarItemSprite( 0x91, MyColors.BROWN, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT);

    public Naginata() {
        super("Naginata", 36, new int[]{9,9,9,10,10});
    }

    @Override
    public int getSpeedModifier() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Naginata();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return POLEARM_SPRITES;
    }
}

package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Pike extends PolearmWeapon {

    private static final AvatarItemSprite[] POLEARM_SPRITES = makeShiftedSpriteSet(
            new FixedAvatarItemSprite( 0x90, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT));


    private static final Sprite SPRITE = new TwoHandedItemSprite(6, 4);

    public Pike() {
        super("Pike", 34, new int[]{9,9,9,9,9,9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Pike();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return POLEARM_SPRITES[index];
    }
}

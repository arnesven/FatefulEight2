package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class GreatAxe extends AxeWeapon {

    private static final AvatarItemSprite TWO_HANDED_AXE_SPRITES = new FixedAvatarItemSprite(0x4C, MyColors.BROWN, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY);

    private static final Sprite SPRITE = new TwoHandedItemSprite(5, 5);

    public GreatAxe() {
        super("Great Axe", 20, new int[]{5, 8, 10, 14}, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GreatAxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return TWO_HANDED_AXE_SPRITES;
    }
}

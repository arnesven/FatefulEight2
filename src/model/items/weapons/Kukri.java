package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Kukri extends BladedWeapon implements PairableWeapon {

    private static final AvatarItemSprite SWORD_SPRITES =
            new AvatarItemSprite(0x0, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    private static final Sprite SPRITE = new ItemSprite(4, 11);

    public Kukri() {
        super("Kukri", 26, new int[]{5, 9}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Kukri();
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
    public Sprite makePairSprite() {
        return new ItemSprite(1, 14);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return SWORD_SPRITES;
    }
}

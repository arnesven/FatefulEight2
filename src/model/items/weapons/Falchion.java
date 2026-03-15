package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Falchion extends BladedWeapon implements PairableWeapon {

    private static final AvatarItemSprite SWORD_SPRITES =
            new AvatarItemSprite(0x0, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    private static final Sprite SPRITE = new ItemSprite(7, 0);

    public Falchion() {
        super("Falchion", 16, new int[]{5, 8, 14}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Falchion();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(8, 14);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return SWORD_SPRITES;
    }
}

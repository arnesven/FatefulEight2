package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Scepter extends BluntWeapon implements PairableWeapon {

    private static final AvatarItemSprite BLUNT_SPRITES =
            new AvatarItemSprite(0x20, MyColors.BROWN, MyColors.GOLD, MyColors.GOLD , MyColors.TRANSPARENT);

    private static final Sprite SPRITE = new ItemSprite(4, 1);

    public Scepter() {
        super("Scepter", 21, new int[]{5, 8, 10}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Scepter();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(11, 14);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return BLUNT_SPRITES;
    }
}

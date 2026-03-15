package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class OrcishKnife extends SmallBladedWeapon implements PairableWeapon {

    private static final AvatarItemSprite SMALL_BLADES =
            new AvatarItemSprite(0x06, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    private static final Sprite SPRITE = new ItemSprite(4, 0);

    public OrcishKnife() {
        super("Orcish Knife", 18, new int[]{3, 7}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OrcishKnife();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(5, 14);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return SMALL_BLADES;
    }
}

package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Sicle extends AxeWeapon implements PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(9, 12);
    private static final AvatarItemSprite AXE_SPRITES = new AvatarItemSprite(0x46,
            MyColors.LIGHT_GRAY, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.TRANSPARENT);

    public Sicle() {
        super("Sicle", 20, new int[]{7, 8, 8}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Sicle();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(6, 15);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AXE_SPRITES;
    }
}

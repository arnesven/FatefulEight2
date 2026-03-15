package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class PineWand extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(1, 6);

    private static final AvatarItemSprite WAND_SPRITES = new AvatarItemSprite(0x10,
            MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.BROWN);

    public PineWand() {
        super("Pine Wand", 20, Skill.MagicRed, new int[]{9, 11, 14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PineWand();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(9, 15);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return WAND_SPRITES;
    }
}

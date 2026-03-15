package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CommonPickaxe extends Pickaxe {

    private static final Sprite SPRITE = new ItemSprite(2, 5);

    private static final AvatarItemSprite AXE_SPRITES = new AvatarItemSprite(0x46,
            MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.LIGHT_GRAY, MyColors.BROWN);

    public CommonPickaxe() {
        super("Pickaxe", 18, new int[]{5, 8, 10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CommonPickaxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AXE_SPRITES;
    }
}

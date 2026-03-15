package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RustyPickaxe extends Pickaxe {

    private static final Sprite SPRITE =  new ItemSprite(2, 5, MyColors.DARK_BROWN, MyColors.DARK_RED);
    private static final AvatarItemSprite AXE_SPRITES = new AvatarItemSprite(0x46,
            MyColors.DARK_BROWN, MyColors.DARK_RED, MyColors.DARK_RED, MyColors.DARK_BROWN);

    public RustyPickaxe() {
        super("Rusty Pickaxe", 5, new int[]{6, 11});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RustyPickaxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AXE_SPRITES;
    }
}

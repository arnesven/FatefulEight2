package model.items;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Tonfa extends BrawlingWeapon implements BlockingItem {
    private static final Sprite SPRITE = new ItemSprite(3, 8);

    private static final AvatarItemSprite AVATAR_SPRITE =
            new AvatarItemSprite(0x53, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.BEIGE);

    public Tonfa() {
        super("Tonfa", 18, new int[]{6, 8, 10}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }

    @Override
    public Item copy() {
        return new Tonfa();
    }

    @Override
    public String getExtraText() {
        return "Block " + getBlockChance();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(4, 8);
    }

    @Override
    public int getBlockChance() {
        return 1;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}

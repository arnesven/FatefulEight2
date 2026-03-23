package model.items.weapons;

import model.items.Item;
import model.items.PirateItem;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Musket extends SlugThrower implements PirateItem {

    private static final Sprite SPRITE = new TwoHandedItemSprite(14, 16);
    private static final AvatarItemSprite ON_SPRITE = new FixedAvatarItemSprite(0x3D, MyColors.BROWN, MyColors.GRAY, MyColors.GRAY, MyColors.TRANSPARENT);


    public Musket() {
        super("Musket", 65, new int[]{7, 7, 7, 7, 10}, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 3400;
    }

    @Override
    public Item copy() {
        return new Musket();
    }

    @Override
    public int getStance() {
        return POLEARM_STANCE;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return ON_SPRITE;
    }
}

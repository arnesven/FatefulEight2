package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class CompetitionBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(9, 6, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.BEIGE);

    public CompetitionBow() {
        super("Competition Bow", 10, new int[]{12,13,14,15});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CompetitionBow();
    }
}

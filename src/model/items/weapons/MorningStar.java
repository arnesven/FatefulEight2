package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class MorningStar extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(5,1);

    public MorningStar() {
        super("Morning Star", 18, new int[]{5, 7, 10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MorningStar();
    }
}

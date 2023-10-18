package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Warhammer extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(7, 1);

    public Warhammer() {
        super("Warhammer", 18, new int[]{4,7,10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Warhammer();
    }
}

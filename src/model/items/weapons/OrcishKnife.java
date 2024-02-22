package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class OrcishKnife extends SmallBladedWeapon {
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
}

package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Flail extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(6, 1);

    public Flail() {
        super("Flail", 16, new int[]{5,6,10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Flail();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}

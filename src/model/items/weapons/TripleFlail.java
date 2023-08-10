package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TripleFlail extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(8, 11);

    public TripleFlail() {
        super("Triple Flail", 42, new int[]{9, 10, 14}, false, -2);
    }

    @Override
    public int getNumberOfAttacks() {
        return 3;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TripleFlail();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}

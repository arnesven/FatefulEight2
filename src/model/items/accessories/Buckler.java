package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Buckler extends ShieldItem {
    private static final Sprite SPRITE =  new ItemSprite(1, 3);

    public Buckler() {
        super("Buckler", 16, false, 1);
    }

    @Override
    public int getAP() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Buckler();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}

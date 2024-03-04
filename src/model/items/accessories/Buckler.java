package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import util.MyRandom;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Buckler extends ShieldItem {
    private static final Sprite SPRITE =  new ItemSprite(1, 3);
    private static final Sprite ALT_SPRITE =  new ItemSprite(8, 3);
    private final Sprite sprite;

    public Buckler() {
        super("Buckler", 10, false, 1);
        this.sprite = MyRandom.flipCoin() ? SPRITE : ALT_SPRITE;
    }

    @Override
    public int getAP() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
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

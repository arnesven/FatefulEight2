package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ChainMail extends HeavyArmorClothing {
    private static final Sprite SPRITE = new ItemSprite(5, 2);

    public ChainMail() {
        super("Chain Mail", 20, 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ChainMail();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}

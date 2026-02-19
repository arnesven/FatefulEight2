package model.items;

import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Tonfa extends BrawlingWeapon implements BlockingItem {
    private static final Sprite SPRITE = new ItemSprite(3, 8);

    public Tonfa() {
        super("Tonfa", 18, new int[]{6, 8, 10}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Tonfa();
    }

    @Override
    public String getExtraText() {
        return "Block " + getBlockChance();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(4, 8);
    }

    @Override
    public int getBlockChance() {
        return 1;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}

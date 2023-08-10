package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import util.MyRandom;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BattleAxe extends AxeWeapon {
    private static final Sprite SPRITE = new ItemSprite(3, 5);
    private static final Sprite ALT_SPRITE = new ItemSprite(3, 11);
    private final Sprite sprite;

    public BattleAxe() {
        super("Battle Axe", 14, new int[]{5, 8, 10}, false);
        if (MyRandom.flipCoin()) {
            sprite = SPRITE;
        } else {
            sprite = ALT_SPRITE;
        }
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new BattleAxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}

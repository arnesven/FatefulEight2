package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.items.clothing.FurArmor;
import util.MyRandom;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BattleAxe extends AxeWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(3, 5);
    private static final Sprite ALT_SPRITE = new ItemSprite(3, 11);
    private Sprite sprite;

    public BattleAxe() {
        super("Battle Axe", 19, new int[]{5, 8, 9}, false);
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
        BattleAxe toReturn = new BattleAxe();
        if (this.sprite == SPRITE) {
            toReturn.sprite = SPRITE;
        } else {
            toReturn.sprite = ALT_SPRITE;
        }
        return toReturn;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public Sprite makePairSprite() {
        if (sprite == ALT_SPRITE) {
            return new ItemSprite(15, 15);
        }
        return new ItemSprite(2, 15);
    }
}

package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BattleAxe extends AxeWeapon {
    private static final Sprite SPRITE = new ItemSprite(3, 5);

    public BattleAxe() {
        super("Battle Axe", 14, new int[]{5, 8, 10}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
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

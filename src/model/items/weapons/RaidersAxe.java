package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RaidersAxe extends AxeWeapon {
    private static final Sprite SPRITE = new ItemSprite(2, 11);

    public RaidersAxe() {
        super("Raider's Axe", 18, new int[]{5, 8, 8, 10}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RaidersAxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}

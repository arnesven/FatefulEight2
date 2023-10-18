package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class DaiKatana extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(10, 1);

    public DaiKatana() {
        super("Dai-Katana", 26, new int[]{10, 11, 11, 11, 12}, true, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DaiKatana();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}

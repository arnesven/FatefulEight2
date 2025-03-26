package model.items.weapons;

import model.items.Item;
import view.sprites.RangedStrikeEffect;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Harpoons extends PolearmWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(0, 17);

    public Harpoons() {
        super("Harpoons", 30, new int[]{10,10,10,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Javelins();
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }

    @Override
    public int getWeight() {
        return 1500;
    }
}

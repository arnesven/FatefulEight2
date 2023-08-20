package model.items.weapons;

import model.items.Item;
import view.sprites.RangedStrikeEffect;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class ThrowingAxes extends AxeWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(1, 5);

    public ThrowingAxes() {
        super("Throwing Axes", 16, new int[]{7, 10}, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public Item copy() {
        return new ThrowingAxes();
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }
}

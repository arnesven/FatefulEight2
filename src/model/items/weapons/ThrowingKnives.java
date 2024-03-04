package model.items.weapons;

import model.items.Item;
import view.sprites.RangedStrikeEffect;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class ThrowingKnives extends SmallBladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 0);

    public ThrowingKnives() {
        super("Throwing Knives", 18, new int[]{6, 10}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ThrowingKnives();
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }
}

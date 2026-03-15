package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.*;

public class ThrowingKnives extends SmallBladedWeapon {

    private static final AvatarItemSprite SMALL_BLADES =
            new AvatarItemSprite(0x06, MyColors.LIGHT_GRAY, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.TRANSPARENT);

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

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return SMALL_BLADES;
    }
}

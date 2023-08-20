package model.enemies.behaviors;

import view.MyColors;
import view.sprites.RunOnceAnimationSprite;

public class MagicMeleeAttackBehavior extends MeleeAttackBehavior {
    @Override
    public boolean allowsDamageReduction() {
        return false;
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new MagicMeleeStrikeEffectSprite();
    }

    private static class MagicMeleeStrikeEffectSprite extends RunOnceAnimationSprite {
        public MagicMeleeStrikeEffectSprite() {
            super("strikeeffectsprite", "combat.png", 0, 3, 32, 32, 8, MyColors.LIGHT_RED);
        }
    }

    @Override
    public String getUnderText() {
        return "Magic";
    }
}

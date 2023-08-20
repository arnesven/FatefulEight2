package model.enemies.behaviors;

import model.Model;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;

public class MagicRangedAttackBehavior extends EnemyAttackBehavior {
    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public boolean allowsDamageReduction() {
        return false;
    }

    @Override
    public String getUnderText() {
        return "Magic Ranged";
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new RangedMagicStrikeEffectSprite();
    }

    private static class RangedMagicStrikeEffectSprite extends RunOnceAnimationSprite {
        private int shift = -64;

        public RangedMagicStrikeEffectSprite() {
            super("enemyrangedmagiceffect", "combat.png", 8, 2, 32, 32, 4, MyColors.WHITE);
            setColor2(MyColors.LIGHT_RED);
        }

        @Override
        public int getYShift() {
            return shift;
        }

        @Override
        public void stepAnimation(long elapsedTimeMs, Model model) {
            super.stepAnimation(elapsedTimeMs, model);
            shift += 4;
        }
    }
}

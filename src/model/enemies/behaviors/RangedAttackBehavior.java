package model.enemies.behaviors;

import model.Model;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;

public class RangedAttackBehavior extends EnemyAttackBehavior {
    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new EnemyRangedStrikeEffectSprite();
    }

    private static class EnemyRangedStrikeEffectSprite extends RunOnceAnimationSprite {
        private int shift = -64;

        public EnemyRangedStrikeEffectSprite() {
            super("rangedstrike", "combat.png", 8, 0, 32, 32, 4, MyColors.WHITE);
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

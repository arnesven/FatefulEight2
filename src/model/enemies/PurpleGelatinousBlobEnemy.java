package model.enemies;

import view.MyColors;

public class PurpleGelatinousBlobEnemy extends GelatinousBlobEnemy {

    public PurpleGelatinousBlobEnemy(char a) {
        super(a, MyColors.PURPLE, MyColors.DARK_PURPLE);
        setAttackBehavior(new MagicBlobAttackBehavior());
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new PurpleGelatinousBlobEnemy(getEnemyGroup());
    }

    private static class MagicBlobAttackBehavior extends BlobAttackBehavior {
        public MagicBlobAttackBehavior() {
            super(6);
        }

        @Override
        public boolean isPhysicalAttack() {
            return false;
        }
    }
}

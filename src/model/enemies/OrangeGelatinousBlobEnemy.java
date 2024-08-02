package model.enemies;

import view.MyColors;

public class OrangeGelatinousBlobEnemy extends GelatinousBlobEnemy {

    public OrangeGelatinousBlobEnemy(char a) {
        super(a, MyColors.ORANGE, MyColors.RED);
        setAttackBehavior(new RangedBlobAttackBehavior());
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new OrangeGelatinousBlobEnemy(getEnemyGroup());
    }

    private static class RangedBlobAttackBehavior extends BlobAttackBehavior {
        public RangedBlobAttackBehavior() {
            super(6);
        }

        @Override
        public boolean canAttackBackRow() {
            return true;
        }
    }
}

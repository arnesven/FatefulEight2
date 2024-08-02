package model.enemies;

import model.states.events.GelatinousBlobEvent;
import view.MyColors;

public class WhiteGelatinousBlobEnemy extends GelatinousBlobEnemy {
    public WhiteGelatinousBlobEnemy(char a) {
        super(a, MyColors.WHITE, MyColors.LIGHT_GRAY);
        setAttackBehavior(new BlobAttackBehavior(4));
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    protected GelatinousBlobEnemy getMitosisCopy() {
        return GelatinousBlobEvent.makeRandomBlob();
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new WhiteGelatinousBlobEnemy(getEnemyGroup());
    }
}

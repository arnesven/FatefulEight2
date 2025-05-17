package model.enemies;

import model.enemies.behaviors.ParalysisAttackBehavior;

public class GiantSpider extends SpiderEnemy {
    public GiantSpider(char a) {
        super(a, "Giant Spider", HOSTILE, new ParalysisAttackBehavior(3));
    }

    @Override
    public int getMaxHP() {
        return 10;
    }
}

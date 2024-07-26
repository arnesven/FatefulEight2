package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;
import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinSpearman extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0x84);

    public GoblinSpearman(char a) {
        super(a, "Spearman", new MeleeAttackBehavior());
    }

    @Override
    public int getSpeed() {
        return 7;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinSpearman(getEnemyGroup());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

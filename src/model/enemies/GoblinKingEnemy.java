package model.enemies;

import model.enemies.behaviors.KnockBackAttackBehavior;
import model.enemies.behaviors.MixedAttackBehavior;
import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinKingEnemy extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0xB7);

    public GoblinKingEnemy(char a) {
        super(a, "Goblin King", new KnockBackAttackBehavior(5));
    }

    @Override
    public int getSpeed() {
        return -3;
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinKingEnemy(getEnemyGroup());
    }
}

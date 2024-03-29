package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;
import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinSwordsman extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0x80);

    public GoblinSwordsman(char a) {
        super(a, "Goblin Swordsman", new MeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinSwordsman(getEnemyGroup());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

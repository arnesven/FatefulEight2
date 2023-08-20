package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;
import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinAxeWielder extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0x88);

    public GoblinAxeWielder(char b) {
        super(b, "Goblin Axe Wielder", new MeleeAttackBehavior());
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinAxeWielder(getEnemyGroup());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

package model.enemies;

import model.enemies.behaviors.PoisonAttackBehavior;
import model.enemies.behaviors.RangedPoisonAttackBehavior;
import view.sprites.FrogManSprite;
import view.sprites.Sprite;

public class FrogmanShamanEnemy extends FrogmanEnemy {
    private static final Sprite SPRITE = new FrogManSprite(0xCC);

    public FrogmanShamanEnemy(char b) {
        super(b, "Frogman Shaman", new RangedPoisonAttackBehavior(5));
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

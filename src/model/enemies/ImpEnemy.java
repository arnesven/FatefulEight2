package model.enemies;

import model.enemies.behaviors.RangedAttackBehavior;
import view.sprites.Sprite;

public class ImpEnemy extends AltarEnemy {
    private static final Sprite SPRITE = makeDemonSprite("impenemy", 0x5E);

    public ImpEnemy(char a) {
        super(a, "Imp");
        setAttackBehavior(new RangedAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 5;
    }
}

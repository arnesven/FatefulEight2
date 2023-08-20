package model.enemies;

import model.enemies.behaviors.MagicMeleeAttackBehavior;
import view.sprites.Sprite;

public class FiendEnemy extends AltarEnemy {
    private static final Sprite SPRITE = makeDemonSprite("fiend", 0x5C);

    public FiendEnemy(char a) {
        super(a, "Fiend");
        setAttackBehavior(new MagicMeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 4;
    }
}

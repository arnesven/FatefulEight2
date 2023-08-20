package model.enemies;

import model.enemies.behaviors.MagicRangedAttackBehavior;
import view.sprites.Sprite;

public class SuccubusEnemy extends AltarEnemy {
    private static final Sprite SPRITE = makeDemonSprite("succubus", 0x5F);

    public SuccubusEnemy(char a) {
        super(a, "Succubus");
        setAttackBehavior(new MagicRangedAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 9;
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

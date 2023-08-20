package model.enemies;

import model.enemies.behaviors.MagicMeleeAttackBehavior;
import view.sprites.Sprite;

public class DaemonEnemy extends AltarEnemy {
    private static final Sprite SPRITE = makeDemonSprite("daemon", 0x5D);

    public DaemonEnemy(char a) {
        super(a, "Daemon");
        setAttackBehavior(new MagicMeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 7;
    }
}

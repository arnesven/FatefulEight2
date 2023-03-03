package model.enemies;

import view.sprites.Sprite;

public class DaemonEnemy extends AltarEnemy {
    private static final Sprite SPRITE = makeDemonSprite("daemon", 0x5D);

    public DaemonEnemy(char a) {
        super(a, "Daemon");
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getSpeed() {
        return 5;
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

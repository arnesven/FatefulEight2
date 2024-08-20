package model.enemies;

import model.enemies.behaviors.MagicMeleeAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class DaemonEnemy extends AltarEnemy {
    private static final Sprite SPRITE = new DaemonSprite();

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

    private static class DaemonSprite extends LoopingSprite {
        public DaemonSprite() {
            super("fiend", "enemies.png", 0x14, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.RED);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }
}

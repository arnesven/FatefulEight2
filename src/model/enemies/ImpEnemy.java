package model.enemies;

import model.enemies.behaviors.MagicRangedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class ImpEnemy extends AltarEnemy {
    private static final Sprite SPRITE = new ImpSprite();

    public ImpEnemy(char a) {
        super(a, "Imp");
        setAttackBehavior(new MagicRangedAttackBehavior());
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
        return 3;
    }

    private static class ImpSprite extends LoopingSprite {
        public ImpSprite() {
            super("fiend", "enemies.png", 0x12B, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.RED);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }
}

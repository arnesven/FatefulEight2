package model.enemies;

import model.enemies.behaviors.MagicRangedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.List;

public class SuccubusEnemy extends AltarEnemy {
    private static final Sprite SPRITE = new SuccubusSprite();

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

    private static class SuccubusSprite extends LoopingSprite {
        public SuccubusSprite() {
            super("succubus", "enemies.png", 0x120, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.RED);
            setColor4(MyColors.DARK_RED);
            setFrames(7);
        }
    }
}

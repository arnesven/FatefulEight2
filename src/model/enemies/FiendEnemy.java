package model.enemies;

import model.enemies.behaviors.MagicMeleeAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class FiendEnemy extends AltarEnemy {
    private static final Sprite SPRITE = new FiendSprite();

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

    private static class FiendSprite extends LoopingSprite {
        public FiendSprite() {
            super("fiend", "enemies.png", 0x127, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.RED);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }
}

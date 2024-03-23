package model.enemies;

import model.enemies.behaviors.MultiKnockBackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class AbnormallyLargeTrollEnemy extends TrollEnemy {
    private static final Sprite SPRITE = new AbnormallyLargeTrollSprite();

    public AbnormallyLargeTrollEnemy(char a) {
        super(a);
        setName("Large Troll");
        setAttackBehavior(new MultiKnockBackBehavior(4, 3));
    }

    @Override
    public int getMaxHP() {
        return 24;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class AbnormallyLargeTrollSprite extends LoopingSprite {
        public AbnormallyLargeTrollSprite() {
            super("abnormallylargetrollsprite", "enemies.png", 0x7C, 32, 64);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.LIGHT_GRAY);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.PEACH);
            setFrames(4);
        }
    }

    @Override
    protected int getHeight() {
        return 2;
    }
}

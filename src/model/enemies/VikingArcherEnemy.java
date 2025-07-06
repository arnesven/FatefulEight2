package model.enemies;

import model.enemies.behaviors.RangedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VikingArcherEnemy extends VikingEnemy {
    private static final Sprite SPRITE = new VikingArcherSprite();

    public VikingArcherEnemy(char a) {
        super(a, "Viking Archer");
        setAttackBehavior(new RangedAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamageReduction() {
        return 0;
    }

    private static class VikingArcherSprite extends LoopingSprite {
        public VikingArcherSprite() {
            super("vikingarcher", "enemies.png", 0x13C, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GREEN);
            setColor3(MyColors.PINK);
            setColor4(MyColors.GOLD);
        }
    }
}

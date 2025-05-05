package model.enemies;

import model.enemies.behaviors.RangedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcishNinjaStarThrowerEnemy extends OrcWarrior {
    private static final StarThrowerSprite SPRITE = new StarThrowerSprite();

    public OrcishNinjaStarThrowerEnemy(char b) {
        super(b);
        setAttackBehavior(new RangedAttackBehavior());
    }

    @Override
    public String getName() {
        return "Ninja Star Thrower";
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class StarThrowerSprite extends LoopingSprite {
        public StarThrowerSprite() {
            super("ninjastarthrower", "enemies.png", 0x5C, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GRAY);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.LIGHT_GRAY);
            setFrames(4);
        }
    }
}

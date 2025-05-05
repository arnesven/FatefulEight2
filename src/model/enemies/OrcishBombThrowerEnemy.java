package model.enemies;

import model.enemies.behaviors.MagicRangedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcishBombThrowerEnemy extends OrcWarrior {
    private static final Sprite SPRITE = new OrcishBombEnemySprite();

    public OrcishBombThrowerEnemy(char b) {
        super(b);
        setAttackBehavior(new MagicRangedAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getName() {
        return "Ninja Bomb Thrower";
    }

    @Override
    public int getDamage() {
        return 3;
    }

    private static class OrcishBombEnemySprite extends LoopingSprite {
        public OrcishBombEnemySprite() {
            super("ninjabombthrower", "enemies.png", 0x5C, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GRAY);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.GOLD);
            setFrames(4);
        }
    }
}

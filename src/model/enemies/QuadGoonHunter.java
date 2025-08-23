package model.enemies;

import model.enemies.behaviors.MultiRangedAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonHunter extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonHunter(char e) {
        super(e, "Hunter", new MultiRangedAttackBehavior(2));
        sprite = new HunterSprite(Race.randomRace().getColor());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getDamageReduction() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    private class HunterSprite extends LoopingSprite {
        public HunterSprite(MyColors skinColor) {
            super("hunter", "enemies.png", 0x08, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(skinColor);
            setColor4(MyColors.BROWN);
            setFrames(4);
        }
    }
}

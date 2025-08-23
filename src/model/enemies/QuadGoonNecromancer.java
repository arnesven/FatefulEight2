package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;
import model.enemies.behaviors.SummonSkeletonSpellAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonNecromancer extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonNecromancer(char e) {
        super(e, "Necromancer", new SummonSkeletonSpellAttackBehavior());
        this.sprite = new NecromancerSprite(Race.randomRace().getColor());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    private static class NecromancerSprite extends LoopingSprite {
        public NecromancerSprite(MyColors skinColor) {
            super("soldier", "enemies.png", 0x1C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(skinColor);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }
}

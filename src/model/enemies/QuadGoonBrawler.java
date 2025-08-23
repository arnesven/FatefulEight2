package model.enemies;

import model.enemies.behaviors.BleedAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonBrawler extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonBrawler(char group) {
        super(group, "Brawler", new BleedAttackBehavior(8));
        sprite = new BrawlerSprite(Race.randomRace().getColor());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    private static class BrawlerSprite extends LoopingSprite {
        public BrawlerSprite(MyColors skinColor) {
            super("brawler", "enemies.png", 0x150, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(skinColor);
            setColor4(MyColors.LIGHT_GRAY);
        }
    }
}

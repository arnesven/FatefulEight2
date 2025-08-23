package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;
import model.enemies.behaviors.MultiAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.SoldierSprite;
import view.sprites.Sprite;

public class QuadGoonFencer extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonFencer(char e) {
        super(e, "Fencer", new MultiAttackBehavior(2));
        sprite = new SoldierSprite(MyColors.DARK_PURPLE, Race.randomRace().getColor());
    }

    @Override
    public int getDamageReduction() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getSpeed() {
        return 5;
    }
}

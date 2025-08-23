package model.enemies;

import model.enemies.behaviors.KnockBackAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonJuggernaught extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonJuggernaught(char d) {
        super(d, "Juggernaught", new KnockBackAttackBehavior(6, 1));
        this.sprite = new JuggernaughtSprite(Race.randomRace().getColor());
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
        return 18;
    }

    @Override
    public int getDamageReduction() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    private static class JuggernaughtSprite extends LoopingSprite {
        public JuggernaughtSprite(MyColors skinColor) {
            super("juggernaught", "enemies.png", 0x14A, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(skinColor);
            setColor4(MyColors.LIGHT_GRAY);
        }
    }
}

package model.enemies;

import model.enemies.behaviors.IllusionSpellAttackBehavior;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonMesmer extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonMesmer(char c) {
        super(c, "Mesmer", new IllusionSpellAttackBehavior());
        Race r = Race.randomRace();
        sprite = new MesmerSprite(r.getColor(), r.getRandomHairColor(true));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    private class MesmerSprite extends LoopingSprite {
        public MesmerSprite(MyColors skinColor, MyColors hairColor) {
            super("mesmer", "enemies.png", 0x146, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(skinColor);
            setColor4(hairColor);
        }
    }
}

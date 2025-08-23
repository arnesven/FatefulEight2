package model.enemies;

import model.enemies.behaviors.RestorationSpellAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonMedic extends QuadGoonEnemy {
    private Sprite sprite;

    public QuadGoonMedic(char a) {
        super(a, "Medic", new RestorationSpellAttackBehavior());
        this.sprite = new MedicSprite(Race.randomRace());
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
        return 3;
    }

    private static class MedicSprite extends LoopingSprite {
        public MedicSprite(Race r) {
            super("imolator", "enemies.png", 0x9C, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor4(MyColors.WHITE);
            setColor3(r.getColor());
        }
    }
}

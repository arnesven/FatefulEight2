package model.enemies;

import model.enemies.behaviors.IncreasedCriticalMeleeAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcishNinjaEnemy extends OrcWarrior {
    private static final Sprite SPRITE = new OrcishNinjaSprite();

    public OrcishNinjaEnemy(char a) {
        super(a);
        setAttackBehavior(new IncreasedCriticalMeleeAttackBehavior(1));
    }

    @Override
    public String getName() {
        return "Ninja Blade Wielder";
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class OrcishNinjaSprite extends LoopingSprite {
        public OrcishNinjaSprite() {
            super("orcishninja", "enemies.png", 0x36, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GRAY);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.GRAY);
            setFrames(4);
        }
    }
}

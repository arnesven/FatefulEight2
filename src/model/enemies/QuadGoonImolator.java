package model.enemies;

import model.enemies.behaviors.DestructionSpellAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class QuadGoonImolator extends QuadGoonEnemy {
    private final ImoatorSprite sprite;

    public QuadGoonImolator(char a) {
        super(a, "Imolator", new DestructionSpellAttackBehavior());
        this.sprite = new ImoatorSprite(Race.randomRace());
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
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 9;
    }

    private static class ImoatorSprite extends LoopingSprite {
        public ImoatorSprite(Race r) {
            super("imolator", "enemies.png", 0x8C, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(r.getColor());
        }
    }
}

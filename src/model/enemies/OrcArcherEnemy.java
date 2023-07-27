package model.enemies;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcArcherEnemy extends OrcWarrior {
    private static final Sprite SPRITE = new OrcArcherSprite();

    public OrcArcherEnemy(char c) {
        super(c);
        setName("Orc Archer");
    }

    @Override
    protected int getFightingStyle() {
        return FIGHTING_STYLE_RANGED;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class OrcArcherSprite extends LoopingSprite {
        public OrcArcherSprite() {
            super("orcarcher", "enemies.png", 0xC0, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.TAN);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.BROWN);
            setFrames(4);
        }
    }
}

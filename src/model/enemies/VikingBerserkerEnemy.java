package model.enemies;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VikingBerserkerEnemy extends VikingEnemy {
    private static final Sprite SPRITE = new BerserkerSprite();

    public VikingBerserkerEnemy(char a) {
        super(a, "Viking Berserker");
    }

    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class BerserkerSprite extends LoopingSprite {
        public BerserkerSprite() {
            super("vikingberserker", "enemies.png", 0x140, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.PINK);
            setColor4(MyColors.GOLD);
        }
    }
}

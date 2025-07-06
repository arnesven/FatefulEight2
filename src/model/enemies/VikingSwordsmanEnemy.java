package model.enemies;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VikingSwordsmanEnemy extends VikingEnemy {
    private static final Sprite SPRITE = new SwordsmanSprite();

    public VikingSwordsmanEnemy(char b) {
        super(b, "Viking Swordsman");
    }

    @Override
    public int getSpeed() {
        return super.getSpeed() + 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class SwordsmanSprite extends LoopingSprite {
        public SwordsmanSprite() {
            super("vikingswordsman", "enemies.png", 0x138, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_BLUE);
            setColor3(MyColors.PINK);
            setColor4(MyColors.GOLD);
        }
    }
}

package model.enemies;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcChieftain extends OrcWarrior {
    private static final Sprite SPRITE = new OrcChiefSprite();

    public OrcChieftain(char c) {
        super(c);
        setName("Orc Chieftain");
    }

    @Override
    public int getMaxHP() {
        return 9;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    private static class OrcChiefSprite extends LoopingSprite {
        public OrcChiefSprite() {
            super("mugger", "enemies.png", 0x3C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_BROWN);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.GOLD);
            setFrames(4);
        }
    }
}

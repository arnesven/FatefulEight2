package model.enemies;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VikingAxeWielderEnemy extends VikingEnemy {
    private static final Sprite SPRITE = new AxemanSprite();

    public VikingAxeWielderEnemy(char b) {
        super(b, "Viking Axe Wielder");
    }

    @Override
    public int getSpeed() {
        return super.getSpeed() - 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamageReduction() {
        return super.getDamage() + 1;
    }

    private static class AxemanSprite extends LoopingSprite {
        public AxemanSprite() {
            super("vikingaxeman", "enemies.png", 0x134, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_RED);
            setColor3(MyColors.PINK);
            setColor4(MyColors.GOLD);
        }
    }
}

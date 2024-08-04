package model.enemies;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SmallScorpionEnemy extends ScorpionEnemy {

    private static final Sprite SPRITE = new Sprite32x32("scorpion", "enemies.png", 0x6F,
            MyColors.BLACK, MyColors.YELLOW, MyColors.GOLD, MyColors.RED);

    public SmallScorpionEnemy(char a) {
        super(a);
        setName("Small Scorpion");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getDamage() {
        return 2;
    }
}

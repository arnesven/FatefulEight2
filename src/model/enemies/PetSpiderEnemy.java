package model.enemies;

import view.MyColors;
import view.sprites.Sprite;

public class PetSpiderEnemy extends SpiderEnemy {
    private static final Sprite SPRITE = new SpiderSprite(MyColors.BLACK, MyColors.GOLD,
            MyColors.WHITE, MyColors.ORANGE);

    public PetSpiderEnemy(char a) {
        super(a);
        setName("Pet Spider");
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public SpiderEnemy copy() {
        return new PetSpiderEnemy('A');
    }
}

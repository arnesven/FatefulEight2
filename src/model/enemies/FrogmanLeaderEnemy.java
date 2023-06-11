package model.enemies;

import view.sprites.FrogManSprite;
import view.sprites.Sprite;

public class FrogmanLeaderEnemy extends FrogmanEnemy {
    private static final Sprite SPRITE = new FrogManSprite(0xC8);

    public FrogmanLeaderEnemy(char a) {
        super(a, "Frogman Leader");
    }

    @Override
    public int getMaxHP() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

package model.enemies;

import view.sprites.FrogManSprite;
import view.sprites.Sprite;

public class FrogmanScoutEnemy extends FrogmanEnemy {
    private static final Sprite SPRITE = new FrogManSprite(0xC4);

    public FrogmanScoutEnemy(char a) {
        super(a, "Frogman Scout");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    protected int getFightingStyle() {
        return Enemy.FIGHTING_STYLE_RANGED;
    }
}

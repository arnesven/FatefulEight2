package model.enemies;

import view.sprites.FrogManSprite;
import view.sprites.Sprite;

public class FrogmanShamanEnemy extends FrogmanEnemy {
    private static final Sprite SPRITE = new FrogManSprite(0xCC);

    public FrogmanShamanEnemy(char b) {
        super(b, "Frogman Shaman");
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    protected int getFightingStyle() {
        return FIGHTING_STYLE_MIXED;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

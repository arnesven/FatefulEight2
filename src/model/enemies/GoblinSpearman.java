package model.enemies;

import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinSpearman extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0x84);

    public GoblinSpearman(char a) {
        super(a, "Goblin Spearman");
    }

    @Override
    public int getSpeed() {
        return 7;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }
}

package model.enemies;

import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinAxeWielder extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0x88);

    public GoblinAxeWielder(char b) {
        super(b, "Goblin Axe Wielder");
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

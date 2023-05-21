package model.enemies;

import view.MyColors;
import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinClubWielder extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinSprite(0xA7, MyColors.BROWN);

    public GoblinClubWielder(char a) {
        super(a, "Goblin Club Wielder");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinClubWielder(getEnemyGroup());
    }
}

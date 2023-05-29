package model.enemies;

import view.MyColors;
import view.sprites.GoblinSprite;
import view.sprites.Sprite;

public class GoblinBowman extends GoblinEnemy{
    private static final Sprite SPRITE = new GoblinSprite(0xAB, MyColors.BROWN);

    public GoblinBowman(char a) {
        super(a, "Goblin Bowman");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isRanged() {
        return true;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinBowman(getEnemyGroup());
    }
}

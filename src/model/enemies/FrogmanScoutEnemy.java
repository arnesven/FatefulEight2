package model.enemies;

import model.enemies.behaviors.RangedAttackBehavior;
import view.sprites.FrogManSprite;
import view.sprites.Sprite;

public class FrogmanScoutEnemy extends FrogmanEnemy {
    private static final Sprite SPRITE = new FrogManSprite(0xC4);

    public FrogmanScoutEnemy(char a) {
        super(a, "Frogman Scout", new RangedAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 2;
    }
}

package model.enemies;

import model.enemies.behaviors.RangedAttackBehavior;
import model.races.Race;
import view.sprites.BanditSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class BanditArcherEnemy extends BanditEnemy {

    private static final LoopingSprite humanSprite = new BanditSprite(Race.NORTHERN_HUMAN.getColor(), 0xC0);
    private static final LoopingSprite halfOrcSprite = new BanditSprite(Race.HALF_ORC.getColor(), 0xC0);

    public BanditArcherEnemy(char a) {
        super(a, "Bandit Archer", 4);
        setAttackBehavior(new RangedAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        if (getRace() == Race.NORTHERN_HUMAN) {
            return humanSprite;
        } else {
            return halfOrcSprite;
        }
    }
}

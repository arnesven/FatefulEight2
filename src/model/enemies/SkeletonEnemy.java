package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.combat.StandardCombatLoot;
import view.sprites.LoopingSprite;
import view.sprites.SkeletonEnemySprite;
import view.sprites.Sprite;

public class SkeletonEnemy extends UndeadEnemy {
    private static final LoopingSprite skeletonSprite = new SkeletonEnemySprite();

    public SkeletonEnemy(char a) {
        super(a, "Skeleton");
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return skeletonSprite;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }
}

package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.BleedAttackBehavior;
import view.sprites.BatSprite;
import view.sprites.Sprite;

public class BatEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new BatSprite();

    public BatEnemy(char enemyGroup) {
        super(enemyGroup, "Bat", NORMAL, new BleedAttackBehavior(1));
    }

    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

}

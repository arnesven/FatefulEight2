package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.RationsCombatLoot;
import model.enemies.behaviors.PoisonAttackBehavior;
import view.sprites.Sprite;
import view.sprites.ViperSprite;

public class ViperEnemy extends BeastEnemy {
    private static Sprite sprite = new ViperSprite("viper", "enemies.png", 0x00);

    public ViperEnemy(char enemyGroup) {
        super(enemyGroup, "Viper", HOSTILE, new PoisonAttackBehavior(1));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getMaxHP() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(1);
    }

}

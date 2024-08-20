package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.RationsCombatLoot;
import model.enemies.behaviors.KnockDownAttackBehavior;
import view.sprites.BearSprite;
import view.sprites.Sprite;

public class BearEnemy extends BeastEnemy {

    private static Sprite sprite = new BearSprite("bear", "enemies.png", 0x10);

    public BearEnemy(char group) {
        super(group, "Bear", NORMAL, new KnockDownAttackBehavior(5));
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(10);
    }

}

package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.RationsCombatLoot;
import model.enemies.behaviors.KnockDownAttackBehavior;
import model.enemies.behaviors.MeleeAttackBehavior;
import view.sprites.BearSprite;
import view.sprites.Sprite;

import java.awt.*;

public class BearEnemy extends BigBeastEnemy {

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

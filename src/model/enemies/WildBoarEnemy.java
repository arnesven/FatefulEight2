package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.RationsCombatLoot;
import model.enemies.behaviors.KnockBackAttackBehavior;
import view.sprites.Sprite;
import view.sprites.WildBoarSprite;

public class WildBoarEnemy extends BigBeastEnemy {
    private static Sprite sprite = new WildBoarSprite("wildboar", "enemies.png", 0x20);

    public WildBoarEnemy(char a) {
        super(a, "Wild Boar", NORMAL, new KnockBackAttackBehavior(4));
    }

    @Override
    public int getMaxHP() {
        return 7;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(5);
    }
}

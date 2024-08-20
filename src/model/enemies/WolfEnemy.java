package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.BleedAttackBehavior;
import view.sprites.Sprite;
import view.sprites.WolfSprite;

public class WolfEnemy extends BeastEnemy {
    private static Sprite sprite = new WolfSprite("wolf", "enemies.png", 0x30);
    private final int health;
    private final int damage;

    public WolfEnemy(char a, int health, int damage) {
        super(a, "Wolf", HOSTILE, new BleedAttackBehavior(3));
        this.health = health;
        this.damage = damage;
        this.setCurrentHp(getMaxHP());
    }

    public WolfEnemy(char a) {
        this(a, 4, 3);
    }

    @Override
    public int getMaxHP() {
        return health;
    }

    @Override
    public int getSpeed() {
        return 7;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}

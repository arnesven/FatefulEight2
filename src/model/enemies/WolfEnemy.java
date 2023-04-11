package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.combat.RandomMoneyCombatLoot;
import model.combat.RationsCombatLoot;
import view.sprites.Sprite;
import view.sprites.WildBoarSprite;
import view.sprites.WolfSprite;

public class WolfEnemy extends BigEnemy {
    private static Sprite sprite = new WolfSprite("wolf", "enemies.png", 0x30);
    private final int health;
    private final int damage;

    public WolfEnemy(char a, int health, int damage) {
        super(a, "Wolf");
        this.health = health; // TODO: This wolf gets 0 health for some reason
        this.damage = damage;
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

    @Override
    public int getWidth() {
        return 2;
    }
}

package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.horses.HorseHandler;
import view.sprites.RidingSprite;
import view.sprites.Sprite;

public class MountedEnemy extends Enemy {
    private final Enemy inner;
    private final RidingSprite sprite;

    public MountedEnemy(Enemy innerEnemy) {
        super(innerEnemy.getEnemyGroup(), "Bandit", new MeleeAttackBehavior());
        this.inner = innerEnemy;
        this.sprite = new RidingSprite(inner, HorseHandler.generateHorse());
        setCurrentHp(innerEnemy.getMaxHP());
    }

    @Override
    public int getMaxHP() {
        return inner.getMaxHP();
    }

    @Override
    public int getSpeed() {
        return inner.getSpeed() + 4;
    }

    @Override
    public String getDeathSound() {
        return inner.getDeathSound();
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return inner.getDamage();
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    @Override
    protected int getHeight() {
        return 2;
    }
}

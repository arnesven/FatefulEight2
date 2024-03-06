package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.RangedAttackBehavior;
import model.races.Race;
import view.sprites.ElfEnemySprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class ElfEnemy extends HumanoidEnemy {
    private static final LoopingSprite elfSprite = new ElfEnemySprite(Race.WOOD_ELF.getColor());

    public ElfEnemy(char a) {
        super(a, "Elf", new RangedAttackBehavior());
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
        return elfSprite;
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

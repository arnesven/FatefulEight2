package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;

public class ElvenGladiatorEnemy extends ElfEnemy {
    public ElvenGladiatorEnemy(char a) {
        super(a);
        setName("Elven Gladiator");
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    public int getMaxHP() {
        return 8;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}

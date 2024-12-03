package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.DungeonInmateLoot;

public class DungeonInmateEnemy extends ThrallEnemy {
    public DungeonInmateEnemy(char b) {
        super(b);
        setName("Prisoner");
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new DungeonInmateLoot();
    }
}

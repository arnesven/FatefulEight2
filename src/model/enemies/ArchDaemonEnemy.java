package model.enemies;

import model.Model;
import model.combat.BossCombatLoot;
import model.combat.CombatLoot;


public class ArchDaemonEnemy extends DaemonEnemy {
    public ArchDaemonEnemy(char a) {
        super(a);
    }

    @Override
    public String getName() {
        return "Archdaemon";
    }

    @Override
    public int getMaxHP() {
        return 24;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }
}

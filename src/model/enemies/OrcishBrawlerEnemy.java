package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;

public class OrcishBrawlerEnemy extends OrcWarrior {
    public OrcishBrawlerEnemy(char a) {
        super(a);
        setName("Orcish Brawler");
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    public int getMaxHP() {
        return 13;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}

package model.enemies;


import model.Model;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;

public class HumanVeteranEnemy extends SoldierEnemy {
    public HumanVeteranEnemy(char a) {
        super(a);
        setName("Human Veteran");
    }

    @Override
    public int getMaxHP() {
        return 11;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}

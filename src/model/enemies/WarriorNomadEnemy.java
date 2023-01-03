package model.enemies;

public class WarriorNomadEnemy extends NomadEnemy {
    public WarriorNomadEnemy(char a) {
        super(a, "Nomad Warrior");
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    public int getDamage() {
        return 5;
    }
}

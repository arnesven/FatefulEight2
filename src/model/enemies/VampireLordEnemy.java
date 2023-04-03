package model.enemies;

public class VampireLordEnemy extends VampireEnemy {
    public VampireLordEnemy(char a) {
        super(a);
    }

    @Override
    public String getName() {
        return "Vampire Lord";
    }

    @Override
    public int getMaxHP() {
        return 28;
    }
}

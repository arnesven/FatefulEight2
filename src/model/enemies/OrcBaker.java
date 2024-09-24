package model.enemies;

public class OrcBaker extends OrcWarrior {
    public OrcBaker(char a) {
        super(a);
    }

    @Override
    public String getName() {
        return "Orc Baker";
    }

    @Override
    public int getDamage() {
        return 3;
    }
}

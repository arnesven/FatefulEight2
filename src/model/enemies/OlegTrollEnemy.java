package model.enemies;

public class OlegTrollEnemy extends TrollEnemy {
    public OlegTrollEnemy(char a) {
        super(a);
        setName("Oleg");
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    public int getMaxHP() {
        return 18;
    }
}

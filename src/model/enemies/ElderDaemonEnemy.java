package model.enemies;

public class ElderDaemonEnemy extends DaemonEnemy {
    public ElderDaemonEnemy(char a) {
        super(a);
        setName("Elder Daemon");
    }

    @Override
    public int getMaxHP() {
        return 24;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    public int getDamage() {
        return 6;
    }
}

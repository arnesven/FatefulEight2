package model.enemies;

public class CultistLeaderEnemy extends CultistEnemy {
    public CultistLeaderEnemy(char a) {
        super(a);
        setName("Cult Leader");
    }

    @Override
    public int getDamage() {
        return 6;
    }
}

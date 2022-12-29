package model.enemies;

public class MansionGuardEnemy extends BanditEnemy {
    public MansionGuardEnemy(char a) {
        super(a);
    }

    @Override
    public String getName() {
        return "Guard";
    }

    @Override
    public int getMaxHP() {
        return 4;
    }
}

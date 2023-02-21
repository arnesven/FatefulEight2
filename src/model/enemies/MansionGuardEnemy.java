package model.enemies;

public class MansionGuardEnemy extends BanditEnemy {
    public MansionGuardEnemy(char a) {
        super(a, "Guard", 4);
    }

    @Override
    public String getName() {
        return "Guard";
    }

}

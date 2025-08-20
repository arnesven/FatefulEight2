package model.enemies;

public class ToughConstableEnemy extends ConstableEnemy {
    public ToughConstableEnemy(char a) {
        super(a);
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }
}

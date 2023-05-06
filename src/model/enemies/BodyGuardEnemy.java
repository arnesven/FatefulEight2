package model.enemies;

public class BodyGuardEnemy extends MuggerEnemy{

    public BodyGuardEnemy(char enemyGroup) {
        super(enemyGroup);
        setName("Bodyguard");
    }

    @Override
    public int getMaxHP() {
        return 7;
    }

    @Override
    public int getDamage() {
        return 3;
    }


}

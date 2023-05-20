package model.enemies;

public class PetSpiderEnemy extends SpiderEnemy {
    public PetSpiderEnemy(char a) {
        super(a);
        setName("Pet Spider");
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    public int getDamage() {
        return 4;
    }
}

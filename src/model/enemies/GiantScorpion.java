package model.enemies;

public class GiantScorpion extends ScorpionEnemy {
    public GiantScorpion(char a) {
        super(a);
    }

    @Override
    public String getName() {
        return "Giant Scorpion";
    }

    @Override
    public int getPhysicalDamageReduction() {
        return 1;
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 4;
    }
}

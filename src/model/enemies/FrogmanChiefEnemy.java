package model.enemies;

public class FrogmanChiefEnemy extends FrogmanLeaderEnemy{
    public FrogmanChiefEnemy(char c) {
        super(c);
        setName("Frogman Chieftain");
    }

    @Override
    public int getMaxHP() {
        return 8;
    }
}

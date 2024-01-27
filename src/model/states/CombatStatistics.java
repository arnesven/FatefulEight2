package model.states;

public class CombatStatistics {

    private int fledEnemies = 0;
    private int killedEnemies = 0;

    public int getKilledEnemies() {
        return killedEnemies;
    }

    public int getFledEnemies() {
        return fledEnemies;
    }

    public void increaseFledEnemies() {
        fledEnemies++;
    }

    public void increaseKilledEnemies() {
        killedEnemies++;
    }
}

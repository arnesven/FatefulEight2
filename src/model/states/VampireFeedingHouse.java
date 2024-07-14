package model.states;

import util.MyRandom;

public class VampireFeedingHouse {
    private final int stories;
    private final int dwellers;
    private final int sleeping;
    private final int lockDifficulty;

    public VampireFeedingHouse() {
        this.stories = MyRandom.randInt(1, 3);
        this.dwellers = MyRandom.randInt(1, 4);
        this.sleeping = MyRandom.randInt(dwellers);
        this.lockDifficulty = MyRandom.randInt(6, 8);
    }

    public int getStories() {
        return stories;
    }

    public int getDwellers() {
        return dwellers;
    }

    public int getSleeping() {
        return sleeping;
    }

    public int getLockDifficulty() {
        return lockDifficulty;
    }
}

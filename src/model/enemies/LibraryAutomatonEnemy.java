package model.enemies;

import view.MyColors;

public class LibraryAutomatonEnemy extends AutomatonEnemy {
    public LibraryAutomatonEnemy(char a, MyColors color) {
        super(a, color);
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 4;
    }
}

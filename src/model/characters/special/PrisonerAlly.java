package model.characters.special;

import model.enemies.Enemy;

public class PrisonerAlly extends AllyFromEnemyCharacter {
    public PrisonerAlly(Enemy enemy) {
        super(enemy);
    }


    @Override
    public int getMaxHP() {
        return 4;
    }
}

package model.enemies;

import model.enemies.behaviors.DeadlyStrikeBehavior;
import view.MyColors;

public class RiverTrollSpearerEnemy extends RiverTrollEnemy {
    public RiverTrollSpearerEnemy(char a) {
        super(a, "Spearer", 0x104, MyColors.BROWN);
        setAttackBehavior(new DeadlyStrikeBehavior(1.5));
    }
}

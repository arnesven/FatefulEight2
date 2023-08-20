package model.enemies;

import model.enemies.behaviors.MultiMagicAttackBehavior;

public class DoomMageEnemy extends RedMageEnemy {
    public DoomMageEnemy(char a) {
        super(a);
        setAttackBehavior(new MultiMagicAttackBehavior(3));
    }

    @Override
    public String getName() {
        return "Doom Mage";
    }

    @Override
    public int getMaxHP() {
        return 16;
    }
}

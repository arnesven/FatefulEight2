package model.enemies;

public class DoomMageEnemy extends RedMageEnemy {
    public DoomMageEnemy(char a) {
        super(a);
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

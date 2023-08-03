package model.enemies;

public class QuadMinionEnemy extends CultistEnemy {
    public QuadMinionEnemy(char a) {
        super(a);
        setName("Quad Minion");
    }

    @Override
    protected int getFightingStyle() {
        return FIGHTING_STYLE_MELEE;
    }
}

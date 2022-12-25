package model.quests.scenes;

import model.enemies.SkeletonEnemy;

import java.util.List;

public class VampireCombatSubScene extends CombatSubScene {
    public VampireCombatSubScene(int col, int row) {
        super(col, row, List.of(new SkeletonEnemy('A')));
    }

    @Override
    protected String getCombatDetails() {
        return "Vampire Guardian";
    }
}

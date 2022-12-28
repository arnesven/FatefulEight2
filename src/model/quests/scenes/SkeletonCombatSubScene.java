package model.quests.scenes;

import model.enemies.SkeletonEnemy;
import java.util.List;

public class SkeletonCombatSubScene extends CombatSubScene {
    public SkeletonCombatSubScene(int col, int row) {
        super(col, row, List.of(
//                new SkeletonEnemy('A'),
//                new SkeletonEnemy('A'),
//                new SkeletonEnemy('A'),
                new SkeletonEnemy('A')));
    }

    @Override
    protected String getCombatDetails() {
        return "4 Skeletons";
    }


}

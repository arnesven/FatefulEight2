package model.states.mine;

import model.SteppingMatrix;
import model.enemies.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class SpiderInfestedMine extends LogicalMine {

    private static final List<SpiderEnemy> SPIDER_TYPES = List.of(
            new CommonSpiderEnemy('A'), new ForestSpiderEnemy('A'), new CaveSpiderEnemy('A'), new GiantSpiderEnemy('A'));

    public SpiderInfestedMine(boolean enteredFromSurface) {
        super(enteredFromSurface);
    }

    @Override
    public void addNPCs(SteppingMatrix<MineObject> matrix, int level) {
        if (MyRandom.randInt(3) == 0) {
            return;
        }
        List<Enemy> enemies = new ArrayList<>();
        int groupSize = MyRandom.randInt(level,  2 + level / 2);
        for (int i = 0; i < groupSize; ++i) {
            int typeIndex = Math.min(level / 4, 3);
            enemies.add(SPIDER_TYPES.get(typeIndex).copy());
        }
        placeRandomly(matrix, new EnemyMineObject(enemies, MyRandom.flipCoin()));
    }
}

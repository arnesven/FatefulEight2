package model.states.mine;

import model.SteppingMatrix;
import model.enemies.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class GoblinInfestedMine extends LogicalMine {
    private static final int[] MIN_GROUPS = new int[]{1, 0, 0, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] MAX_GROUPS = new int[]{1, 1, 1, 1, 1, 2, 2, 3, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0};

    public GoblinInfestedMine(boolean enteredFromSurface) {
        super(enteredFromSurface);
    }

    @Override
    public void addNPCs(SteppingMatrix<MineObject> matrix, int level) {
        for (int i = 0; i < groupsToPlace(level); ++i) {
            super.placeRandomly(matrix, makeGoblinGroupForLevel(level));
        }
    }

    private int groupsToPlace(int level) {
        if (level >= MIN_GROUPS.length) {
            return 0;
        }
        return MyRandom.randInt(MIN_GROUPS[level-1], MAX_GROUPS[level-1]);
    }

    private EnemyMineObject makeGoblinGroupForLevel(int level) {
        return new EnemyMineObject(getGoblinGroupForLevel(level), MyRandom.flipCoin());
    }

    private List<Enemy> getGoblinGroupForLevel(int level) {
        int groupSize = MyRandom.randInt(level, level+3);
        List<Enemy> enemies = new ArrayList<>();
        int dieRoll = MyRandom.rollD6();
        List<GoblinEnemy> templates = List.of(new GoblinAxeWielder('A'), new GoblinSpearman('E'), new GoblinBowman('B'),
                new GoblinSwordsman('D'), new GoblinClubWielder('C'));
        for (int i = 0; i < groupSize; ++i) {
            if (dieRoll == 6) {
                enemies.add(MyRandom.sample(templates).copy());
            } else {
                enemies.add(templates.get(dieRoll - 1).copy());
            }
        }
        if (MyRandom.randInt(50) < level) {
            enemies.add(enemies.size()/2, new GoblinKingEnemy('F'));
        }
        return enemies;
    }
}

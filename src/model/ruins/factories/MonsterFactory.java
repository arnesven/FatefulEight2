package model.ruins.factories;

import model.Model;
import model.enemies.*;
import model.ruins.objects.DungeonMonster;
import model.ruins.objects.DungeonObject;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class MonsterFactory implements Serializable {

    private static final double SLEEP_CHANCE = 0.6667;

    public final DungeonObject makeRandomEnemies(Model model, Random random) {
        DungeonMonster monster = spawnMonster(model, random);
        if (random.nextDouble() < SLEEP_CHANCE) {
            monster.setSleeping(true);
        }
        return monster;
    }

    protected DungeonMonster spawnMonster(Model model, Random random) {
        int dieRoll = random.nextInt(13);
        switch (dieRoll) {
            case 0:
                return new DungeonMonster(List.of(new SnowyBeastEnemy('A'), new SnowyBeastEnemy('A')));
            case 1:
                return new DungeonMonster(List.of(new GoblinAxeWielder('A'), new GoblinAxeWielder('A'),
                        new GoblinSpearman('B'), new GoblinSpearman('B'), new GoblinSwordsman('C'),
                        new GoblinSwordsman('C')));
            case 2:
                return new DungeonMonster(List.of(new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'),
                        new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A')));
            case 3:
                return new DungeonMonster(List.of(new TrollEnemy('A'), new OrcWarrior('B'), new OrcWarrior('B')));
            case 4:
                return new DungeonMonster(List.of(new FiendEnemy('A'), new SuccubusEnemy('B'), new SuccubusEnemy('B')));
            case 5:
                return new DungeonMonster(List.of(new ManticoreEnemy('A'), new ManticoreEnemy('A')));
            case 6:
                return new DungeonMonster(List.of(new DaemonEnemy('A'), new ImpEnemy('B'), new ImpEnemy('B')));
            case 7:
                return new DungeonMonster(List.of(new LizardmanEnemy('A'), new LizardmanEnemy('A'), new CrocodileEnemy('B'), new CrocodileEnemy('B')));
            case 8:
                return new DungeonMonster(List.of(new SpiderEnemy('A'), new SpiderEnemy('A'), new SpiderEnemy('A'), new ScorpionEnemy('B')));
            case 9:
                return new DungeonMonster(List.of(new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'),
                        new GiantRatEnemy('B'), new GiantRatEnemy('B'), new GiantRatEnemy('B')));
            case 10:
                return new DungeonMonster(List.of(new AutomatonEnemy('A'), new AutomatonEnemy('A')));
            case 11:
                return new DungeonMonster(List.of(new GhostEnemy('A'), new GhostEnemy('A'), new GhostEnemy('A')));
            default:
                return new DungeonMonster(List.of(new SkeletonEnemy('A'), new SkeletonEnemy('A'), new SkeletonEnemy('A')));
        }
    }

}

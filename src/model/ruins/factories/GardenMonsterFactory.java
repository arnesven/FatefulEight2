package model.ruins.factories;

import model.Model;
import model.enemies.*;
import model.ruins.objects.DungeonMonster;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;

import java.util.List;
import java.util.Random;

public class GardenMonsterFactory extends MonsterFactory {

    @Override
    protected DungeonMonster spawnMonster(Random random) {
        int dieRoll = random.nextInt(8);
        switch (dieRoll) {
            case 0:
                return new DungeonMonster(List.of(new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'),
                        new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A')));
            case 1:
                return new DungeonMonster(List.of(new WildBoarEnemy('B'), new WildBoarEnemy('B'), new WildBoarEnemy('B')));
            case 2:
                return new DungeonMonster(List.of(new SkeletonEnemy('A'), new SkeletonEnemy('A')));
            case 3:
                return new DungeonMonster(List.of(new SpiderEnemy('A'), new SpiderEnemy('A'), new SpiderEnemy('A')));
            case 4:
                return new DungeonMonster(List.of(new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'),
                        new RatEnemy('A'), new RatEnemy('A')));
            case 5:
                return new DungeonMonster(List.of(new BearEnemy('B')));
            default:
                return new FaeryDungeonMonster();
        }
    }

    private static class FaeryDungeonMonster extends DungeonMonster {
        public FaeryDungeonMonster() {
            super(List.of(new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'),
                    new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'),
                    new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A')));
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            if (DailyEventState.getPartyAlignment(model) < 0) {
                super.entryTrigger(model, exploreRuinsState);
            } else {
                exploreRuinsState.println("The faeries just ignore you.");
            }
        }
    }
}

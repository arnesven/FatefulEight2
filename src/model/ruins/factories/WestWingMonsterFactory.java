package model.ruins.factories;

import model.Model;
import model.enemies.*;
import model.ruins.objects.DungeonMonster;
import model.states.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WestWingMonsterFactory extends MonsterFactory {

    private final Model model;

    public WestWingMonsterFactory(Model model) {
        this.model = model;
    }

    protected DungeonMonster spawnMonster(Random random) {
        int dieRoll = random.nextInt(5);
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies;
        switch (dieRoll) {
            case 0:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new BatEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new BatEnemy('A'));
                }
                break;
            case 1:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new ScorpionEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new ScorpionEnemy('A'));
                }
                break;
            case 2:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new SpiderEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new SpiderEnemy('A'));
                }
                break;
            case 3:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new GiantRatEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new GiantRatEnemy('A'));
                }
                break;
            default:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new SkeletonEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new SkeletonEnemy('A'));
                }
        }
        return new DungeonMonster(enemies);
    }
}

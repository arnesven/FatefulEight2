package model.ruins.factories;

import model.Model;
import model.enemies.*;
import model.ruins.objects.DungeonMonster;
import model.states.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FatueGardenMonsterFactory extends MonsterFactory {

    @Override
    protected DungeonMonster spawnMonster(Model model, Random random) {
        int dieRoll = random.nextInt(6);
        int numberOfEnemies;
        List<Enemy> enemies = new ArrayList<>();
        switch (dieRoll) {
            case 0:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new BatEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new BatEnemy('A'));
                }
                break;
            case 1:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new RatEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new RatEnemy('A'));
                }
                break;
            case 2:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new SkeletonEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new SkeletonEnemy('A'));
                }
                break;
            case 3:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new SpiderEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new SpiderEnemy('A'));
                }
                break;
            case 4:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new ViperEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new ViperEnemy('A'));
                }
                break;
            default:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new WerewolfEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new WerewolfEnemy('A'));
                }
        }
        return new DungeonMonster(enemies);
    }
}

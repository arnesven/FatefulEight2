package model.ruins.factories;

import model.Model;
import model.enemies.*;
import model.ruins.objects.DungeonMonster;
import model.states.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UndeadMonsterFactory extends MonsterFactory {
    private final Model model;

    public UndeadMonsterFactory(Model model) {
        this.model = model;
    }

    protected DungeonMonster spawnMonster(Random random) {
        int dieRoll = random.nextInt(4);
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies;
        switch (dieRoll) {
            case 0:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new GhostEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new GhostEnemy('A'));
                }
                break;
            case 1:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new SkeletonEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new SkeletonEnemy('A'));
                }
                break;
            case 2:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new MummyEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new MummyEnemy('A'));
                }
                break;
            default:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new GhoulEnemy('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new GhoulEnemy('A'));
                }
        }
        return new DungeonMonster(enemies);
    }
}

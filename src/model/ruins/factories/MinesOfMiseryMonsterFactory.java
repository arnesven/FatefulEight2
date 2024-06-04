package model.ruins.factories;

import model.Model;
import model.enemies.*;
import model.ruins.objects.DungeonMonster;
import model.states.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesOfMiseryMonsterFactory extends MonsterFactory {
    private final Model model;

    public MinesOfMiseryMonsterFactory(Model model) {
        this.model = model;
    }

    protected DungeonMonster spawnMonster(Random random) {
        int dieRoll = random.nextInt(5);
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies;
        switch (dieRoll) {
            case 0:
                numberOfEnemies = Math.max(1, GameState.getSuggestedNumberOfEnemies(model, new GoblinSpearman('A')) - 1);
                int numberOfSpearmen = random.nextInt(numberOfEnemies);
                for (int i = 0; i < numberOfSpearmen; ++i) {
                    enemies.add(new GoblinSpearman('A'));
                }
                int numberOfAxemen = random.nextInt(numberOfEnemies - numberOfSpearmen);
                for (int i = 0; i < numberOfAxemen; ++i) {
                    enemies.add(new GoblinAxeWielder('B'));
                }
                int numberOfBowmen = random.nextInt(numberOfSpearmen - numberOfAxemen);
                for (int i = 0; i < numberOfBowmen; ++i) {
                    enemies.add(new GoblinBowman('C'));
                }
                break;
            case 1:
                numberOfEnemies = GameState.getSuggestedNumberOfEnemies(model, new GoblinClubWielder('A'));
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new GoblinClubWielder('C'));
                }
                break;
            case 2:
                numberOfEnemies = Math.max(1, GameState.getSuggestedNumberOfEnemies(model, new GoblinSwordsman('A')) - 1);
                for (int i = 0; i < numberOfEnemies; ++i) {
                    enemies.add(new GoblinSwordsman('A'));
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

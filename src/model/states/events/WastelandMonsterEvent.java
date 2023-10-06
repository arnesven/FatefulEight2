package model.states.events;

import model.Model;
import model.combat.WastelandCombatTheme;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class WastelandMonsterEvent extends DailyEventState {
    public WastelandMonsterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters some monsters roaming the wasteland!");
        List<Enemy> enemies = null;
        int roll = MyRandom.rollD10();
        switch (roll) {
            case 1:
            case 2:
                enemies = makeOrcsAndGoblins();
                break;
            case 3:
                enemies = makeLizardmen();
                break;
            case 4:
                enemies = makeUndeadEnemies();
                break;
            case 5:
                enemies = makeWolfEnemies();
                break;
            case 6:
            case 8:
                enemies = makeBanditEnemies();
                break;
            case 9:
                enemies = makeScorpionEnemies();
                break;
            case 10:
                enemies = makeTrollEnemies();
        }
        runCombat(enemies, new WastelandCombatTheme(), true);
    }

    private List<Enemy> makeTrollEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new TrollEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new TrollEnemy('A'));
        }
        return result;
    }

    private List<Enemy> makeBanditEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new BanditArcherEnemy('A'));
        for (int i = num/2; i > 0; --i) {
            result.add(new BanditArcherEnemy('A'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new BanditEnemy('B'));
        for (int i = num/2; i > 0; --i) {
            result.add(new BanditEnemy('B'));
        }
        return result;
    }

    private List<Enemy> makeWolfEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new WolfEnemy('A'));
        for (int i = num/2+1; i > 0; --i) {
            result.add(new WolfEnemy('A'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new MountainWolfEnemy('A'));
        for (int i = num/2; i > 0; --i) {
            result.add(new MountainWolfEnemy('B'));
        }
        return result;
    }

    private List<Enemy> makeUndeadEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new SkeletonEnemy('A'));
        for (int i = num/2+1; i > 0; --i) {
            result.add(new SkeletonEnemy('A'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new GhostEnemy('B'));
        for (int i = num/2; i > 0; --i) {
            result.add(new GhostEnemy('B'));
        }
        return result;
    }

    private List<Enemy> makeLizardmen() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new LizardmanEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new LizardmanEnemy('A'));
        }
        return result;
    }

    private List<Enemy> makeOrcsAndGoblins() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new GoblinBowman('A'));
        for (int i = num/3+1; i > 0; --i) {
            result.add(new GoblinBowman('B'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new GoblinSpearman('A'));
        for (int i = num/3+1; i > 0; --i) {
            result.add(new GoblinSpearman('C'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new OrcWarrior('A'));
        for (int i = num/3; i > 0; --i) {
            result.add(new OrcWarrior('A'));
        }
        return result;
    }

    private List<Enemy> makeScorpionEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new ScorpionEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new ScorpionEnemy('A'));
        }
        return result;
    }
}

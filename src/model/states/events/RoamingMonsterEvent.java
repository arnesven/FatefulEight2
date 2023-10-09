package model.states.events;

import model.Model;
import model.enemies.*;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public abstract class RoamingMonsterEvent extends DailyEventState {

    public RoamingMonsterEvent(Model model) {
        super(model);
    }

    protected List<Enemy> makeNomadEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new NomadEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new NomadEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeBearEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new BearEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new BearEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeSnowyBeasts() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new SnowyBeastEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new SnowyBeastEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeTrollEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new TrollEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new TrollEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeBanditEnemies() {
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

    protected List<Enemy> makeWolfEnemies() {
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

    protected List<Enemy> makeUndeadEnemies() {
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

    protected List<Enemy> makeLizardmen() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new LizardmanEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new LizardmanEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeOrcsAndGoblins() {
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

    protected List<Enemy> makeScorpionEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new ScorpionEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new ScorpionEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeSpiderEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new SpiderEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new SpiderEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeCrocodileEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new CrocodileEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new CrocodileEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeFrogmenEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new FrogmanScoutEnemy('A'));
        for (int i = num/3+1; i > 0; --i) {
            result.add(new FrogmanScoutEnemy('B'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new FrogmanShamanEnemy('A'));
        for (int i = num/3+1; i > 0; --i) {
            result.add(new FrogmanShamanEnemy('C'));
        }
        num = getSuggestedNumberOfEnemies(getModel(), new FrogmanLeaderEnemy('A'));
        for (int i = num/3; i > 0; --i) {
            result.add(new FrogmanLeaderEnemy('A'));
        }
        return result;
    }


    protected List<Enemy> makeViperEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new ViperEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new ViperEnemy('A'));
        }
        return result;
    }

    protected List<Enemy> makeBatEnemies() {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new BatEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new BatEnemy('A'));
        }
        return result;
    }
}

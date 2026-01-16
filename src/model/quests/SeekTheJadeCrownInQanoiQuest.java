package model.quests;

import model.Model;
import model.enemies.*;
import model.map.WorldBuilder;
import model.map.locations.QanoiPyramidLocation;
import model.states.events.QanoiPuzzleEvent;
import util.MyPair;

import java.util.List;

public class SeekTheJadeCrownInQanoiQuest extends SeekTheJadeCrownQuest {
    private MyPair<List<Integer>, List<Integer>> oldSolution;

    public SeekTheJadeCrownInQanoiQuest() {
        super(QanoiPyramidLocation.NAME, WorldBuilder.QANOI_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInQanoiQuest();
    }

    protected List<Enemy> makeJungleMonsters() {
        return List.of(new LizardmanEnemy('A'), new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                new LizardmanEnemy('A'), new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                new LizardmanEnemy('A'), new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                new LizardmanEnemy('A'));
    }

    protected List<Enemy> makePyramidDenizens() {
        return List.of(new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'),
                new OrcArcherEnemy('B'),  new OrcArcherEnemy('B'), new OrcBoarRiderEnemy('C'), new OrcBoarRiderEnemy('C'),
                new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'));
    }

    @Override
    protected void resetPuzzleState() {
        oldSolution = null;
    }

    @Override
    protected boolean isPuzzleEventSolved(Model model) {
        QanoiPuzzleEvent event;
        if (oldSolution != null) {
            event = new QanoiPuzzleEvent(model, oldSolution);
        } else {
            event = new QanoiPuzzleEvent(model);
        }
        event.doTheEvent(model);
        oldSolution = event.getPuzzleSolution();
        return event.solvedPuzzle();
    }
}

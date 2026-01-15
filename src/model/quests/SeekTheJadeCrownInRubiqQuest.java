package model.quests;

import model.Model;
import model.enemies.*;
import model.mainstory.jungletribe.RubiqBall;
import model.mainstory.jungletribe.RubiqPuzzleEvent;
import model.map.WorldBuilder;
import model.map.locations.RubiqPyramidLocation;

import java.util.List;

public class SeekTheJadeCrownInRubiqQuest extends SeekTheJadeCrownQuest {
    private List<RubiqBall> oldPuzzleState;

    public SeekTheJadeCrownInRubiqQuest() {
        super(RubiqPyramidLocation.NAME, WorldBuilder.RUBIQ_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInRubiqQuest();
    }

    @Override
    protected void resetPuzzleState() {
        oldPuzzleState = null;
    }

    protected List<Enemy> makeJungleMonsters() {
        return List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanLeaderEnemy('C'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanShamanEnemy('B'),
                new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'));
    }

    protected List<Enemy> makePyramidDenizens() {
        return List.of(new LizardmanEnemy('A'), new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                new LizardmanEnemy('A'), new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                new LizardmanEnemy('A'), new LizardmanEnemy('A'), new LizardmanEnemy('A'),
                new LizardmanEnemy('A'));
    }

    @Override
    protected boolean isPuzzleEventSolved(Model model) {
        RubiqPuzzleEvent event;
        if (oldPuzzleState != null) {
            event = new RubiqPuzzleEvent(model, oldPuzzleState);
        } else {
            event = new RubiqPuzzleEvent(model);
        }
        event.doTheEvent(model);
        oldPuzzleState = event.getPuzzleState();
        return event.solvedPuzzle();
    }
}

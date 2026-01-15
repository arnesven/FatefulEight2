package model.quests;

import model.Model;
import model.enemies.*;
import model.mainstory.jungletribe.RubiqBall;
import model.mainstory.jungletribe.RubiqPuzzleEvent;
import model.map.WorldBuilder;
import model.map.locations.SudoqPyramidLocation;

import java.util.List;

public class SeekTheJadeCrownInSudoqQuest extends SeekTheJadeCrownQuest {

    private List<RubiqBall> oldPuzzleState;

    public SeekTheJadeCrownInSudoqQuest() {
        super(SudoqPyramidLocation.NAME, WorldBuilder.SUDOQ_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInSudoqQuest();
    }

    protected List<Enemy> makeJungleMonsters() {
        return List.of(new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'),
                new OrcArcherEnemy('B'),  new OrcArcherEnemy('B'), new OrcBoarRiderEnemy('C'), new OrcBoarRiderEnemy('C'),
                new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'));
    }

    protected List<Enemy> makePyramidDenizens() {
        return List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanLeaderEnemy('C'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanShamanEnemy('B'),
                new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'));
    }

    @Override
    protected void resetPuzzleState() {
        oldPuzzleState = null;
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

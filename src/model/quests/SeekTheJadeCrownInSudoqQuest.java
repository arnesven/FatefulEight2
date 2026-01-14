package model.quests;

import model.enemies.*;
import model.map.WorldBuilder;
import model.map.locations.SudoqPyramidLocation;

import java.util.List;

public class SeekTheJadeCrownInSudoqQuest extends SeekTheJadeCrownQuest {
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
}

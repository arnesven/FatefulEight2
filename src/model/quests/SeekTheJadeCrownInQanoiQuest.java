package model.quests;

import model.enemies.*;
import model.map.WorldBuilder;
import model.map.locations.QanoiPyramidLocation;

import java.util.List;

public class SeekTheJadeCrownInQanoiQuest extends SeekTheJadeCrownQuest {
    public SeekTheJadeCrownInQanoiQuest() {
        super(QanoiPyramidLocation.NAME, WorldBuilder.QANOI_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInQanoiQuest();
    }

    protected List<Enemy> makeJungleMonsters() {
        return List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanLeaderEnemy('C'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanShamanEnemy('B'),
                new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'));
    }

    protected List<Enemy> makePyramidDenizens() {
        return List.of(new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'),
                new OrcArcherEnemy('B'),  new OrcArcherEnemy('B'), new OrcBoarRiderEnemy('C'), new OrcBoarRiderEnemy('C'),
                new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'));
    }
}

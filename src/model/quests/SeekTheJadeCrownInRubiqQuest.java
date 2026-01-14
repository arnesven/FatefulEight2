package model.quests;

import model.enemies.*;
import model.map.WorldBuilder;
import model.map.locations.RubiqPyramidLocation;

import java.util.List;

public class SeekTheJadeCrownInRubiqQuest extends SeekTheJadeCrownQuest {

    public SeekTheJadeCrownInRubiqQuest() {
        super(RubiqPyramidLocation.NAME, WorldBuilder.RUBIQ_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInRubiqQuest();
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
}

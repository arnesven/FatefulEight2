package model.quests;

import model.map.WorldBuilder;
import model.map.locations.RubiqPyramidLocation;

public class SeekTheJadeCrownInRubiqQuest extends SeekTheJadeCrownQuest {

    public SeekTheJadeCrownInRubiqQuest() {
        super(RubiqPyramidLocation.NAME, WorldBuilder.RUBIQ_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInRubiqQuest();
    }
}

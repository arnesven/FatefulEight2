package model.quests;

import model.map.WorldBuilder;
import model.map.locations.SudoqPyramidLocation;

public class SeekTheJadeCrownInSudoqQuest extends SeekTheJadeCrownQuest {
    public SeekTheJadeCrownInSudoqQuest() {
        super(SudoqPyramidLocation.NAME, WorldBuilder.SUDOQ_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInSudoqQuest();
    }
}

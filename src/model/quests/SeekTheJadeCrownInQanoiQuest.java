package model.quests;

import model.map.WorldBuilder;
import model.map.locations.QanoiPyramidLocation;

public class SeekTheJadeCrownInQanoiQuest extends SeekTheJadeCrownQuest {
    public SeekTheJadeCrownInQanoiQuest() {
        super(QanoiPyramidLocation.NAME, WorldBuilder.QANOI_PYRAMID_LOCATION);
    }

    @Override
    public MainQuest copy() {
        return new SeekTheJadeCrownInQanoiQuest();
    }
}

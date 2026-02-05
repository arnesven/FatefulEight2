package view.help;

import model.map.*;
import view.GameView;
import view.MyColors;

import java.util.List;

public class TerrainHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT = "The world is made up of many different types of terrains," +
            "climates and geographical features. Different terrain types will generate different " +
            "kind of random events.";

    public TerrainHelpChapter(GameView view) {
        super(view, "Terrain", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new CastleHelpDialog(view),
                new SpecificTerrainHelpDialog(view, new CaveHex(0, CaveHex.GROUND_COLOR, 0), false),
                new SpecificTerrainHelpDialog(view, new DesertHex(0, 0, null, 0), false),
                new SpecificTerrainHelpDialog(view, new FieldsHex(0, 0, 0), false),
                new FishingVillageHelpDialog(view),
                new InnHelpDialog(view),
                new SpecificTerrainHelpDialog(view, new JungleHex(0, 0, 0), false),
                new MonasteryHelpDialog(view),
                new SpecificTerrainHelpDialog(view, new PlainsHex(0, 0, null, 0), false),
                new RiverTerrainHelpDialog(view),
                new RoadsTerrainHelpDialog(view),
                new BrokenRoadTerrainHelpDialog(view),
                new SpecificTerrainHelpDialog(view, new SeaHex(0), false),
                new SpecificTerrainHelpDialog(view, new SwampHex(0, 0, 0), false),
                new TempleHelpDialog(view),
                new TownHelpDialog(view),
                new SpecificTerrainHelpDialog(view, new WastelandHex(0, 0, 0), false),
                new SpecificTerrainHelpDialog(view, new WoodsHex(0, 0, 0), false)
        );
    }
}

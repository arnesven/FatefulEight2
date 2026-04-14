package view.help;

import view.GameView;

import java.util.List;

public class TutorialMines extends ExpandableHelpDialog {
    private static final String[] TEXT = new String[]{"Mines are prevalent around the world. They are common in " +
            "hilly or mountainous terrain. Mines are often also connected to caves.\n\n" +
            "When your party enters a mine you will initially have 99 steps to explore it. When these steps have " +
            "been depleted some of your party members may get tired (Endurance Skill is used for this) and " +
            "need to return to the mine entrance. Those that don't will gain an additional 50 steps " +
            "and can carry on until the skill check is eventually failed.\n\n" +
            "Rocks and geodes can be mined with the Labor skill. These skill tests cannot be re-rolled with Stamina like normal, " +
            "instead a failed attempt may exhaust a point of Stamina if the character fails an Endurance test.\n\n" +
            "Mining also requires that your party has at least one item which can be used for mining (Pickaxes and Mauls)."};

    public TutorialMines(GameView view) {
        super(view, "Mines", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new ElevatorChapter(view),
                new GeodesChapter(view),
                new MineMonstersChapter(view),
                new OresChapter(view)
        );
    }

    private static class ElevatorChapter extends SubChapterHelpDialog {
        public ElevatorChapter(GameView view) {
            super(view, "Elevator", "TODO");
            // TODO
        }
    }

    private static class GeodesChapter extends SubChapterHelpDialog {
        public GeodesChapter(GameView view) {
            super(view, "Geodes", "TODO");
            // TODO
        }
    }

    private static class MineMonstersChapter extends SubChapterHelpDialog {
        private static final String MONSTERS_TEXT = "Some mines are infested with monsters and may block your way " +
                "or otherwise interfere with your mining activities.\n\n" +
                "Monsters in mines are either dormant or alert. Alert monsters will attack you as soon as you " +
                "venture into a space adjacent to it. Unless you moved straight toward the monster, that attack will " +
                "result in an Ambush.\n\n" +
                "A dormant enemy will not attack you unless you enter into its space, " +
                "and doing so will trigger a Surprise Attack on that enemy.";

        public MineMonstersChapter(GameView view) {
            super(view, "Mine Monsters", MONSTERS_TEXT);
        }
    }

    private static class OresChapter extends SubChapterHelpDialog {
        public OresChapter(GameView view) {
            super(view, "Ores", "TODO");
            // TODO
        }
    }
}

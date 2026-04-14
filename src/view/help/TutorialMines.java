package view.help;

import model.Model;
import model.states.mine.*;
import view.GameView;
import view.party.DrawableObject;

import java.awt.*;
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
        private static final String ELEVATOR_TEXT =
                "Every mine has an elevator. The elevator shaft goes straight through " +
                "the mine and is accessible and callable from every level.\n\n" +
                "The elevator can take you to any " +
                "level of the mine, but only to levels at which you have once been.";

        public ElevatorChapter(GameView view) {
            super(view, "Elevator", ELEVATOR_TEXT);
        }
    }

    private static class GeodesChapter extends SubChapterHelpDialog {
        private static final String GEODES_TEXT = "Often you will spot gemstones lodged into the rock in the mine. " +
                "Such geodes can be worked upon to try to extract a gemstone. Failing the Labor Skill check to mine " +
                "the geode will result in it breaking apart, without getting the gemstone within.\n\n" +
                "Amethysts, Topazes and Sapphires " +
                "are prevalent on the higher levels of the mine and relatively uncomplicated to dislodge.\n\n\n\n\n\n\n" +
                "The more precious and valuable Emeralds, Rubies and Diamonds can only be found " +
                "at the deeper levels and are much harder to extract from the rock.";

        private final AmethystGeodeObject amethyst;
        private final int lineOffset;
        private final EmeraldGeodeObject emerald;
        private final TopazGeodeObject topaz;
        private final SapphireGeodeObject sapphire;
        private final RubyGeodeObject ruby;
        private final DiamondGeodeObject diamond;

        public GeodesChapter(GameView view) {
            super(view, "Geodes", GEODES_TEXT);
            this.amethyst = new AmethystGeodeObject();
            this.topaz = new TopazGeodeObject();
            this.sapphire = new SapphireGeodeObject();
            this.emerald = new EmeraldGeodeObject();
            this.ruby = new RubyGeodeObject();
            this.diamond = new DiamondGeodeObject();
            this.lineOffset = 19;
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            List<DrawableObject> result = super.buildDecorations(model, xStart, yStart);
            result.add(new MineObjectDecoration(amethyst, xStart + 7, yStart + lineOffset));
            result.add(new MineObjectDecoration(topaz, xStart + 15, yStart + lineOffset));
            result.add(new MineObjectDecoration(sapphire, xStart + 24, yStart + lineOffset));
            result.add(new MineObjectDecoration(emerald, xStart + 7, yStart + lineOffset + 11));
            result.add(new MineObjectDecoration(ruby, xStart + 15, yStart + lineOffset + 11));
            result.add(new MineObjectDecoration(diamond, xStart + 24, yStart + lineOffset + 11));
            return result;
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
        private static final String ORES_TEXT = "Mines contain different kinds of rocks. " +
                "Some rocks have valuable minerals, like iron, silver and gold. Ore rocks " +
                "also have three distinct qualities, Normal, Rich and Ultra Rich. But you will have to venture " +
                "deeper into mines to find the latter two. You may attempt to mine an ore rock multiple times, " +
                "but each failed attempt may result in the loss of a Stamina Point.\n\n" +
                "Iron Ore can be mined to gain materials.\n\n\n\n\n\n\n" +
                "    Normal   Rich    Ultra Rich\n\n" +
                "Silver Ore can be mined to gain Obols.\n\n\n\n\n\n\n" +
                "Gold Ore can be mined to gain gold.\n\n\n\n\n\n\n";
        private final MaterialsOreObject[] ironOres;
        private final SilverOreObject[] silverOres;
        private final GoldOreObject[] goldOres;
        private final int lineOffset;

        public OresChapter(GameView view) {
            super(view, "Ores", ORES_TEXT);
            this.ironOres = new MaterialsOreObject[]{new MaterialsOreObject(0),
                    new MaterialsOreObject(1), new MaterialsOreObject(2)};
            this.silverOres = new SilverOreObject[]{new SilverOreObject(0),
                    new SilverOreObject(1), new SilverOreObject(2)};
            this.goldOres = new GoldOreObject[]{new GoldOreObject(0),
                    new GoldOreObject(1), new GoldOreObject(2)};
            this.lineOffset = 19;
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            List<DrawableObject> result = super.buildDecorations(model, xStart, yStart);
            result.add(new MineObjectDecoration(ironOres[0], xStart + 7, yStart + lineOffset));
            result.add(new MineObjectDecoration(ironOres[1], xStart + 15, yStart + lineOffset));
            result.add(new MineObjectDecoration(ironOres[2], xStart + 24, yStart + lineOffset));
            result.add(new MineObjectDecoration(silverOres[0], xStart + 7, yStart + lineOffset + 10));
            result.add(new MineObjectDecoration(silverOres[1], xStart + 15, yStart + lineOffset + 10));
            result.add(new MineObjectDecoration(silverOres[2], xStart + 24, yStart + lineOffset + 10));
            result.add(new MineObjectDecoration(goldOres[0], xStart + 7, yStart + lineOffset + 18));
            result.add(new MineObjectDecoration(goldOres[1], xStart + 15, yStart + lineOffset + 18));
            result.add(new MineObjectDecoration(goldOres[2], xStart + 24, yStart + lineOffset + 18));
            return result;
        }
    }

    private static class MineObjectDecoration extends DrawableObject {
        private final MineableObject obj;

        public MineObjectDecoration(MineableObject obj, int x, int y) {
            super(x, y);
            this.obj = obj;
        }

        @Override
        public void drawYourself(Model model, int x, int y) {
            model.getScreenHandler().clearSpace(x, x+4, y, y+4);
            obj.drawYourself(model.getScreenHandler(), null, new Point(x, y));
        }
    }
}

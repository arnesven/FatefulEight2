package model.quests;

import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.GhostEnemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HauntedMansionQuest extends Quest {
    private static final String INTRO = "A nobleman is determined to move back into his ancestral home, but it has " +
            "been invaded by some spectral menace. What is the source of the dark presence, and how to exorcise it?";
    private static final String ENDING = "The nobleman is overjoyed to find his mansion purged from the dark pestilence, " +
            "and rewards you for your service,";
    private static final Sprite32x32 outdoors = new Sprite32x32("outdoors", "combat.png", 0x13,
            MyColors.DARK_GREEN, MyColors.GREEN, MyColors.CYAN, MyColors.CYAN);
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.NOB, Race.ALL);

    public HauntedMansionQuest() {
        super("Haunted Mansion", "Nobleman", QuestDifficulty.HARD, 1, 50, 0, INTRO, ENDING);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Entrance",
                List.of(new CollaborativeSkillCheckSubScene(1, 3, Skill.Search, 10,
                        "Perhaps if we search, we can find an alternative way in."),
                        new SoloSkillCheckSubScene(2, 2, Skill.Logic, 8,
                                "There are strange markings on the door. Is this lock some kind of puzzle?"))),
                new QuestScene("Dark Hallway",
                List.of(new CollaborativeSkillCheckSubScene(6, 1, Skill.Search, 11,
                                "These hallways are like a maze. Which way should we go?"))),
                new QuestScene("Ancestral Ghosts",
                        List.of(new GhostCombatScene(4, 1, 4),
                                new GhostCombatScene(5, 4, 3),
                                new GhostCombatScene(5, 6, 2))),
                new QuestScene("Forbidding Dungeon",
                        List.of(new CollaborativeSkillCheckSubScene(3, 4, Skill.Search, 12,
                        "This place is really scary! Let's find our way out of here."))),
                new QuestScene("Secret Chamber",
                        List.of(new CollaborativeSkillCheckSubScene(4, 7, Skill.MagicWhite, 12,
                                "There is a dark curse cast upon this place. " +
                                        "We must perform a ritual to dissolve it with holy magic."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        List<QuestJunction> juncs = new ArrayList<>();
        juncs.add(new HauntedMansionStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL)),
                "The door to the basement is sealed. How do we get in?"));
        SimpleJunction sj = new SimpleJunction(2, 3, new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL));
        juncs.add(sj);
        SimpleJunction sj2 = new SimpleJunction(5, 0, new QuestEdge(scenes.get(1).get(0)));
        juncs.add(sj2);
        SimpleJunction sj3 = new SimpleJunction(6, 3, new QuestEdge(sj));
        juncs.add(sj3);
        return juncs;
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(2).get(0), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectFail(scenes.get(2).get(0));
        scenes.get(0).get(1).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectFail(scenes.get(2).get(0));
        scenes.get(1).get(0).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(junctions.get(3));
        scenes.get(2).get(2).connectSuccess(scenes.get(4).get(0));

        scenes.get(3).get(0).connectSuccess(scenes.get(4).get(0), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectFail(scenes.get(2).get(1));

        scenes.get(4).get(0).connectFail(scenes.get(2).get(2));
        scenes.get(4).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BG_SPRITES;
    }

    private static final Sprite32x32 topWinLeft = new Sprite32x32("topwinleft", "quest.png", 0x78,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 topWinMid = new Sprite32x32("topWinMid", "quest.png", 0x79,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 topWinRight = new Sprite32x32("topWinRight", "quest.png", 0x7A,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 facade = new Sprite32x32("facade", "quest.png", 0x75,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 frontdoor = new Sprite32x32("frontdoor", "quest.png", 0x71,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 sky1 = new Sprite32x32("sky1", "quest.png", 0x08,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 sky2 = new Sprite32x32("sky2", "quest.png", 0x09,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 sky3 = new Sprite32x32("sky3", "quest.png", 0x0A,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite32x32 sky4 = new Sprite32x32("sky4", "quest.png", 0x0B,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 uul = new Sprite32x32("underul", "quest.png", 0x18,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 uur = new Sprite32x32("underur", "quest.png", 0x1A,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 ull = new Sprite32x32("underll", "quest.png", 0x28,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 ulr = new Sprite32x32("underlr", "quest.png", 0x2A,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 uu = new Sprite32x32("underupper", "quest.png", 0x19,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 ul = new Sprite32x32("underlower", "quest.png", 0x29,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 ustairs = new Sprite32x32("understairs", "quest.png", 0x1B,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.BLACK, MyColors.DARK_BLUE);
    private static final Sprite32x32 midl = new Sprite32x32("undermidleft", "quest.png", 0x38,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 mid = new Sprite32x32("undermid", "quest.png", 0x39,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite32x32 midr = new Sprite32x32("undermidright", "quest.png", 0x3A,
            MyColors.DARK_GREEN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final List<QuestBackground> BG_SPRITES = makeBackgroundSprites();

    private static List<QuestBackground> makeBackgroundSprites() {
        Sprite[][] sprites = new Sprite[][]{
                {sky2, sky2, sky2, sky2, sky3, sky3, sky4, sky3},
                {sky1, topWinLeft, topWinRight, topWinLeft, topWinMid, topWinRight, topWinLeft, topWinRight},
                {sky1, facade, facade, facade, facade, facade, facade, facade},
                {outdoors, facade, frontdoor, facade, facade, facade, facade, facade},
                {outdoors, uul, uu, ustairs, uu, uu, uu, uur},
                {outdoors, ull, ul, ul, ul, ul, ul, ulr},
                {outdoors, outdoors, uul, uu, ustairs, uu, uur, outdoors},
                {outdoors, outdoors, midl, mid, mid, mid, midr, outdoors},
                {outdoors, outdoors, ull, ul, ul, ul, ulr, outdoors}
        };
        List<QuestBackground> result = new ArrayList<>();
        for (int y = 0; y < sprites.length; y++) {
            for (int x = 0; x < sprites[0].length; ++x) {
                result.add(new QuestBackground(new Point(x, y), sprites[y][x]));
            }
        }
        return result;
    }

    private static List<Enemy> makeGhosts(int num) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            result.add(new GhostEnemy('A'));
        }
        return result;
    }

    private static class GhostCombatScene extends CombatSubScene {
        private final int numberOfGhosts;

        public GhostCombatScene(int col, int row, int numberOfGhosts) {
            super(col, row, makeGhosts(numberOfGhosts), false);
            this.numberOfGhosts = numberOfGhosts;
        }

        @Override
        protected String getCombatDetails() {
            return "Ancestral Ghosts";
        }

        @Override
        public List<Enemy> getEnemies() {
            return makeGhosts(numberOfGhosts);
        }

        @Override
        protected boolean hasBeenDefeated() {
            return false;
        }
    }

    private static class HauntedMansionStartPoint extends QuestStartPoint {
        public HauntedMansionStartPoint(List<QuestEdge> questEdges, String talk) {
            super(questEdges, talk);
            setRow(3);
        }
    }
}

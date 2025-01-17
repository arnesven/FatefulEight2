package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.OrcArcherEnemy;
import model.enemies.OrcChieftain;
import model.enemies.OrcWarrior;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.races.Race;
import model.states.QuestState;
import model.states.events.OrcsEvent;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.PalisadeSprite;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;
import view.combat.PalisadeCombatTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrcWarCampQuest extends MainQuest {
    public static final String QUEST_NAME = "Orc War Camp";
    private static final String INTRO_TEXT =
            "You have arrived at the orc war camp. Your orders are to investigate " +
            "the camp and appraise the Orcs' intentions. Unfortunately the camp is well fortified " +
            "and you will have to make your way inside the fortifications to find the information you need.";
    private static final String ENDING_TEXT = "You've found the information you needed. It will be crucial in order to properly defend against these invaders.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();

    private List<Enemy> archers;
    private List<Enemy> otherEnemies;
    private CombatTheme combatTheme = new PalisadeCombatTheme(true);

    public OrcWarCampQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD,
                new Reward(1, 0), 0, INTRO_TEXT, ENDING_TEXT);
        archers = List.of(new OrcArcherEnemy('C'), new OrcArcherEnemy('C'), new OrcArcherEnemy('C'),
                new OrcArcherEnemy('C'), new OrcArcherEnemy('C'), new OrcArcherEnemy('C'));
        otherEnemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            otherEnemies.add(new OrcWarrior('B'));
        }
        otherEnemies.add(new OrcChieftain('A'));
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Approaching the camp", List.of(
                            new ApproachMainGateSubScene(3, 1),
                            new ApproachPalisadeWallSubScene(7, 3),
                            new PalisadeArchersCombatSubScene(6, 3),
                            new ApproachStreamOpeningSubScene(0, 3),
                            new CollectiveSkillCheckSubScene(1, 3, Skill.Sneak, 7,
                        "Now, we're going to have to extremely stealthy to avoid detection"))),
                        new QuestScene("Finding information", List.of(
                            new CollaborativeSkillCheckSubScene(4, 4, Skill.Search, 14,
                "Now let's look around for papers. Written orders would be preferable."))),
                        new QuestScene("Alarm raised", List.of(
                            new WarCampCombatSubScene(3, 4)
                )));
    }

    @Override
    public CombatTheme getCombatTheme() {
        return combatTheme;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qsp = new QuestStartPoint(
                List.of(new QuestEdge(scenes.get(0).get(0)),
                        new QuestEdge(scenes.get(0).get(1)),
                        new QuestEdge(scenes.get(0).get(3))),
                "Do we approach the main gate, the palisade or that opening where the stream flows out of the camp?");
        StoryJunction afterFight = new StoryJunction(3, 5, new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("On the corpse of the orc chieftain you find some written papers. Orders it looks like. " +
                        "The paper is not signed but it clearly indicate where this and other orc battalions are to be deployed and attack from.");
                state.leaderSay("This tells us all we need to know.");
                state.partyMemberSay(model.getParty().getRandomPartyMember(), "Too bad we don't know who wrote it.");
                state.leaderSay("Yes... unless...");
                state.println("Hesitantly, " + model.getParty().getLeader().getFirstName() +
                        " pulls out a knife and kneels down by the corpse to slice the abdomen open. " +
                        "Instantly a mass of black guts spill out on the ground. There, among blood and intestines, " +
                        "lies a shiny crimson pearl.");
                state.leaderSay("I knew it. These orcs are being manipulated by the Quad. " +
                        "Now let's get out of here, before more orcs arrive.");
                getCrimsonPearl(model, state);
            }
        };
        StoryJunction afterSearch = new StoryJunction(4, 5, new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("You are surprised that your disguise is working so well and are able to freely move about");

                state.println("You peek into a more luxurious hut in the camp.");
                state.leaderSay("This must be the chieftains hut.");
                state.partyMemberSay(model.getParty().getRandomPartyMember(), "Look's like he's not home at the moment.");
                state.print("You hurriedly rummage through a desk and quickly find what you are looking for. Papers with what appear to be orders. " +
                        "Instructions for where this and other orc battalions are to be deployed and attack from. ");
                state.print("You also find a map depicting this part of the world. Curiously it has a location on it marked with the word 'Quad'. " +
                        "The location matches the one on your map, which you discovered with Willis in the library.");
                state.leaderSay("Seems like these orcs are being manipulated by the Quad. " +
                        "Now let's get out of here before our luck runs out.");
            }
        };
        SimpleJunction extraFail = new SimpleJunction(2, 7, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));
        return List.of(qsp, afterFight, afterSearch, extraFail);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        QuestSubScene bigFight = scenes.get(2).get(0);

        // Main Gate
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0)); // Search
        scenes.get(0).get(0).connectFail(bigFight);

        // Approach palisade
        scenes.get(0).get(1).connectSuccess(scenes.get(0).get(2));
        // scene 0/1 cannot fail.

        // Palisade archer fight
        scenes.get(0).get(2).connectSuccess(bigFight);
        scenes.get(0).get(2).connectFail(junctions.get(3), QuestEdge.VERTICAL);

        // Swim through
        scenes.get(0).get(3).connectSuccess(scenes.get(0).get(4)); // Sneaking
        scenes.get(0).get(3).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);

        // Sneaking
        scenes.get(0).get(4).connectSuccess(scenes.get(1).get(0)); // Search
        scenes.get(0).get(4).connectFail(bigFight);

        scenes.get(1).get(0).connectSuccess(junctions.get(2));
        scenes.get(1).get(0).connectFail(getFailEndingNode());

        bigFight.connectSuccess(junctions.get(1));
        bigFight.connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.TAN;
    }

    @Override
    public MainQuest copy() {
        return new OrcWarCampQuest();
    }

    private static abstract class ApproachSubScene extends ConditionSubScene {
        private final String descr;

        public ApproachSubScene(int col, int row, String description) {
            super(col, row);
            this.descr = description;
        }

        @Override
        public String getDescription() {
            return descr;
        }
        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.print(getIntro());
            if (!state.yesNoInput()) {
                return new QuestEdge(state.getQuest().getStartNode(), QuestEdge.VERTICAL);
            }
            return subRun(model, state);
        }

        protected abstract QuestEdge subRun(Model model, QuestState state);

        protected abstract String getIntro();
    }

    private static class ApproachMainGateSubScene extends ApproachSubScene {
        public ApproachMainGateSubScene(int col, int row) {
            super(col, row, "Approach the main gate");
        }

        @Override
        protected String getIntro() {
            return "There are a few guards here. Apart from straight out storming the gate, " +
                    "the only way to get in is to fool the guards into thinking you belong.\n" +
                    "Do you want to attempt to disguise the party and infiltrate the camp? (Y/N) ";
        }

        @Override
        protected QuestEdge subRun(Model model, QuestState state) {
            state.leaderSay("Okay people. Try your best to look orcish. " +
                    "Desecrate your armor, smear yourselves with mud, walk disgracefully.");
            boolean fail = false;
            GameCharacter failer = null;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getRace().id() == Race.HALF_ORC.id()) {
                    state.println(gc.getName() + " is a half-orc and doesn't really need much of a disguise.");
                } else {
                    state.println(gc.getName() + " attempts to disguise " + (gc.getGender()?"herself":"himself") + ".");
                    SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, gc, Skill.Entertain, 7, 15, 0);
                    if (!result.isSuccessful()) {
                        fail = true;
                        failer = gc;
                    }
                }
            }
            if (fail) {
                state.println("The party walks up to the front gate. The guards are about to let you in, when one of them starts shouting and points to " + failer.getName() + ".");
                state.leaderSay("Uh-oh...");
                if (OrcsEvent.getAttitude(model) > 0) {
                    state.println("Curiously, instead of rushing you with your weapons, you are approached by a large orc and his entourage.");
                    if (new OrcsEvent(model).interactWithOrcs(model)) {
                        return getSuccessEdge();
                    }
                    return getFailEdge();
                }
                state.println("Before you know it, lots of orcs have rushed out of their tents and come at you with their weapons drawn.");
                return getFailEdge();
            }
            return getSuccessEdge();
        }
    }

    private static class ApproachStreamOpeningSubScene extends ApproachSubScene {
        public ApproachStreamOpeningSubScene(int col, int row) {
            super(col, row, "Approach stream opening");
        }

        protected String getIntro() {
            return "There's an opening in the palisade here, a stream is flowing out of the camp from under a metal grate.\n" +
                    "This is probably how the orcs get their fresh water. Do you wish to attempt to swim inside? (Y/N) ";
        }

        @Override
        protected QuestEdge subRun(Model model, QuestState state) {
            boolean result = model.getParty().doCollectiveSkillCheck(model, state, Skill.Acrobatics, 6);
            if (result) {
                state.println("The party manages to swim against the rather strong current, under a metal grate and into " +
                        "the camp.");
                return getSuccessEdge();
            }
            state.println("The party tries to swim in the little stream, but the current is surprisingly strong, " +
                    "and it sweeps you rapidly downstream. By the time you are out of the water, you are far away from the camp " +
                    "and it is too late in the day to go back.");
            return getFailEdge();
        }
    }

    private static class ApproachPalisadeWallSubScene extends ApproachSubScene {
        public ApproachPalisadeWallSubScene(int col, int row) {
            super(col, row, "Approach palisade");
        }

        @Override
        protected String getIntro() {
            return "This part of the palisade is climbable, but there are archers patrolling on top.\n" +
                    "Do you run up to the palisade in order to get close enough to climb it? (Y/N) ";
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        protected QuestEdge subRun(Model model, QuestState state) {
            state.println("As you run up toward the wall the archers spot you and start shooting at you!");
            return getSuccessEdge();
        }
    }

    private class WarCampCombatSubScene extends CombatSubScene {

        public WarCampCombatSubScene(int col, int row) {
            super(col, row, List.of(new OrcWarrior('A')), true);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            OrcsEvent.decreaseAttitude(model);
            combatTheme = new PalisadeCombatTheme(true);
            return super.run(model, state);
        }

        @Override
        public List<Enemy> getEnemies() {
            List<Enemy> enemies = new ArrayList<>(otherEnemies);
            for (Enemy e : archers) {
                if (!e.isDead()) {
                    e.setFortified(false);
                    enemies.add(e);
                }
            }
            return enemies;
        }

        private List<Enemy> makeOrcEnemies() {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                enemies.add(new OrcWarrior('B'));
            }
            enemies.add(new OrcChieftain('A'));
            return enemies;
        }

        @Override
        protected String getCombatDetails() {
            return "Lots of orcs!";
        }
    }

    private class PalisadeArchersCombatSubScene extends CombatSubScene {
        public PalisadeArchersCombatSubScene(int col, int row) {
            super(col, row, List.of(new OrcArcherEnemy('A')), true);
            setTimeLimit(6);
        }

        @Override
        public List<Enemy> getEnemies() {
            List<Enemy> enemies = new ArrayList<>();
            for (Enemy e : archers) {
                if (!e.isDead()) {
                    e.setFortified(true);
                    enemies.add(e);
                }
            }
            return enemies;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            combatTheme = new PalisadeCombatTheme(false);
            QuestEdge toReturn =  super.run(model, state);
            if (toReturn == getSuccessEdge()) {
                state.println("In spite of the archers furiously firing arrows at you, the party manages to climb the " +
                        "palisade and get up on the platform. By now, the camp is in wild commotion and many orcs are rushing " +
                        "toward you with their weapons drawn.");
            }
            return toReturn;
        }

        @Override
        protected String getCombatDetails() {
            return "Orc archers on the palisade wall";
        }
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> background = new ArrayList<>();
        PalisadeSprite ulCorner = new PalisadeSprite(0x5E);
        PalisadeSprite urCorner = new PalisadeSprite(0x5F);
        PalisadeSprite llCorner = new PalisadeSprite(0x6E);
        PalisadeSprite lrCorner = new PalisadeSprite(0x6F);

        background.add(new QuestBackground(new Point(1, 1), ulCorner));
        background.add(new QuestBackground(new Point(6, 1), urCorner));
        background.add(new QuestBackground(new Point(1, 7), llCorner));
        background.add(new QuestBackground(new Point(6, 7), lrCorner));

        PalisadeSprite horizontalTop = new PalisadeSprite(0x8F);
        background.add(new QuestBackground(new Point(2, 1), horizontalTop));
        background.add(new QuestBackground(new Point(5, 1), horizontalTop));

        PalisadeSprite gateLeft = new PalisadeSprite(0x7E);
        PalisadeSprite gateRight = new PalisadeSprite(0x7F);
        background.add(new QuestBackground(new Point(3, 1), gateLeft));
        background.add(new QuestBackground(new Point(4, 1), gateRight));

        PalisadeSprite verticalLeft = new PalisadeSprite(0x7D);
        PalisadeSprite verticalRight = new PalisadeSprite(0x8D);
        for (int y = 2; y < 7; ++y) {
            background.add(new QuestBackground(new Point(1, y), verticalLeft));
            background.add(new QuestBackground(new Point(6, y), verticalRight));
        }

        PalisadeSprite horizontalBottom = new PalisadeSprite(0x8E);
        for (int x = 2; x < 6; ++x) {
            background.add(new QuestBackground(new Point(x, 7), horizontalBottom));
        }

        Sprite32x32 tents = new Sprite32x32("tents", "quest.png", 0x8C,
                MyColors.TAN, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.BLACK);

        background.add(new QuestBackground(new Point(2, 2), tents));
        background.add(new QuestBackground(new Point(5, 2), tents));
        background.add(new QuestBackground(new Point(2, 3), tents));
        background.add(new QuestBackground(new Point(5, 3), tents));
        background.add(new QuestBackground(new Point(2, 5), tents));
        background.add(new QuestBackground(new Point(5, 5), tents));
        background.add(new QuestBackground(new Point(2, 6), tents));
        background.add(new QuestBackground(new Point(3, 6), tents));
        background.add(new QuestBackground(new Point(4, 6), tents));
        background.add(new QuestBackground(new Point(5, 6), tents));
        return background;
    }
}

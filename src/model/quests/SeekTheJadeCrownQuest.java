package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.*;
import model.mainstory.jungletribe.RubiqBall;
import model.mainstory.jungletribe.RubiqPuzzleEvent;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.states.QuestState;
import model.states.events.PeskyCrowEvent;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public abstract class SeekTheJadeCrownQuest extends RemotePeopleQuest {
    private static final String INTRO_TEXT = "TODO INTRO";
    private static final String ENDING_TEXT = "You leave the pyramid, carrying with you some of the trinkets that you've managed to pick up along the way.";

    private final String pyramidName;
    private final Point pyramidLocation;

    private List<RubiqBall> oldPuzzleState;
    boolean golemVisited;

    public SeekTheJadeCrownQuest(String pyramidName, Point pyramidLocation) {
        super(getQuestName(pyramidName), "UNUSED", QuestDifficulty.VERY_HARD, new Reward(200), 3, INTRO_TEXT, ENDING_TEXT);
        this.pyramidName = pyramidName;
        this.pyramidLocation = pyramidLocation;
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        oldPuzzleState = null;
        golemVisited = false;
        getSuccessEndingNode().move(5, 1);
    }

    @Override
    public void setRemoteLocation(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), false);
        setRemotePath(model, model.getWorld().shortestPathToPoint(pyramidLocation));
    }

    public static String getQuestName(String pyramidName) {
        return "Search " + pyramidName + " Pyramid";
    }

    public String getPyramidName() {
        return pyramidName + " Pyramid";
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Trek to the pyramid", List.of(
                    new CombatSubScene(3, 2, makeMonsters(), false) {
                        @Override
                        protected String getCombatDetails() {
                            return "many monsters";
                        }
                    },
                    new CollaborativeSkillCheckSubScene(1, 1, Skill.Survival, 1, // TODO: 15
                            "Perhaps we can find our way through the jungle."))),
                new QuestScene("Search the pyramid", List.of(
                    new CollaborativeSkillCheckSubScene(5, 7, Skill.Search, 1, // TODO: 14
                            "This place is larger than expected. " +
                            "It may take some time to find where the crown could be hidden.")
                )),
                new QuestScene("Denizens of the pyramid", List.of(
                        new CombatSubScene(6, 5, makeMonsters(), false) {
                            @Override
                            protected String getCombatDetails() {
                                return "many monsters";
                            }
                        },
                        new CollectiveSkillCheckSubScene(5, 5, Skill.Sneak, 1, // TODO: 8
                                "No need to disturb them.")
                )),
                new QuestScene("Stone Golem", List.of(
                        new PuzzleSubScene(6, 2),
                        new CombatSubScene(7, 2, List.of(new StoneGolemEnemy('A')), false) { // TODO: Stone Golem enemy

                            @Override
                            protected String getCombatDetails() {
                                return "a Stone Golem";
                            }

                            @Override
                            public List<Enemy> getEnemies() {
                                return List.of(new StoneGolemEnemy('A'));
                            }

                            @Override
                            public QuestEdge run(Model model, QuestState state) {
                                return super.run(model, state);
                            }

                            @Override
                            protected boolean hasBeenDefeated() {
                                return !golemVisited;
                            }
                        }
                )));
    }

    private List<Enemy> makeMonsters() {
        return List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanLeaderEnemy('C'),
                new FrogmanShamanEnemy('B'), new FrogmanScoutEnemy('A'), new FrogmanShamanEnemy('B'),
                new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartingPointWithPosition(2, 0,
                List.of(new QuestEdge(scenes.get(0).get(0)),
                        new QuestEdge(scenes.get(0).get(1))),
                "Do we take the easy way, or the safe way?");

        QuestJunction sj = new SimpleJunction(2, 3, new QuestEdge(scenes.get(1).get(0), QuestEdge.VERTICAL));

        QuestDecisionPoint qdp = new QuestDecisionPoint(5, 6, List.of(
                new QuestEdge(scenes.get(2).get(0)),
                new QuestEdge(scenes.get(2).get(1))),
                "Looks like some monsters have taken up residence here. " +
                "Do we clear them out, or sneak past them?");
        return List.of(qsp, sj, qdp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(0).get(1).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(getFailEndingNode());

        scenes.get(1).get(0).connectSuccess(junctions.get(2));
        scenes.get(1).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));
        scenes.get(2).get(1).connectSuccess(scenes.get(3).get(0), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectFail(scenes.get(2).get(0));

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectFail(scenes.get(3).get(1));
        scenes.get(3).get(1).connectSuccess(scenes.get(3).get(0));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.mysticSong;
    }

    private class PuzzleSubScene extends QuestSubScene {

        private static final Sprite32x32 SPRITE = new Sprite32x32("storyjunc", "quest.png", 0x0C,
                MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BROWN);

        public PuzzleSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        public String getDetailedDescription() {
            return "???";
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "???";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.leaderSay("Hmm... there's some kind of contraption in the wall here. Looks like some kind of puzzle.");
            if (model.getParty().size() > 1) {
                GameCharacter rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                state.partyMemberSay(rando, "Perhaps the crown is hidden behind it?");
            }
            model.getLog().waitForAnimationToFinish();
            RubiqPuzzleEvent event;
            if (oldPuzzleState != null) {
                event = new RubiqPuzzleEvent(model, oldPuzzleState);
            } else {
                event = new RubiqPuzzleEvent(model);
            }
            event.doTheEvent(model);
            if (event.solvedPuzzle()) {
                return getSuccessEdge();
            }
            oldPuzzleState = event.getPuzzleState();
            golemVisited = true;
            return getFailEdge();
        }
    }
}

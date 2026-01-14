package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.*;
import model.mainstory.jungletribe.GainSupportOfJungleTribeTask;
import model.mainstory.jungletribe.RubiqBall;
import model.mainstory.jungletribe.RubiqPuzzleEvent;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.MiniPictureSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.MiniPictureSubView;
import view.subviews.SubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SeekTheJadeCrownQuest extends RemotePeopleQuest {
    private static final String INTRO_TEXT = "Your quest for the Jade Crown has led you to believe it is hidden in the ";
    private static final String ENDING_TEXT = "You leave the pyramid, carrying with you some of the trinkets that you've managed to pick up along the way.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackgroundSprites();
    private static final List<QuestBackground> DECORATIONS = makeDecorations();

    private final String pyramidName;
    private final Point pyramidLocation;

    private List<RubiqBall> oldPuzzleState;
    boolean golemVisited;

    public SeekTheJadeCrownQuest(String pyramidName, Point pyramidLocation) {
        super(getQuestName(pyramidName), "UNUSED", QuestDifficulty.VERY_HARD, new Reward(200), 3,
                INTRO_TEXT + pyramidName + " pyramid.", ENDING_TEXT);
        this.pyramidName = pyramidName;
        this.pyramidLocation = pyramidLocation;
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        oldPuzzleState = null;
        golemVisited = false;
        getSuccessEndingNode().move(6, 0);
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
                    new CombatSubScene(3, 2, makeJungleMonsters(), false) {
                        @Override
                        protected String getCombatDetails() {
                            return "many monsters";
                        }
                    },
                    new CollaborativeSkillCheckSubScene(1, 2, Skill.Survival, 1, // TODO: 15
                            "Perhaps we can find our way through the jungle."))),
                new QuestScene("Search the pyramid", List.of(
                    new CollaborativeSkillCheckSubScene(5, 7, Skill.Search, 1, // TODO: 14
                            "This place is larger than expected. " +
                            "It may take some time to find where the crown could be hidden.")
                )),
                new QuestScene("Denizens of the pyramid", List.of(
                        new CombatSubScene(6, 5, makePyramidDenizens(), false) {
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

    protected abstract List<Enemy> makeJungleMonsters();

    protected abstract List<Enemy> makePyramidDenizens();

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartingPointWithPosition(2, 1,
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
                GainSupportOfJungleTribeTask jungleTask = (GainSupportOfJungleTribeTask) getTask();
                state.println("By completing the puzzle you unlock the chamber within.");
                if (jungleTask.isCrownInPyramid(getPyramidName())) {
                    state.println("There, on a pedastal in the middle of the room, sits a brilliant crown. It matches Jequen's description perfectly.");
                    state.leaderSay("This must be it!");
                    state.println("You recovered the Jade Crown!");
                    SubView current = model.getSubView();
                    model.setSubView(new MiniPictureSubView(current, new MiniPictureSprite(0x43), "The Jade Crown"));
                    state.println("Press return to continue.");
                    state.waitForReturn();
                    model.setSubView(current);
                } else {
                    state.println("You find plenty of trinkets, but nothing matching the description of the Jade crown.");
                    state.leaderSay("Well, this was a waste of time.");
                }
                return getSuccessEdge();
            }
            oldPuzzleState = event.getPuzzleState();
            golemVisited = true;
            return getFailEdge();
        }
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return DECORATIONS;
    }


    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> bg = new ArrayList<>();
        Sprite skyGradient = new Sprite32x32("skyGradient", "quest.png", 0xAD,
                MyColors.DARK_PURPLE, MyColors.PURPLE, MyColors.PINK, MyColors.ORANGE);
        Sprite sunset = new Sprite32x32("skyGradient", "quest.png", 0xAE,
                MyColors.DARK_PURPLE, MyColors.PURPLE, MyColors.PINK, MyColors.YELLOW);
        for (int i = 0; i < 8; ++i) {
            if (i == 2) {
                bg.add(new QuestBackground(new Point(i, 0), sunset, true));
            } else {
                bg.add(new QuestBackground(new Point(i, 0), skyGradient, true));
            }
        }



        Sprite pyra = new Sprite32x32("pyra", "quest.png", 0x30, MyColors.GRAY_RED,
                MyColors.GRAY, MyColors.BROWN, MyColors.DARK_GREEN);
        Sprite pyraFloor = new Sprite32x32("pyradoor", "quest.png", 0x08, MyColors.GRAY_RED,
                MyColors.GRAY, MyColors.BROWN, MyColors.BROWN);
        Sprite pyraRoof = new Sprite32x32("pyraroof", "quest.png", 0x32, MyColors.GRAY_RED,
                MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.BROWN);

        Sprite pyraStairs =  new Sprite32x32("pyrastairs", "quest.png", 0x43, MyColors.GRAY,
                MyColors.GRAY, MyColors.BROWN, MyColors.DARK_GREEN);

        // PYRAMID
        bg.add(new QuestBackground(new Point(2, 8), pyra, true));
        bg.add(new QuestBackground(new Point(3, 8), pyra, true));
        bg.add(new QuestBackground(new Point(4, 8), pyra, true));
        bg.add(new QuestBackground(new Point(5, 8), pyra, true));
        bg.add(new QuestBackground(new Point(6, 8), pyraStairs, true));
        bg.add(new QuestBackground(new Point(7, 8), pyra, true));

        bg.add(new QuestBackground(new Point(2, 7), pyraRoof, true));
        for (int x = 3; x < 8; ++x) {
            bg.add(new QuestBackground(new Point(x, 7), pyraFloor, true));
        }

        bg.add(new QuestBackground(new Point(2, 6), pyraRoof, true));
        bg.add(new QuestBackground(new Point(3, 6), pyra, true));
        bg.add(new QuestBackground(new Point(4, 6), pyra, true));
        bg.add(new QuestBackground(new Point(5, 6), pyra, true));
        bg.add(new QuestBackground(new Point(6, 6), pyraStairs, true));
        bg.add(new QuestBackground(new Point(7, 6), pyra, true));

        bg.add(new QuestBackground(new Point(3, 5), pyraRoof, true));
        for (int x = 4; x < 8; ++x) {
            bg.add(new QuestBackground(new Point(x, 5), pyraFloor, true));
        }

        bg.add(new QuestBackground(new Point(3, 4), pyraRoof, true));
        bg.add(new QuestBackground(new Point(4, 4), pyra, true));
        bg.add(new QuestBackground(new Point(5, 4), pyra, true));
        bg.add(new QuestBackground(new Point(6, 4), pyraStairs, true));
        bg.add(new QuestBackground(new Point(7, 4), pyra, true));

        bg.add(new QuestBackground(new Point(4, 3), pyraRoof, true));
        for (int x = 5; x < 8; ++x) {
            bg.add(new QuestBackground(new Point(x, 3), pyraFloor, true));
        }

        bg.add(new QuestBackground(new Point(4, 2), pyraRoof, true));
        bg.add(new QuestBackground(new Point(5, 2), pyra, true));
        bg.add(new QuestBackground(new Point(6, 2), pyraStairs, true));
        bg.add(new QuestBackground(new Point(7, 2), pyra, true));

        bg.add(new QuestBackground(new Point(5, 1), pyraRoof, true));
        bg.add(new QuestBackground(new Point(6, 1), pyraFloor, true));
        bg.add(new QuestBackground(new Point(7, 1), pyraRoof, true));
        return bg;
    }

    private static List<QuestBackground> makeDecorations() {
        List<QuestBackground> bg = new ArrayList<>();
        Sprite jungle = new Sprite32x32("jungle", "world_foreground.png", 0x61,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
        for (int i = 0; i < 9; ++i) {
            bg.add(new QuestBackground(new Point(0, i), jungle, false));
            if (i < 8) {
                bg.add(new QuestBackground(new Point(i, 8), jungle, false));
            }
        }
        bg.add(new QuestBackground(new Point(1, 0), jungle, false));

        bg.add(new QuestBackground(new Point(3, 0), jungle, false));
        bg.add(new QuestBackground(new Point(4, 0), jungle, false));
        return bg;
    }
}

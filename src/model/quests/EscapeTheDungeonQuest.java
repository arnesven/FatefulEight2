package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.*;
import model.items.Item;
import model.mainstory.SitInDungeonState;
import model.map.CastleLocation;
import model.quests.scenes.*;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import model.states.RunAwayState;
import model.states.dailyaction.CraftItemState;
import sound.BackgroundMusic;
import util.MatrixFunction;
import util.MyMatrices;
import util.MyPair;
import util.MyStrings;
import view.MyColors;
import view.sprites.DungeonWallSprite;
import view.sprites.Sprite32x32;
import view.subviews.QuestSubView;
import view.subviews.SubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EscapeTheDungeonQuest extends MainQuest {
    public static final String QUEST_NAME = "Escape the Dungeon";
    private static final String INTRO = "After spending a few days in your cell a few options for escape seem feasible to you. " +
            "You must escape the dungeon cells and figure out what's going on in the kingdom.";
    private static final String OUTRO = "You escape through a sewage pipe and emerge on the outside of the castle walls.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();
    private List<Item> belongings;
    private QuestJunction[][] nodeGrid;
    private List<MovingEnemyGroup> enemies;

    public EscapeTheDungeonQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 1, 0, 50, INTRO, OUTRO);
    }

    @Override
    public MainQuest copy() {
        return new EscapeTheDungeonQuest();
    }


    public void setLootRewardItems(List<Item> belongings) {
        this.belongings = belongings; // TODO: Give back to player
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        GameState notToReturn = super.endOfQuest(model, state, questWasSuccess);
        if (questWasSuccess) {
            return new RunAwayState(model);
        }
        CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
        return new SitInDungeonState(model, castle, this, 0);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        List<QuestScene> scenes = new ArrayList<>();
        scenes.add(new QuestScene("Break out of cell", List.of(
                new SoloLockpickingSubScene(4, 1, 1, "Maybe we can try this old piece of bone."), // 9
                new SoloSkillCheckSubScene(3, 1, Skill.Sneak, 1, "He passes every once in a while, " + // 10
                        "if we time it just right we can probably grab his keys without him noticing."),
                new CollaborativeSkillCheckSubScene(5, 0, Skill.Labor, 1, "It will be a lot of work " + // 13
                        "but maybe we can dig a tunnel through this wall."),
                new CollaborativeSkillCheckSubScene(1, 0, Skill.Labor, 1, "It will be a lot of work " + // 13
                        "but maybe we can dig a tunnel through this wall."))));
        scenes.add(new QuestScene("Inmates", List.of(
                new CombatInmatesSubScene(6, 0, 3),
                new CombatInmatesSubScene(1, 4, 5),
                new CombatInmatesSubScene(1, 6, 6),
                new CombatInmatesSubScene(6, 4, 7),
                new CombatInmatesSubScene(6, 6, 8)
        )));
        scenes.add(new QuestScene("Guards at the door", List.of(
                new CombatGuardsSubScene(5, 7),
                new CollectiveSkillCheckSubScene(4, 8, Skill.Sneak, 8,
                        "They'll have to be blind not to see us, but let's try it anyway.")
        )));
        return scenes;
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        enemies = makeGuardGroups();
        List<QuestJunction> juncs = new ArrayList<>();
        juncs.add(new QuestStartingPointWithPosition(3, 0,
                        List.of(new QuestEdge(scenes.get(0).get(0)), new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL),
                                new QuestEdge(scenes.get(0).get(2)), new QuestEdge(scenes.get(0).get(3))),
                        "We can try to tunnel out, or maybe we can " +
                                "lift the key off the guard while he passes by, or maybe we can just pick the lock."));

        QuestJunction extraFail = new SimpleJunction(0, 1, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));
        juncs.add(extraFail);

        QuestDecisionPoint qdp = new QuestDecisionPoint(4, 7,
                List.of(new QuestEdge(scenes.get(2).get(0)), new QuestEdge(scenes.get(2).get(1))),
                "We can fight them, or we can try the stealthy approach.");
        juncs.add(qdp);

        this.nodeGrid = new QuestJunction[8][7];
        for (int x = 3; x <= 4; ++x) {
            for (int y = 2; y <= 5; ++y) {
                addChooseNode(nodeGrid, x, y, new ArrayList<>());
                if (y > 2) {
                    connect(nodeGrid[x][y], nodeGrid[x][y-1]);
                }
            }
        }

        StoryJunction craftTable = new CraftingTableJunction(2, 7, new QuestEdge(nodeGrid[3][5]));
        juncs.add(craftTable);

        nodeGrid[3][3].connectTo(new QuestEdge(scenes.get(1).get(1)));
        nodeGrid[3][5].connectTo(new QuestEdge(scenes.get(1).get(2)));
        nodeGrid[4][3].connectTo(new QuestEdge(scenes.get(1).get(3)));
        nodeGrid[4][5].connectTo(new QuestEdge(scenes.get(1).get(4)));

        nodeGrid[4][5].connectTo(new QuestEdge(qdp));
        nodeGrid[3][5].connectTo(new QuestEdge(craftTable, QuestEdge.VERTICAL));

        for (int y = 2; y <= 5; ++y) {
            connect(nodeGrid[3][y], nodeGrid[4][y]);
        }
        MyMatrices.traversRowWise(nodeGrid, (matrix, x, y) -> {
            if (matrix[x][y] != null) {
                juncs.add(matrix[x][y]);
            }
        });

        for (MovingEnemyGroup enemy : enemies) {
            enemy.setOnPath(nodeGrid);
        }

        return juncs;
    }

    private List<MovingEnemyGroup> makeGuardGroups() {
        List<Point> path = List.of(new Point(3, 2), new Point(3, 3),
                new Point(3, 4), new Point(3, 5),
                new Point(4, 5), new Point(4, 4),
                new Point(4, 3), new Point(4, 2));
        return List.of(new MovingEnemyGroup("Guards", makeGuards(6), path, 1),
                new MovingEnemyGroup("Guards", makeGuards(6), path, 5));
    }

    private List<Enemy> makeGuards(int amount) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            result.add(new JailGuardEnemy('A'));
        }
        return result;
    }

    private void connect(QuestJunction junc1, QuestJunction junc2) {
        junc1.connectTo(new ArrowlessEdge(junc2));
        junc2.connectTo(new ArrowlessEdge(junc1));
    }

    private void addChooseNode(QuestNode[][] nodeGrid, int x, int y, List<QuestEdge> questEdges) {
        int finalY = y;
        if (y > 2) {
            finalY++;
        }
        nodeGrid[x][y] = new ChooseNode(x, finalY, questEdges) {
            @Override
            protected boolean preRunHook(Model model, QuestState state) {
                if (hasEnemies()) {
                    if (runCombat(model, state)) {
                        return true;
                    }
                }
                moveGuards(model, state);
                if (hasEnemies()) {
                    if (runCombat(model, state)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private void moveGuards(Model model, QuestState state) {
        state.setCursorEnabled(false);
        for (MovingEnemyGroup group : enemies) {
            ChooseNode node = group.getNode();
            QuestEdge edge = group.getEdgeToMoveTo(model, state, nodeGrid);
            if (edge != null && edge.getNode() instanceof ChooseNode) {
                ChooseNode destination = ((ChooseNode) edge.getNode());
                node.removeEnemyGroup(group);
                QuestSubView.animateAvatarAlongEdge(state, node.getPosition(), edge, group.getSprite());
                destination.addEnemyGroup(group);
                group.setNode(destination);
            }
        }
        state.setCursorEnabled(true);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(junctions.get(1));
        scenes.get(0).get(0).connectSuccess(nodeGrid[4][2]);

        scenes.get(0).get(1).connectFail(junctions.get(1));
        scenes.get(0).get(1).connectSuccess(nodeGrid[3][2]);

        scenes.get(0).get(2).connectFail(junctions.get(1), QuestEdge.VERTICAL);
        scenes.get(0).get(2).connectSuccess(scenes.get(1).get(0));

        scenes.get(0).get(3).connectFail(junctions.get(1));
        scenes.get(0).get(3).connectSuccess(nodeGrid[3][2], QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectSuccess(nodeGrid[4][2], QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectSuccess(nodeGrid[3][3]);
        scenes.get(1).get(2).connectSuccess(nodeGrid[3][5]);
        scenes.get(1).get(3).connectSuccess(nodeGrid[4][3]);
        scenes.get(1).get(4).connectSuccess(nodeGrid[4][5]);

        scenes.get(2).get(0).connectSuccess(getSuccessEndingNode());

        scenes.get(2).get(1).connectSuccess(getSuccessEndingNode());
        scenes.get(2).get(1).connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.DARK_BROWN;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> result = new ArrayList<>();
        Sprite32x32[] walls = new Sprite32x32[7];
        for (int i = 0; i < walls.length; ++i) {
            walls[i] = new Sprite32x32("escapedungeonwall" + i, "quest.png", 0x96 + i, MyColors.DARK_BROWN,
                    MyColors.LIGHT_GRAY, MyColors.BLACK, MyColors.GRAY);
        }

        // TOP CELLS
        result.add(new QuestBackground(new Point(0, 0), walls[3]));
        result.add(new QuestBackground(new Point(2, 0), walls[3]));
        result.add(new QuestBackground(new Point(5, 0), walls[3]));
        result.add(new QuestBackground(new Point(7, 0), walls[3]));
        result.add(new QuestBackground(new Point(0, 1), walls[2]));
        result.add(new QuestBackground(new Point(1, 1), walls[4]));
        result.add(new QuestBackground(new Point(2, 1), walls[2]));
        result.add(new QuestBackground(new Point(3, 1), walls[1]));
        result.add(new QuestBackground(new Point(4, 1), walls[0]));
        result.add(new QuestBackground(new Point(5, 1), walls[2]));
        result.add(new QuestBackground(new Point(6, 1), walls[4]));
        result.add(new QuestBackground(new Point(7, 1), walls[2]));


        // LEFT CELLS
        result.add(new QuestBackground(new Point(0, 3), walls[1]));
        result.add(new QuestBackground(new Point(1, 3), walls[5]));
        result.add(new QuestBackground(new Point(2, 3), walls[5]));
        result.add(new QuestBackground(new Point(2, 4), walls[6]));
        result.add(new QuestBackground(new Point(0, 5), walls[1]));
        result.add(new QuestBackground(new Point(1, 5), walls[1]));
        result.add(new QuestBackground(new Point(2, 5), walls[2]));
        result.add(new QuestBackground(new Point(2, 6), walls[6]));
        result.add(new QuestBackground(new Point(0, 7), walls[1]));
        result.add(new QuestBackground(new Point(1, 7), walls[5]));
        result.add(new QuestBackground(new Point(2, 7), walls[2]));

        // RIGHT CELLS
        result.add(new QuestBackground(new Point(7, 3), walls[1]));
        result.add(new QuestBackground(new Point(6, 3), walls[5]));
        result.add(new QuestBackground(new Point(5, 3), walls[5]));
        result.add(new QuestBackground(new Point(5, 4), walls[6]));
        result.add(new QuestBackground(new Point(7, 5), walls[1]));
        result.add(new QuestBackground(new Point(6, 5), walls[1]));
        result.add(new QuestBackground(new Point(5, 5), walls[2]));
        result.add(new QuestBackground(new Point(5, 6), walls[6]));
        result.add(new QuestBackground(new Point(7, 7), walls[1]));
        result.add(new QuestBackground(new Point(6, 7), walls[5]));
        result.add(new QuestBackground(new Point(5, 7), walls[2]));

        return result;
    }

    private static List<Enemy> makeInmates(int numberOfEnemies) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < numberOfEnemies; ++i) {
            result.add(new DungeonInmateEnemy('A'));
        }
        return result;
    }

    private class CombatInmatesSubScene extends CombatSubScene {
        private final int numberOfPrisoner;
        private boolean removeAvatars = false;

        public CombatInmatesSubScene(int col, int row, int numberOfEnemies) {
            super(col, row, makeInmates(numberOfEnemies));
            this.numberOfPrisoner = numberOfEnemies;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            moveGuards(model, state);
            InmateEvent inmateEvent = new InmateEvent(model);
            inmateEvent.doTheEvent(model);
            QuestEdge toReturn;

            if (inmateEvent.didAttack()) {
                toReturn = super.run(model, state);
            } else {
                removeAvatars = true;
                toReturn = getSuccessEdge();
            }

            do {
                moveGuards(model, state);
                state.print("Do you want to hide out in this cell for a bit? (Y/N) ");
            } while (inmateEvent.yesNoInput());
            return toReturn;
        }

        @Override
        protected boolean hasBeenDefeated() {
            return removeAvatars;
        }

        @Override
        protected String getCombatDetails() {
            return "Prisoners";
        }

        private class InmateEvent extends DailyEventState {
            private boolean didAttack = false;

            public InmateEvent(Model model) {
                super(model);
            }

            @Override
            protected void doEvent(Model model) {
                SubView subView = model.getSubView();
                showRandomPortrait(model, Classes.None, "Prisoner");
                println("You encounter " + MyStrings.numberWord(numberOfPrisoner) + " prisoners.");
                portraitSay("Hey you! You running around in here?");
                leaderSay("Something like that. What are you lot up to?");
                portraitSay("We're busting out of here soon.");
                leaderSay("The guards look tough, you really thin you can take them down?");
                portraitSay("Heh, yeah, but we've been keeping busy... look at these.");
                println("The prisoners pull out a few makeshift weapons, put together from " +
                        "odd pieces of wood and metal fragments.");
                portraitSay("Nothing fancy, but better than facing those guards with just our fists.");

                int choice = multipleOptionArrowMenu(model, 24, 28,
                        List.of("Propose join up", "Attack prisoners", "Leave"));
                if (choice == 0) {
                    MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 11);
                    if (pair.first) {
                        partyMemberSay(pair.second, "How about we join forces? If we swarm the guards, there's not much they can do.");
                        portraitSay("That sounds like a good idea!");
                        // TODO: Make allies during this quest.
                    } else {
                        partyMemberSay(pair.second, "Yes... you guys go first, we'll be right behind you.");
                        portraitSay("Hey, that doesn't seem quite fair to me.");
                        tryToBackOut(model);
                    }
                } else if (choice == 1) {
                    leaderSay("Yes... thanks for making them for us! Let's get them!");
                    portraitSay("You want a piece of us? Fine, we'll smack you down!");
                    didAttack = true;
                } else {
                    // TODO: Bring back
                    // tryToBackOut(model);
                }
                model.setSubView(subView);
            }

            private void tryToBackOut(Model model) {
                leaderSay("Alright, we'll just stay out of each other's way then.");
                portraitSay("What? You think we'll just let you go after we've told you our plan?");
                leaderSay("Worried we'll sell you out to the guards? Come on, you can trust us!");
                portraitSay("I've heard that one before. Not falling for that, I say 'no witnesses' is a better motto.");
                leaderSay("You are making a grave mistake.");
                didAttack = true;
            }

            public boolean didAttack() {
                return didAttack;
            }
        }
    }

    private class CombatGuardsSubScene extends CombatSubScene {
        public CombatGuardsSubScene(int col, int row) {
            super(col, row, makeGuards(12));
        }

        @Override
        protected String getCombatDetails() {
            return "Lots of guards";
        }
    }

    private class CraftingTableJunction extends StoryJunction {
        public CraftingTableJunction(int col, int row, QuestEdge edge) {
            super(col, row, edge);
        }

        @Override
        protected void doAction(Model model, QuestState state) {
            moveGuards(model, state);
            state.leaderSay("A crafting table? That's unexpected.");
            CraftItemState craftState = new CraftItemState(model);
            SubView subView = model.getSubView();
            craftState.run(model);
            model.setSubView(subView);
            do {
                moveGuards(model, state);
                state.print("Do you want to hide out here for a bit? (Y/N) ");
            } while (state.yesNoInput());
        }
    }
}

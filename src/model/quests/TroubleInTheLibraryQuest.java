package model.quests;

import model.MainStory;
import model.Model;
import model.classes.Skill;
import model.journal.StoryPart;
import model.quests.scenes.ArrowlessEdge;
import model.quests.scenes.ChooseNode;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.QuestState;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class TroubleInTheLibraryQuest extends MainQuest {
    public static final String QUEST_NAME = "Trouble in the Library";
    private static final String INTRO_TEXT = "In search of invaluable information, the party has come to a large library. " +
            "Before anything can be learned however, you must first disable the magical automatons which have run amuck within.";
    private static final String ENDING_TEXT = "The automatons have been disabled, and the library can once again be visited by civilians.";

    public TroubleInTheLibraryQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 1, 0, 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Discern Automaton Behavior", List.of(
                new SoloSkillCheckSubScene(4, 4, Skill.Logic, 8,
                        "Wait a minute. It feels like there's a pattern to these machines' behavior."))),
                new QuestScene("Check Dead Automatons", List.of(
                        new ConditionSubScene(7, 8) {
                            @Override
                            public String getDescription() {
                                return "Exit library.";
                            }

                            @Override
                            public QuestEdge run(Model model, QuestState state) {
                                state.println("There are still automatons roaming in the library!");
                                return getFailEdge();
                            }
                        }
                )),
                new QuestScene("Shut down machine", List.of(
                new SoloSkillCheckSubScene(4, 8, Skill.MagicAny, 10,
                        "Here's the arcane machine which is powering the automatons. How do we shut it down?"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        List<QuestJunction> juncs = new ArrayList<>();
        QuestNode[][] nodeGrid = new QuestNode[8][9];
        QuestSubScene subScene = scenes.get(0).get(0);
        nodeGrid[subScene.getColumn()][subScene.getRow()] = subScene;

        nodeGrid[3][4] = new StoryJunction(3, 4, new QuestEdge(nodeGrid[3][3], QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.leaderSay("So you're telling me, that the blue automatons always .... and the red ones .....");
            }
        };
        nodeGrid[0][0] = new QuestStartPointWithoutDecision(new QuestEdge(nodeGrid[1][0]), "In we go...");
        QuestSubScene conditionScene = scenes.get(1).get(0);
        nodeGrid[conditionScene.getColumn()][conditionScene.getRow()] = conditionScene;
        QuestSubScene machineScene = scenes.get(2).get(0);
        nodeGrid[machineScene.getColumn()][machineScene.getRow()] = machineScene;

        for (int y = 7; y >= 0; --y) {
            addChooseNode(nodeGrid, 7, y, List.of(new ArrowlessEdge(nodeGrid[7][y+1])));
        }

        for (int x = 6; x >= 0; --x) {
            addChooseNode(nodeGrid, x, 7, List.of(new ArrowlessEdge(nodeGrid[x+1][7])));
            for (int y = 6; y >= 0; --y) {
                if (nodeGrid[x][y] == null) {
                    List<QuestEdge> connections = new ArrayList<>();
                    if (nodeGrid[x][y + 1] instanceof ChooseNode) {
                        connections.add(new ArrowlessEdge(nodeGrid[x][y+1]));
                    }
                    if (nodeGrid[x+1][y] instanceof ChooseNode) {
                        connections.add(new ArrowlessEdge(nodeGrid[x+1][y]));
                    }
                    addChooseNode(nodeGrid, x, y, connections);
                }
            }
        }

        for (int y = 0; y < nodeGrid[0].length - 1; y++) {
            for (int x = 1; x < nodeGrid.length; ++x) {
                if (nodeGrid[x][y] instanceof QuestJunction && nodeGrid[x-1][y] instanceof ChooseNode) {
                    ((QuestJunction) nodeGrid[x][y]).connectTo(new ArrowlessEdge(nodeGrid[x - 1][y]));
                }
            }
        }

        for (int x = 0; x < nodeGrid.length; ++x) {
            for (int y = 1; y < nodeGrid[0].length - 1; ++y) {
                if (nodeGrid[x][y] instanceof QuestJunction && nodeGrid[x][y-1] instanceof ChooseNode) {
                    ((QuestJunction) nodeGrid[x][y]).connectTo(new ArrowlessEdge(nodeGrid[x][y - 1]));
                }
            }
        }

        ((QuestJunction)nodeGrid[subScene.getColumn()][subScene.getRow()-1]).connectTo(new QuestEdge(subScene));

        ((QuestJunction)nodeGrid[3][4]).getConnections().clear();
        ((QuestJunction)nodeGrid[3][4]).connectTo(new QuestEdge(nodeGrid[3][3], QuestEdge.VERTICAL));

        ((QuestJunction)nodeGrid[0][0]).getConnections().clear();
        ((QuestJunction)nodeGrid[0][0]).connectTo(new QuestEdge(nodeGrid[1][0], QuestEdge.HORIZONTAL));

        conditionScene.connectSuccess(getSuccessEndingNode());
        conditionScene.connectFail(nodeGrid[7][7]);

        ((QuestJunction)nodeGrid[machineScene.getColumn()][machineScene.getRow()-1]).connectTo(new QuestEdge(machineScene));

        for (int y = 0; y < nodeGrid[0].length; y++) {
            for (int x = 0; x < nodeGrid.length; x++) {
                if (nodeGrid[x][y] instanceof QuestJunction) {
                    juncs.add((QuestJunction) nodeGrid[x][y]);
                }
            }
        }
        return juncs;
    }

    private void addChooseNode(QuestNode[][] nodeGrid, int x, int y, List<QuestEdge> questEdges) {
        nodeGrid[x][y] = new ChooseNode(x, y, questEdges);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        for (QuestJunction junc : junctions) {
            if (junc.getRow() == 4) {
                if (junc.getColumn() == 5) {
                    scenes.get(0).get(0).connectFail(junc);
                } else if (junc.getColumn() == 3) {
                    scenes.get(0).get(0).connectSuccess(junc);
                }
            }

            if (junc.getRow() == 7) {
                if (junc.getColumn() == 2) {
                    scenes.get(2).get(0).connectFail(junc);
                }
            }
        }
        scenes.get(2).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    protected int getStoryTrack() {
        return StoryPart.TRACK_A;
    }
}

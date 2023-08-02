package model.quests;

import model.Model;
import model.states.QuestState;
import util.MyStrings;
import view.MyColors;
import view.subviews.ArrowMenuSubView;
import view.subviews.ControlPanelSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class AncientStrongholdQuest extends MainQuest {
    public static final String QUEST_NAME = "Ancient Stronghold";
    private static final String TEXT =
            "You've finally arrived at the location of the Quad's supposed hiding place, " +
            "the ancient stronghold. You do not know what to expect, but you know that you must " +
            "enter and face whatever terror lies within.";
    private static final String END_TEXT =
            "With the spirit of the Quad defeated you feel a heavy weight lifted from your shoulders. " +
            "The world will now be a safer place, for people, orcs and frogmen alike.";

    private AncientStrongholdModel strongholdModel;

    public AncientStrongholdQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 2, 0, 100, TEXT, END_TEXT);
        getSuccessEndingNode().move(6, 0);
        strongholdModel = new AncientStrongholdModel();
    }

    @Override
    public MainQuest copy() {
        return new AncientStrongholdQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        QuestSubScene groundFloor = new StrongholdCombatSubScene(5, 8);
        return List.of(new QuestScene("Ground Floor", List.of(
                groundFloor,
                new AncientMachinerySubScene(3, 8, groundFloor) {
                    @Override
                    protected AncientStrongholdControlPanel getControlPanel() {
                        return strongholdModel.getControlPanel(0);
                    }
                }
        )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new StrongholdStartingPoint(new QuestEdge(scenes.get(0).get(0)),
                "I guess we're really doing this..."));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectFail(scenes.get(0).get(1));

        scenes.get(0).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static class StrongholdStartingPoint extends QuestStartPointWithoutDecision {
        public StrongholdStartingPoint(QuestEdge connection, String leaderTalk) {
            super(connection, leaderTalk);
            setRow(8);
            setColumn(7);
        }
    }

    private static class StrongholdCombatSubScene extends ConditionSubScene {
        public StrongholdCombatSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        public String getDescription() {
            return "Ground Floor";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            // TODO: Random fight
            state.println("You are on the ground floor of the tower.");
            state.print("Do you want to ascend the stairs to the 1st floor? (Y/N) ");
            if (state.yesNoInput()) {
                return getSuccessEdge();
            }
            return getFailEdge();
        }
    }

    private abstract static class AncientMachinerySubScene extends ConditionSubScene {
        private final QuestSubScene previous;

        public AncientMachinerySubScene(int col, int row, QuestSubScene previous) {
            super(col, row);
            this.previous = previous;
        }

        @Override
        public String getDescription() {
            return "Strange Machinery";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("This room has a large machine with cogs, pistons and gears. " +
                    "There's a control panel in the middle of the room with a single lever. Next to it " +
                    "are four round slots.");
            do {
                state.print("Do you step up to the control panel (Y) or do you wish to return to the previous room (N)? ");
                if (state.yesNoInput()) {
                    controlPanelLoop(model, state);
                } else {
                    break;
                }
            } while (true);
            return new QuestEdge(previous);
        }

        private void controlPanelLoop(Model model, QuestState state) {
            SubView questSubView = model.getSubView();
            state.setCursorEnabled(false);
            AncientStrongholdControlPanel controlPanel = getControlPanel();
            ControlPanelSubView controlPanelSubView = new ControlPanelSubView(model.getSubView(), controlPanel);
            model.setSubView(controlPanelSubView);
            do {
                state.waitForReturnSilently();
                int selectedIndex = controlPanelSubView.getCursorIndex();
                if (selectedIndex == -1) {
                    state.println("You step away from the control panel.");
                    break;
                } else if (selectedIndex == AncientStrongholdControlPanel.NUMBER_OF_PEARL_SLOTS) {
                    if (controlPanel.isLeverOn()) {
                        state.println("The lever seems to be locked into position.");
                    } else {
                        if (controlPanel.canPullLever()) {
                            controlPanel.pullLever();
                            state.println("You pull the lever down. The machine hums curiously.");
                        } else {
                            state.println("The lever seems to be locked into position. Perhaps something is missing?");
                        }
                    }
                } else { // Pearl slot index in 0..3
                    if (controlPanel.getPearlSlot(selectedIndex) != null && controlPanel.isLeverOn()) {
                        state.println("The pearl cannot be removed. Something is holding it firmly in place.");
                    } else { // Set or replace pearl
                        MyColors selectedPearlColor = getSelectedPearlColor(model, state, selectedIndex, controlPanel.getPearlSlot(selectedIndex) != null);
                        controlPanel.setPearl(selectedIndex, selectedPearlColor);
                        if (selectedPearlColor == null) {
                            state.println("You remove the pearl from the slot.");
                        } else {
                            state.println("You place the " + selectedPearlColor.name().toLowerCase() + " pearl into the slot.");
                        }
                    }
                }
            } while (true);
            model.setSubView(questSubView);
            state.setCursorEnabled(true);
        }

        private MyColors getSelectedPearlColor(Model model, QuestState state, int xShift, boolean remove) {
            List<String> optionList = new ArrayList<>();
            for (MyColors color : AncientStrongholdControlPanel.PEARL_COLORS) {
                optionList.add(MyStrings.capitalize(color.name()));
            }
            if (remove) {
                optionList.add("Remove");
            }
            int[] selectedAction = new int[1];
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    optionList, 32+xShift*4, 22, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    selectedAction[0] = cursorPos;
                    model.setSubView(getPrevious());
                }
            });
            state.waitForReturnSilently();
            if (selectedAction[0] == AncientStrongholdControlPanel.PEARL_COLORS.length) {
                return null;
            }
            return AncientStrongholdControlPanel.PEARL_COLORS[selectedAction[0]];
        }

        protected abstract AncientStrongholdControlPanel getControlPanel();
    }
}

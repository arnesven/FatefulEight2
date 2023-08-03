package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.*;
import model.states.CombatEvent;
import model.states.QuestState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.ControlPanelSubView;
import view.subviews.SubView;
import view.widget.QuestBackground;

import java.awt.*;
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
    private static final Sprite CONTROL_PANEL = new Sprite32x32("controlpanel", "quest.png", 0x0E,
            MyColors.BLACK, MyColors.DARK_BROWN, MyColors.DARK_GRAY, MyColors.CYAN);
    private static final Sprite32x32 SUBSCENE_SPRITE = new Sprite32x32("combatsubscene", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackgroundSprites();

    private AncientStrongholdModel strongholdModel;
    private List<QuestBackground> decorations;

    public AncientStrongholdQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 2, 0, 100, TEXT, END_TEXT);
        strongholdModel = new AncientStrongholdModel();
        updateDecorations();
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        getSuccessEndingNode().move(6, 0);
    }

    @Override
    public MainQuest copy() {
        return new AncientStrongholdQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        QuestSubScene[] floors = new QuestSubScene[AncientStrongholdModel.FLOORS];
        QuestSubScene[] machines = new QuestSubScene[AncientStrongholdModel.FLOORS];
        for (int i = 0; i < floors.length; ++i) {
            floors[i] = new StrongholdCombatSubScene(5, 7-i, i, (i==0 ? null : floors[i-1]));
            int finalI = i;
            machines[i] = new AncientMachinerySubScene(4, 7-finalI, floors[i], finalI == 0) {
                @Override
                protected AncientStrongholdControlPanel getControlPanel() {
                    return strongholdModel.getControlPanel(finalI);
                }
            };
        }
        List<QuestSubScene> subScenes = new ArrayList<>();
        for (int i = 0; i < floors.length; ++i) {
            subScenes.add(floors[i]);
            subScenes.add(machines[i]);
        }
        return List.of(new QuestScene("Floors and Machines", subScenes));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        SimpleJunction elevator = new SimpleJunction(1, 7, new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL));
        return List.of(new StrongholdStartingPoint(new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                "I guess we're really doing this..."), elevator);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        for (int i = 0; i < AncientStrongholdModel.FLOORS; i++) {
            if (i != AncientStrongholdModel.FLOORS - 1) {
                scenes.get(0).get(i * 2).connectSuccess(scenes.get(0).get(i * 2 + 2), QuestEdge.VERTICAL);
            }
            scenes.get(0).get(i*2).connectFail(scenes.get(0).get(i*2 + 1), QuestEdge.VERTICAL);
        }
        scenes.get(0).get(1).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private class StrongholdStartingPoint extends QuestStartPointWithoutDecision {
        public StrongholdStartingPoint(QuestEdge connection, String leaderTalk) {
            super(connection, leaderTalk);
            setRow(8);
            setColumn(7);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            strongholdModel.setCode(model.getMainStory().getAncientStrongholdCode());
            return super.run(model, state);
        }
    }

    private static class StrongholdCombatSubScene extends QuestSubScene {
        private final int floorNumber;
        private final QuestSubScene previous;

        public StrongholdCombatSubScene(int col, int row, int floorNumber, QuestSubScene previous) {
            super(col, row);
            this.floorNumber = floorNumber;
            this.previous = previous;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SUBSCENE_SPRITE.getName(), new Point(xPos, yPos), SUBSCENE_SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return floorNumberText(floorNumber) + " Floor";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (MyRandom.flipCoin()) {
                state.print("The party encounters a group of enemies! Press enter to continue.");
                state.waitForReturn();
                AncientStrongholdEnemySet enemySet = new AncientStrongholdEnemySet(floorNumber);
                CombatEvent combat = new CombatEvent(model, enemySet.getEnemies(),
                        state.getCombatTheme(), true, false);
                combat.addExtraLoot(enemySet.getPearls());
                combat.run(model);
                if (!model.getParty().isWipedOut()) {
                    state.transitionToQuestView(model);
                    ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
                } else {
                    return new QuestEdge(state.getQuest().getFailEndingNode());
                }
            }

            state.print("You are on the " + floorNumberText(floorNumber).toLowerCase() + " floor of the tower. " +
                    "Select a location to go to.");
            while (true) {
                state.setSelectedElement(this);
                state.waitForReturn();
                if (state.getSelectedElement() == this) {
                    state.print("You are on the " + floorNumberText(floorNumber).toLowerCase() + " floor of the tower. " +
                            "Select a location to go to.");
                } else if (getSuccessEdge() != null && state.getSelectedElement() == getSuccessEdge().getNode()) {
                    return getSuccessEdge();
                } else if (getFailEdge() != null && state.getSelectedElement() == getFailEdge().getNode()) {
                    return getFailEdge();
                } else if (state.getSelectedElement() == previous) {
                    return new QuestEdge(previous);
                } else {
                    state.print("You cannot there from your current location.");
                }
            }
        }

        private String floorNumberText(int floorNumber) {
            switch (floorNumber) {
                case 0 : return "Ground";
                case 1 : return "1st";
                case 2 : return "2nd";
                case 3 : return "3rd";
                default : return floorNumber + "th";
            }
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        protected MyColors getFailEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public String getDetailedDescription() {
            return "Combat H";
        }
    }

    private abstract class AncientMachinerySubScene extends QuestSubScene {
        private final QuestSubScene previous;
        private final boolean isElevator;

        public AncientMachinerySubScene(int col, int row, QuestSubScene previous, boolean isElevator) {
            super(col, row);
            this.previous = previous;
            this.isElevator = isElevator;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SUBSCENE_SPRITE.getName(), new Point(xPos, yPos), SUBSCENE_SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "Strange Machinery";
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        public String getDetailedDescription() {
            return null;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("This room has a large machine with cogs, pistons and gears. " +
                    "There's a control panel in the middle of the room with a single lever. Next to it " +
                    "are four round slots.");
            do {
                state.print("Do you step up to the control panel (Y) or do you wish to return to the previous room (N)? ");
                if (state.yesNoInput()) {
                    if (!controlPanelLoop(model, state) && isElevator) {
                        return getFailEdge();
                    }
                    if (getControlPanel().codeIsCorrect() && isElevator) {
                        state.print("The door to your left is now open. Do you wish to take the elevator to the top of the tower? (Y/N) ");
                        if (state.yesNoInput()) {
                            return getSuccessEdge();
                        }
                    }
                } else {
                    break;
                }
            } while (true);
            return new QuestEdge(previous);
        }

        private boolean controlPanelLoop(Model model, QuestState state) {
            SubView questSubView = model.getSubView();
            state.setCursorEnabled(false);
            AncientStrongholdControlPanel controlPanel = getControlPanel();
            ControlPanelSubView controlPanelSubView = new ControlPanelSubView(model.getSubView(), controlPanel);
            model.setSubView(controlPanelSubView);
            boolean result = true;
            do {
                state.waitForReturnSilently();
                int selectedIndex = controlPanelSubView.getCursorIndex();
                if (selectedIndex == -1) {
                    state.println("You step away from the control panel.");
                    break;
                } else if (selectedIndex == AncientStrongholdControlPanel.NUMBER_OF_PEARL_SLOTS) {
                    result = controlPanel.handleLever(model, state, isElevator);
                } else { // Pearl slot index in 0..3
                    controlPanel.handlePearl(model, state, selectedIndex);
                }
            } while (true);
            model.setSubView(questSubView);
            state.setCursorEnabled(true);
            updateDecorations();
            return result;
        }

        protected abstract AncientStrongholdControlPanel getControlPanel();
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return this.decorations;
    }

    private void updateDecorations() {
        List<QuestBackground> controlPanels = new ArrayList<>();
        for (int i = 0; i < AncientStrongholdModel.FLOORS; i++) {
            Sprite dials = new LongSprite(0x53,
                    getDialColor(i, 0), getDialColor(i, 1),
                    getDialColor(i, 2), getDialColor(i, 3));
            Sprite pearls = new LongSprite(0x54,
                    getPearlColor(i, 0), getPearlColor(i, 1),
                    getPearlColor(i, 2), getPearlColor(i, 3));
            dials.addToOver(pearls);
            controlPanels.add(new QuestBackground(new Point(3, 7-i), dials, false));
        }
        this.decorations = controlPanels;
    }

    private MyColors getPearlColor(int panel, int index) {
        if (strongholdModel.getControlPanel(panel).getPearlSlot(index) == null) {
            return MyColors.GRAY;
        }
        return strongholdModel.getControlPanel(panel).getPearlSlot(index);
    }

    private MyColors getDialColor(int panel, int index) {
        switch (strongholdModel.getControlPanel(panel).getDialLevel(index)) {
            case 2 : return MyColors.WHITE;
            case 3 : return MyColors.RED;
            default : return MyColors.GRAY;
        }
    }

    private static class LongSprite extends Sprite {
        public LongSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
            super("longsprite"+num, "quest.png", num%16, num/16, 32, 16);
            setColor1(color1);
            setColor2(color2);
            setColor3(color3);
            setColor4(color4);
        }
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> controlPanels = new ArrayList<>();
        for (int i = 0; i < AncientStrongholdModel.FLOORS; i++) {
            controlPanels.add(new QuestBackground(new Point(3, 7-i), CONTROL_PANEL, false));
        }
        return controlPanels;
    }
}

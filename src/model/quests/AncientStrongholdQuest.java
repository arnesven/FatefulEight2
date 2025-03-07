package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CombatAdvantage;
import model.enemies.*;
import model.items.Item;
import model.items.special.PearlItem;
import model.items.spells.TeleportSpell;
import model.quests.scenes.CombatSubScene;
import model.states.CombatEvent;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyPair;
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

    private final AncientStrongholdModel strongholdModel;
    private List<QuestBackground> decorations;
    private boolean willisIntroElevatorRun = false;
    private boolean willisIntroOtherRun = false;

    public AncientStrongholdQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD,
                new Reward(2, 0, 100), 0, TEXT, END_TEXT);
        strongholdModel = new AncientStrongholdModel();
        updateDecorations();
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        getSuccessEndingNode().move(4, 0);
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
        return List.of(new QuestScene("Floors and Machines", subScenes),
                new QuestScene("Final Scene", List.of(new FinalCombatSubScene(2, 0))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new StrongholdStartingPoint(new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                "I guess we're really doing this..."));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        for (int i = 0; i < AncientStrongholdModel.FLOORS; i++) {
            if (i != AncientStrongholdModel.FLOORS - 1) {
                scenes.get(0).get(i * 2).connectSuccess(scenes.get(0).get(i * 2 + 2), QuestEdge.VERTICAL);
            }
            scenes.get(0).get(i*2).connectFail(scenes.get(0).get(i*2 + 1), QuestEdge.VERTICAL);
        }
        scenes.get(0).get(1).connectSuccess(scenes.get(1).get(0));
        scenes.get(0).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        GameState toReturn = super.endOfQuest(model, state, questWasSuccess);
        state.println("As you have no more need of them. You discard the pearls you have collected.");
        for (Item it : new ArrayList<>(model.getParty().getInventory().getPearls())) {
            if (it instanceof PearlItem) {
                model.getParty().getInventory().removePearl((PearlItem) it);
            }
        }
        state.println("You return to the elevator and notice an antechamber. Inside is a large glowing portal.");
        state.leaderSay("I wonder where this would take us?");
        state.print("Do you step through the portal (Y/N)? ");
        if (state.yesNoInput()) {
            TeleportSpell.teleportPartyToPosition(model, state, model.getMainStory().getCampPosition(), false);
            state.leaderSay("Oh, we're back at the orc war camp. So that's how they managed to move those " +
                    "troops so quickly.");
        }
        return toReturn;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.strongholdSong;
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
        private boolean pearlsFound = false;

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
                boolean surprise = MyRandom.flipCoin();
                state.print("The party encounters a group of enemies! ");
                if (surprise) {
                    state.print(" They seem to have been caught off guard by your presence! ");
                }
                state.print("Press enter to continue.");
                state.waitForReturn();
                AncientStrongholdEnemySet enemySet = new AncientStrongholdEnemySet(floorNumber);
                CombatEvent combat = new CombatEvent(model, enemySet.getEnemies(),
                        state.getCombatTheme(), true, surprise ? CombatAdvantage.Party : CombatAdvantage.Neither);
                combat.addExtraLoot(enemySet.getPearls());
                combat.run(model);
                if (!model.getParty().isWipedOut()) {
                    state.transitionToQuestView(model);
                } else {
                    return new QuestEdge(state.getQuest().getFailEndingNode());
                }
            } else if (!pearlsFound) {
                MyPair<SkillCheckResult, GameCharacter> result = state.doPassiveSkillCheck(Skill.Perception, 8);
                if (result.first.isSuccessful()) {
                    state.println("You spot some pearls on the floor and pick them up.");
                    for (int i = 0; i < 3; ++i) {
                        PearlItem pearl = makeRandomPearl();
                        state.println("The party gains a " + pearl.getName() + ".");
                        model.getParty().getInventory().addSpecialItem(pearl);
                    }
                    pearlsFound = true;
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

        public static PearlItem makeRandomPearl() {
            MyColors color;
            do {
                MyColors[] colors = AncientStrongholdControlPanel.PEARL_COLORS;
                color = colors[MyRandom.randInt(colors.length)];
            } while (color == MyColors.DARK_RED);
            return PearlItem.makeFromColor(color);
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
                    "There's a control panel in the middle of the room with a single lever. Next to the lever " +
                    "are four round slots.");
            willisIntro(model, state);
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

        private void willisIntro(Model model, QuestState state) {
            if (!willisInParty(model)) {
                return;
            }
            if (willisIntroElevatorRun && willisIntroOtherRun) {
                return;
            }

            if (isElevator && !willisIntroElevatorRun) {
                willisSay(model, "This is arcanic machinery!");
                state.leaderSay("Fascinating. Any idea what it could be good for?");
                willisIntroElevatorRun = true;
                willisSay(model, "This machine probably powers the elevator I spotted on the outside of the stronghold.");
                state.leaderSay("Interesting.");
                willisSay(model, "...and this trap door we're standing on.");
                state.println("You quickly jump away.");
                state.leaderSay("Thanks for the heads up Willis.");
                willisSay(model, "This control panel has some dials. My guess is we want all the dials cranked up to red " +
                        "to get this thing working. The wrong code might open the trap door.");
                state.leaderSay("Okay, better let it be until we know what the code is.");
            } else if (!isElevator && !willisIntroOtherRun) {
                willisSay(model, "This is arcanic machinery!");
                state.leaderSay("Fascinating. Any idea what it could be good for?");
                willisIntroOtherRun = true;
                willisSay(model, "No clue. Maybe it is connected to the colored pearls some how.");
                state.leaderSay("Interesting.");
                willisSay(model, "This control panel has some dials. My guess is we want all the dials cranked up to red " +
                        "to get this thing working.");
                state.leaderSay("Okay. Do you think it's safe to fiddle with it?");
                willisSay(model, "I see no harm in it.");
            }
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
                    if (!result) {
                        return false;
                    }
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

    private void willisSay(Model model, String text) {
        model.getParty().partyMemberSay(model, model.getMainStory().getWillisCharacter(), text);
    }

    private boolean willisInParty(Model model) {
        return model.getParty().getPartyMembers().contains(model.getMainStory().getWillisCharacter());
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
        List<QuestBackground> background = new ArrayList<>();
        for (int i = 0; i < AncientStrongholdModel.FLOORS; i++) {
            background.add(new QuestBackground(new Point(3, 7-i), CONTROL_PANEL, false));
        }
        Sprite32x32 sky1 = new Sprite32x32("sky1", "quest.png", 0x08,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
        Sprite32x32 sky2 = new Sprite32x32("sky2", "quest.png", 0x09,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
        Sprite32x32 sky3 = new Sprite32x32("sky3", "quest.png", 0x0A,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
        Sprite32x32 sky4 = new Sprite32x32("sky4", "quest.png", 0x0B,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.YELLOW, MyColors.DARK_BLUE);
        background.add(new QuestBackground(new Point(1, 0), sky1));
        background.add(new QuestBackground(new Point(6, 0), sky1));
        for (int x = 0; x < 8; x += 7) {
            background.add(new QuestBackground(new Point(x, 0), sky1));
            background.add(new QuestBackground(new Point(x, 1), sky2));
            background.add(new QuestBackground(new Point(x, 2), sky3));
            if (x == 0) {
                background.add(new QuestBackground(new Point(x, 3), sky4));
            } else {
                background.add(new QuestBackground(new Point(x, 3), sky3));
            }
            background.add(new QuestBackground(new Point(x, 4), sky3));
            background.add(new QuestBackground(new Point(x, 5), sky2));
            background.add(new QuestBackground(new Point(x, 6), sky1));
        }

        for (int i = 0; i < 6; ++i) {
            background.add(new QuestBackground(new Point(1, i+1),
                    new Sprite32x32("ancienttowerleft" + i, "quest.png", 0x90 + 0x10*i,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.DARK_BLUE)));
            background.add(new QuestBackground(new Point(6, i+1),
                    new Sprite32x32("ancienttowerright" + i, "quest.png", 0x91 + 0x10*i,
                            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.DARK_BLUE)));
        }
        return background;
    }

    private static class FinalCombatSubScene extends CombatSubScene {
        public FinalCombatSubScene(int col, int row) {
            super(col, row, List.of(new QuadMinionEnemy('A'), new QuadMinionEnemy('A'),
                    new GhostEnemy('B'), new GhostEnemy('B'),
                    new GhostEnemy('B'), new GhostEnemy('B'),
                    new QuadMinionEnemy('A'), new QuadMinionEnemy('A'),

                    new QuadMinionEnemy('A'), new QuadMinionEnemy('A'),
                    new GhostEnemy('B'), new QuadSpiritEnemy('C'),
                    new QuadMinionEnemy('A'), new QuadMinionEnemy('A'),
                    new QuadMinionEnemy('A'), new QuadMinionEnemy('A')
                    ));

        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("You step out of the elevator and in to what feels like some kind of a lobby. Through a hallway you can see " +
                    "a light from the chamber within, and you hear voices.");
            state.print("Do you enter the chamber carefully and calmly (Y) or do you rush in with weapons drawn (N)? ");
            if (state.yesNoInput()) {
                state.println("You slowly step into the chamber. There are lots of people here but also ghostly specters. " +
                        "Some of them notice you and start whispering to one another.");
                state.printQuote("Spirit of the Quad",
                        "Ah, finally, I was wondering if you would come for a visit.");
            } else {
                state.println("The chamber is filled with people and specters who seem startled by your sudden presence!");
                setSurpriseAttack(true);
            }
            return super.run(model, state);
        }

        @Override
        protected String getCombatDetails() {
            return "the Quad";
        }
    }
}

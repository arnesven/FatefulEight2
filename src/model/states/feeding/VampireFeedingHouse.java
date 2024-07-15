package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.quests.*;
import model.states.GameState;
import model.states.events.GeneralInteractionEvent;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.subviews.ArrowMenuSubView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class VampireFeedingHouse {
    private static final List<MyColors> HOUSE_COLORS =
            List.of(MyColors.CYAN, MyColors.PINK, MyColors.BEIGE,
                    MyColors.LIGHT_YELLOW);
    private final int width;
    private final int stories;
    private final int dwellers;
    private final int sleeping;
    private final int lockDifficulty;
    private final int windowOpen;
    private final MyColors color;

    private List<FeedingSubScene> subScenes;
    private List<FeedingJunction> junctions;
    private QuestFailNode failNode;
    private QuestSuccessfulNode successfulNode;
    private final GameCharacter vampire;
    private boolean sleepInfoGiven = false;

    public VampireFeedingHouse(GameCharacter vampire) {
        this.width = MyRandom.randInt(1, 3);
        this.stories = MyRandom.randInt(1, 3);
        this.dwellers = MyRandom.randInt(1, width*stories + 1);
        this.sleeping = Math.max(MyRandom.randInt(0, dwellers), MyRandom.randInt(0, dwellers));
        this.lockDifficulty = (sleeping < dwellers && MyRandom.rollD6()==6) ? 0 : MyRandom.randInt(6, 8);
        color = MyRandom.sample(HOUSE_COLORS);
        if (stories == 1) {
            windowOpen = 0;
        } else if (stories == 2) {
            windowOpen = MyRandom.randInt(2);
        } else {
            windowOpen = MyRandom.randInt(2) * 2;
        }

        this.vampire = vampire;
        makeNodes();
    }

    private void makeNodes() {
        this.junctions = new ArrayList<>();
        this.subScenes = new ArrayList<>();

        this.failNode = new QuestFailNode();
        this.successfulNode = new QuestSuccessfulNode(new Reward(0, 0,0 ), "");
        successfulNode.move(7, 3);

        StakeOutSubScene stakeOut = new StakeOutSubScene(0, 3, vampire);
        subScenes.add(stakeOut);
        UnlockDoorSubScene unlockScene = new UnlockDoorSubScene(2, 3, lockDifficulty, vampire);
        subScenes.add(unlockScene);

        ClimbingScene climbingScene = new ClimbingScene(2, 2, windowOpen, vampire);
        if (anyWindowsOpen()) {
            subScenes.add(climbingScene);
        }

        SneakingSubScene sneakScene = new SneakingSubScene(4, 3, dwellers - sleeping, vampire);
        subScenes.add(sneakScene);
        FeedingNode feedingNode = new FeedingNode(6, 3, dwellers, sleeping, vampire);
        subScenes.add(feedingNode);


        GoToNextHouseNode goToNextHouse = new GoToNextHouseNode();

        List<QuestEdge> enterOptions = new ArrayList<>(List.of(new QuestEdge(subScenes.get(1)), new QuestEdge(goToNextHouse)));
        if (anyWindowsOpen()) {
            enterOptions.add(new QuestEdge(climbingScene, QuestEdge.VERTICAL));
        }
        ChooseToEnter chooseToEnterJunc = new ChooseToEnter(1, 3, windowOpen, enterOptions);

        junctions.add(new FeedingStartNode(stakeOut));
        junctions.add(chooseToEnterJunc);
        junctions.add(goToNextHouse);

        stakeOut.connectSuccess(chooseToEnterJunc);
        unlockScene.connectSuccess(sneakScene);
        unlockScene.connectFail(goToNextHouse, QuestEdge.VERTICAL);
        sneakScene.connectSuccess(feedingNode);
        sneakScene.connectFail(failNode, QuestEdge.VERTICAL);
        feedingNode.connectSuccess(successfulNode);
        feedingNode.connectFail(goToNextHouse, QuestEdge.VERTICAL);

        if (anyWindowsOpen()) {
            climbingScene.connectSuccess(sneakScene);
            climbingScene.connectFail(goToNextHouse, QuestEdge.VERTICAL);
        }
    }

    private boolean anyWindowsOpen() {
        return windowOpen > 0;
    }

    public int getStories() {
        return stories;
    }

    public int getDwellers() {
        return dwellers;
    }

    public int getSleeping() {
        return sleeping;
    }

    public int getLockDifficulty() {
        return lockDifficulty;
    }

    public List<? extends QuestJunction> getJunctions() {
        return junctions;
    }

    public List<? extends QuestSubScene> getSubScenes() {
        return subScenes;
    }

    public List<QuestNode> getNodes() {
        List<QuestNode> nodes = new ArrayList<>(subScenes);
        nodes.addAll(junctions);
        nodes.add(failNode);
        nodes.add(successfulNode);
        return nodes;
    }

    public MyColors getColor() {
        return color;
    }

    public int getOpenWindow() {
        return windowOpen;
    }

    public boolean isSleepInfoGiven() {
        return sleepInfoGiven;
    }

    public int getWidth() {
        return width;
    }

    private class FeedingStartNode extends FeedingJunction {
        public FeedingStartNode(QuestNode nextNdde) {
            super(0, 0, List.of(new QuestEdge(nextNdde, QuestEdge.VERTICAL)));
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state) {
            state.println(state.heOrSheCap(vampire.getGender()) + " approaches a house, it's a " +
                    MyStrings.numberWord(getStories()) + " story building.");
            return getConnection(0);
        }
    }

    private class StakeOutSubScene extends FeedingSubScene {
        public StakeOutSubScene(int col, int row, GameCharacter vampire) {
            super(col, row, vampire);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            state.print(vampire.getFirstName() + " stakes out the house for a few minutes. ");
            SkillCheckResult result = vampire.testSkill(model, Skill.Perception);
            state.println("Perception " + result.asString() + ".");
            if (result.getModifiedRoll() < 6 || result.getUnmodifiedRoll() == 1) {
                state.println("But " + heOrShe(vampire.getGender()) + " discerns nothing.");
            } else {
                String dwellers = "are " + MyStrings.numberWord(getDwellers()) + " people";
                if (getDwellers() == 1) {
                    dwellers = "is only one person";
                }
                state.print("And " + hisOrHer(vampire.getGender()) + " vampiric senses tell " +
                        himOrHer(vampire.getGender()) + " that there " + dwellers + " living there");
                if (result.getModifiedRoll() >= 10) {
                    if (getDwellers() == 1) {
                        if (getSleeping() == 1) {
                            state.println(", and he or she is asleep.");
                        } else {
                            state.println(", and he or she is awake.");
                        }
                    } else {
                        String numberWord = MyStrings.numberWord(getSleeping()).replace("zero","none");
                        if (getDwellers() == getSleeping()) {
                            numberWord = "all";
                        }
                        state.println(", " + numberWord +
                                " of them " + (getSleeping()==1?"is":"are") + " asleep.");
                    }
                    VampireFeedingHouse.this.sleepInfoGiven = true;
                } else {
                    state.println(".");
                }
            }
            return super.getSuccessEdge();
        }
    }

    private static class ChooseToEnter extends FeedingJunction {
        private final int windowsOpen;

        public ChooseToEnter(int col, int row, int windowOpen, List<QuestEdge> questEdges) {
            super(col, row, questEdges);
            this.windowsOpen = windowOpen;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state) {
            if (windowsOpen > 0) {
                state.print("There is a window open on the " + MyStrings.nthWord(windowsOpen) + " floor. How would you like to enter the house?");
                int[] selectedAction = new int[1];
                model.setSubView(new ArrowMenuSubView(model.getSubView(),
                        List.of("Through front door", "Through window", "Not at all"), 32, 32, ArrowMenuSubView.NORTH_WEST) {
                    @Override
                    protected void enterPressed(Model model, int cursorPos) {
                        selectedAction[0] = cursorPos;
                        model.setSubView(getPrevious());
                    }
                });
                state.waitForReturn();
                if (selectedAction[0] == 0) {
                    return getConnection(0);
                }
                if (selectedAction[0] == 1) {
                    return getConnection(2);
                }
                return getConnection(1);
            } else {
                state.print("Do you want to try to enter this house? (Y/N) ");
                if (state.yesNoInput()) {
                    return getConnection(0);
                }
                return getConnection(1);
            }
        }
    }

    private static class UnlockDoorSubScene extends FeedingSubScene {
        private final int difficulty;

        public UnlockDoorSubScene(int col, int row, int difficulty, GameCharacter vampire) {
            super(col, row, vampire);
            this.difficulty = difficulty;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (difficulty == 0) {
                state.println("The front door is not locked! " + vampire.getFirstName() + " quietly enters the house.");
                return getSuccessEdge();
            }
            state.println("The front door is locked.");
            boolean success = model.getParty().doSoloLockpickCheck(model, state, difficulty);
            if (!success) {
                state.println("The door to the house remains firmly locked.");
                return getFailEdge();
            }
            state.println(vampire.getFirstName() + " quietly enters the house through the front door.");
            return getSuccessEdge();
        }
    }

    private static class SneakingSubScene extends FeedingSubScene {

        private final int peopleAwake;

        public SneakingSubScene(int col, int row, int peopleAwake, GameCharacter vampire) {
            super(col, row, vampire);
            this.peopleAwake = peopleAwake;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (peopleAwake > 0) {
                state.println(vampire.getFirstName() + " must attempt to remain undetected " +
                        "by the inhabitants who are still awake.");
                SkillCheckResult result = vampire.testSkill(model, Skill.Sneak, 6 + peopleAwake);
                state.println("Sneaking " + result.asString() + ".");
                if (result.isFailure()) {
                    state.println("You have been spotted!");
                    state.printQuote(GameState.manOrWomanCap(MyRandom.flipCoin()), "HEY! Get out of here you creep!");
                    GeneralInteractionEvent.addToNotoriety(model, state, VampireFeedingState.NOTORIETY_FOR_BEING_SPOTTED);
                    state.println(vampire.getFirstName() + " flees the house with haste before the constables arrive. " +
                            "There is now much commotion among the townspeople and there is no point in " +
                            "continuing the prowl tonight.");
                    return getFailEdge();
                }
                state.printQuote(GameState.manOrWomanCap(MyRandom.flipCoin()),
                        MyRandom.sample(List.of("Did I hear something? It's probably just the wind.",
                                "What was that? Hmm... naw, it was nothing.",
                                "Huh, someone there? No... just my mind playing tricks on me.")));
            } else {
                state.println("It appears nobody is awake in the house, and " + vampire.getFirstName() +
                        " can move about freely without fear for being detected.");
            }
            return getSuccessEdge();
        }
    }

    private static class FeedingNode extends FeedingSubScene {
        private final int awake;
        private final int sleeping;

        public FeedingNode(int col, int row, int dwellers, int sleeping, GameCharacter vampire) {
            super(col, row, vampire);
            this.awake = dwellers - sleeping;
            this.sleeping = sleeping;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (sleeping == 0) {
                state.println("There is nobody sleeping in this house."); // TODO: Add mesmerize ability
                state.println(vampire.getFirstName() + " quietly leaves the house.");
                return getFailEdge();
            }
            for (int i = 0; i < sleeping; ++i) {
                AdvancedAppearance victim = PortraitSubView.makeRandomPortrait(Classes.None);
                state.println(vampire.getFirstName() + " approaches the bed of a " + victim.getRace().getName() + ".");
                state.print("Do you wish to feed on this victim? (Y/N) ");
                if (state.yesNoInput()) {
                    state.println(vampire.getFirstName() + " descends upon the " + victim.getRace().getName() +
                            " and sinks " + hisOrHer(vampire.getGender()) + " teeth into " + hisOrHer(victim.getGender()) + ".");
                    state.println("The " + victim.getRace().getName() + " gasps and for a moment it seems " + heOrShe(victim.getGender()) +
                            " is about to wake up, but then it appears the dark aura of the vampire lulls " + himOrHer(victim.getGender()) +
                            " back into a lethargic state. At last, " + vampire.getFirstName() + " can drink " +
                            hisOrHer(vampire.getGender()) + " fill.");
                    vampire.addToSP(9999);
                    state.println(vampire.getFullName() + " Stamina has fully recovered.");
                    return getSuccessEdge();
                } else {
                    state.println(vampire.getFirstName() + " steps away from the bed.");
                }
            }
            state.println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
            return getFailEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }
    }

    private static class ClimbingScene extends FeedingSubScene {
        private final int windowLevel;

        public ClimbingScene(int col, int row, int windowLevel, GameCharacter vampire) {
            super(col, row, vampire);
            this.windowLevel = windowLevel;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            state.print(vampire.getFirstName() + " climbs up the facade of the house... ");
            SkillCheckResult result = vampire.testSkill(model, Skill.Acrobatics, 6 + windowLevel * 2);
            state.println("Acrobatics " + result.asString() + ".");
            if (result.isSuccessful()) {
                state.println(" and nimbly slips in through the window.");
                return getSuccessEdge();
            }
            state.println("But lost " + GameState.hisOrHer(vampire.getGender()) + " foothold and falls down!");
            int hpBefore = vampire.getHP();
            int healthLoss = Math.min(vampire.getHP()-1, 3);
            vampire.addToHP(-healthLoss);
            state.println(vampire.getName() + " loses " + healthLoss + " HP.");
            model.getParty().partyMemberSay(model, vampire, List.of("Ouch.", "That hurt.", "Darn it!#",
                    "Ouch, my back!", "Ow... my butt."));
            return getFailEdge();
        }
    }
}

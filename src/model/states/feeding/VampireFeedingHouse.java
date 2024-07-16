package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.VampirismCondition;
import model.quests.*;
import model.states.GameState;
import model.states.events.GeneralInteractionEvent;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.subviews.ArrowMenuSubView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VampireFeedingHouse {
    public static final Sprite32x32 BAT_SPRITE = new Sprite32x32("batsubscene", "quest.png", 0x92,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    public static final Sprite32x32 EYE_SPRITE = new Sprite32x32("mesmerizesubscene", "quest.png", 0xA2,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    public static final Sprite32x32 BITE_SPRITE = new Sprite32x32("bitesubscene", "quest.png", 0xB2,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
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
    private final boolean canDoBat;
    private final boolean canDoMesmerize;

    private List<FeedingSubScene> subScenes;
    private List<FeedingJunction> junctions;
    private QuestFailNode failNode;
    private QuestSuccessfulNode successfulNode;
    private final GameCharacter vampire;
    private boolean sleepInfoGiven = false;
    private boolean batForm = false;
    private AdvancedAppearance portrait = null;
    private boolean openEyes;

    public VampireFeedingHouse(GameCharacter vampire) {
        this.width = MyRandom.randInt(1, 3);
        this.stories = MyRandom.randInt(1, 3);
        this.dwellers = MyRandom.randInt(1, width*stories + 1);
        this.sleeping = MyRandom.randInt(0, dwellers);
        this.lockDifficulty = (sleeping < dwellers && MyRandom.rollD6()==6) ? 0 : MyRandom.randInt(6, 8);
        color = MyRandom.sample(HOUSE_COLORS);
        windowOpen = MyRandom.randInt(2) * (stories - 1);
        this.vampire = vampire;
        VampirismCondition cond = ((VampirismCondition)vampire.getCondition(VampirismCondition.class));
        this.canDoBat = cond.hasBatAbility();
        this.canDoMesmerize = true; // TODO: Fix
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

        BatSubScene batScene = new BatSubScene(2, 2, vampire);
        ClimbingScene climbingScene = new ClimbingScene(2, 2, windowOpen, vampire);
        if (canDoBat) {
            subScenes.add(batScene);
        } else if (anyWindowsOpen()) {
            subScenes.add(climbingScene);
        }


        SneakingSubScene sneakScene = new SneakingSubScene(4, 3, dwellers - sleeping, vampire);
        subScenes.add(sneakScene);
        FeedingNode feedingNode = new FeedingNode(6, 3, dwellers, sleeping, vampire);
        subScenes.add(feedingNode);

        MesmerizeNode mesmerizeNode = new MesmerizeNode(6, 4, dwellers - sleeping, vampire);
        if (canDoMesmerize) {
            subScenes.add(mesmerizeNode);
        }

        GoToNextHouseNode goToNextHouse = new GoToNextHouseNode();

        List<QuestEdge> enterOptions = new ArrayList<>(List.of(new QuestEdge(subScenes.get(1)), new QuestEdge(goToNextHouse)));
        if (canDoBat) {
            enterOptions.add(new QuestEdge(batScene, QuestEdge.VERTICAL));
        } else if (anyWindowsOpen()) {
            enterOptions.add(new QuestEdge(climbingScene, QuestEdge.VERTICAL));
        }
        ChooseToEnter chooseToEnterJunc = new ChooseToEnter(1, 3, windowOpen, canDoBat, enterOptions);

        junctions.add(new FeedingStartNode(stakeOut));
        junctions.add(chooseToEnterJunc);
        junctions.add(goToNextHouse);

        stakeOut.connectSuccess(chooseToEnterJunc);
        unlockScene.connectSuccess(sneakScene);
        unlockScene.connectFail(goToNextHouse, QuestEdge.VERTICAL);
        sneakScene.connectSuccess(feedingNode);
        sneakScene.connectFail(failNode, QuestEdge.VERTICAL);
        feedingNode.connectSuccess(successfulNode);


        if (canDoMesmerize) {
            mesmerizeNode.connectSuccess(successfulNode);
            mesmerizeNode.connectFail(goToNextHouse);
            feedingNode.connectFail(mesmerizeNode, QuestEdge.VERTICAL);
        } else {
            feedingNode.connectFail(goToNextHouse, QuestEdge.VERTICAL);
        }

        if (canDoBat) {
            batScene.connectSuccess(sneakScene);
            batScene.connectFail(goToNextHouse, QuestEdge.VERTICAL);
        } else if (anyWindowsOpen()) {
            climbingScene.connectSuccess(sneakScene);
            climbingScene.connectFail(goToNextHouse, QuestEdge.VERTICAL);
        }

    }

    public void setBatForm(boolean b) {
        this.batForm = b;
    }

    public boolean isBatForm() {
        return batForm;
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

    public void setPortrait(AdvancedAppearance portrait) {
        this.portrait = portrait;
    }

    public AdvancedAppearance getPortrait() {
        return portrait;
    }

    public void setOpenEyes(boolean openEyes) {
        this.openEyes = openEyes;
    }

    public boolean isOpenEyes() {
        return openEyes;
    }

    private class FeedingStartNode extends FeedingJunction {
        public FeedingStartNode(QuestNode nextNdde) {
            super(0, 2, List.of(new QuestEdge(nextNdde, QuestEdge.VERTICAL)));
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
        private final boolean canDoBat;

        public ChooseToEnter(int col, int row, int windowOpen, boolean canDoBat, List<QuestEdge> questEdges) {
            super(col, row, questEdges);
            this.windowsOpen = windowOpen;
            this.canDoBat = canDoBat;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state) {
            if (windowsOpen > 0 || canDoBat) {
                if (canDoBat) {
                    state.println("You could easily fly into the house as a bat. How would you like to enter the house? ");
                } else {
                    state.print("There is a window open on the " + MyStrings.nthWord(windowsOpen) + " floor. How would you like to enter the house?");
                }
                int[] selectedAction = new int[1];
                model.setSubView(new ArrowMenuSubView(model.getSubView(),
                        List.of("Through front door", (canDoBat?"As bat":"Through window"), "Not at all"), 32, 32, ArrowMenuSubView.NORTH_WEST) {
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
                boolean result = model.getParty().doSoloSkillCheck(model, state, Skill.Sneak, 6 + peopleAwake);
                if (!result) {
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

    private class FeedingNode extends FeedingSubScene {
        private final int awake;
        private final int sleeping;

        public FeedingNode(int col, int row, int dwellers, int sleeping, GameCharacter vampire) {
            super(col, row, vampire);
            this.awake = dwellers - sleeping;
            this.sleeping = sleeping;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(BITE_SPRITE.getName(), new Point(xPos, yPos), BITE_SPRITE, 1);
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (sleeping == 0) {
                state.println("There is nobody sleeping in this house.");
                if (!canDoMesmerize) {
                    state.println(vampire.getFirstName() + " quietly leaves the house.");
                }
                return getFailEdge();
            }
            for (int i = 0; i < sleeping; ++i) {
                if (feedOnVictim(model, state, true)) {
                    return getSuccessEdge();
                }
            }
            if (!canDoMesmerize) {
                state.println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
            } else {
                state.println("There are no more people asleep in the house.");
            }
            return getFailEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }
    }

    private boolean feedOnVictim(Model model, GameState state, boolean isSleeping) {
        AdvancedAppearance victim = PortraitSubView.makeRandomPortrait(Classes.None);
        victim.setMascaraColor(victim.getRace().getColor());
        VampireFeedingHouse.this.setPortrait(victim);
        if (isSleeping) {
            state.println(vampire.getFirstName() + " approaches the bed of a " + victim.getRace().getName() + ".");
        } else {
            VampireFeedingHouse.this.setOpenEyes(true);
            state.println(vampire.getFirstName() + " approaches a " + victim.getRace().getName() + " and uses " +
                    state.hisOrHer(vampire.getGender()) + " mesmerizing stare on " + state.hisOrHer(victim.getGender()) + ".");
            model.getLog().waitForAnimationToFinish();
            VampireFeedingHouse.this.setOpenEyes(false);
        }
        state.print("Do you wish to feed on this victim? (Y/N) ");
        if (state.yesNoInput()) {
            VampireFeedingHouse.this.setOpenEyes(true);
            state.println(vampire.getFirstName() + " descends upon the " + victim.getRace().getName() +
                    " and sinks " + state.hisOrHer(vampire.getGender()) + " teeth into " + state.hisOrHer(victim.getGender()) + ".");
            model.getLog().waitForAnimationToFinish();
            VampireFeedingHouse.this.setOpenEyes(false);
            state.println("The " + victim.getRace().getName() + " gasps and for a moment it seems " + state.heOrShe(victim.getGender()) +
                    " is about to wake up, but then it appears the dark aura of the vampire lulls " + state.himOrHer(victim.getGender()) +
                    " back into a lethargic state. At last, " + vampire.getFirstName() + " can drink " +
                    state.hisOrHer(vampire.getGender()) + " fill.");
            vampire.addToSP(9999);
            state.println(vampire.getFullName() + " Stamina has fully recovered.");
            return true;
        }
        VampireFeedingHouse.this.setPortrait(null);
        if (isSleeping) {
            state.println(vampire.getFirstName() + " steps away from the bed.");
        } else {
            state.println(vampire.getFirstName() + " steps away from the " + victim.getRace().getName() + ".");
        }
        return false;
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

    private class BatSubScene extends FeedingSubScene {
        public BatSubScene(int col, int row, GameCharacter vampire) {
            super(col, row, vampire);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(BAT_SPRITE.getName(), new Point(xPos, yPos), BAT_SPRITE, 1);
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            model.getParty().partyMemberSay(model, vampire, List.of("Batform!", "Bat!", "Dark Wings!",
                    "Abracadabra!", "Shazam!", "Here I go!"));
            VampireFeedingHouse.this.setBatForm(true);

            state.print(vampire.getFirstName() + " flies into the house through ");
            if (getOpenWindow() > 0) {
                state.println("the open window.");
            } else {
                state.println("the chimney.");
            }
            return getSuccessEdge();
        }
    }

    private class MesmerizeNode extends FeedingSubScene {

        private final int awake;

        public MesmerizeNode(int col, int row, int awake, GameCharacter vampire) {
            super(col, row, vampire);
            this.awake = awake;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(EYE_SPRITE.getName(), new Point(xPos, yPos), EYE_SPRITE, 1);
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (awake == 0) {
                state.println("With no other suitable victims in the house, " + vampire.getFirstName() + " quietly leaves.");
                return getFailEdge();
            }
            for (int i = 0; i < awake; ++i) {
                if (feedOnVictim(model, state, false)) {
                    return getSuccessEdge();
                }
            }
            state.println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
            return getFailEdge();
        }
    }
}

package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.combat.conditions.VampirismCondition;
import model.quests.*;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
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
        this.canDoMesmerize = cond.hasMesmerizeAbility();
        makeNodes();
    }

    private void makeNodes() {
        this.junctions = new ArrayList<>();
        this.subScenes = new ArrayList<>();

        this.failNode = new QuestFailNode();
        this.successfulNode = new QuestSuccessfulNode(new Reward(0, 0,0 ), "");
        successfulNode.move(7, 3);

        StakeOutSubScene stakeOut = new StakeOutSubScene(0, 3, vampire, this);
        subScenes.add(stakeOut);
        UnlockDoorSubScene unlockScene = new UnlockDoorSubScene(2, 3, lockDifficulty, vampire);
        subScenes.add(unlockScene);

        BatSubScene batScene = new BatSubScene(2, 2, vampire, this);
        ClimbingScene climbingScene = new ClimbingScene(2, 2, windowOpen, vampire);
        if (canDoBat) {
            subScenes.add(batScene);
        } else if (anyWindowsOpen()) {
            subScenes.add(climbingScene);
        }

        SneakingSubScene sneakScene = new SneakingSubScene(4, 3, dwellers - sleeping, vampire);
        subScenes.add(sneakScene);
        FeedingNode feedingNode = new FeedingNode(6, 3, vampire, this);
        subScenes.add(feedingNode);

        MesmerizeNode mesmerizeNode = new MesmerizeNode(6, 4, vampire, this);
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
        ChooseToEnterJunction chooseToEnterJunc = new ChooseToEnterJunction(1, 3, windowOpen, canDoBat, enterOptions);

        junctions.add(new FeedingStartNode(stakeOut, vampire, this));
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

    public void setSleepInfoGiven(boolean b) {
        sleepInfoGiven = b;
    }

    public boolean canDoMesmerize() {
        return canDoMesmerize;
    }

    public boolean feedOnVictim(Model model, GameState state, boolean isSleeping) {
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

}

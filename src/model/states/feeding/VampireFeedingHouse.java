package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.combat.conditions.PoisonCondition;
import model.combat.conditions.SlowedCondition;
import model.combat.conditions.StrangenessCondition;
import model.combat.conditions.VampirismCondition;
import model.quests.*;
import model.races.Dwarf;
import model.races.ElvenRace;
import model.races.HumanRace;
import model.races.Race;
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
        this.sleeping = Math.max(MyRandom.randInt(0, dwellers), MyRandom.randInt(0, dwellers));
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

        this.failNode = new VampireFeedingFailedNode();
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

        MesmerizeNode mesmerizeNode = new MesmerizeNode(6, 2, vampire, this);
        if (canDoMesmerize) {
            subScenes.add(mesmerizeNode);
        }

        GoToNextHouseNode goToNextHouse = new GoToNextHouseNode();

        List<QuestEdge> enterOptions = new ArrayList<>(List.of(new QuestEdge(subScenes.get(1)), new QuestEdge(goToNextHouse, QuestEdge.VERTICAL)));
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
            model.getParty().enabledVampireLookFor(vampire);
            VampireFeedingHouse.this.setOpenEyes(true);
            state.println(vampire.getFirstName() + " descends upon the " + victim.getRace().getName() +
                    " and sinks " + state.hisOrHer(vampire.getGender()) + " teeth into " + state.hisOrHer(victim.getGender()) + " neck.");
            model.getLog().waitForAnimationToFinish();
            VampireFeedingHouse.this.setOpenEyes(false);
            state.println("The " + victim.getRace().getName() + " gasps and for a moment it seems " + state.heOrShe(victim.getGender()) +
                    " is about to wake up, but then it appears the dark aura of the vampire lulls " + state.himOrHer(victim.getGender()) +
                    " back into a lethargic state. At last, " + vampire.getFirstName() + " can drink " +
                    state.hisOrHer(vampire.getGender()) + " fill.");
            applyRaceSpecificEffect(model, state, vampire, victim);
            model.getLog().waitForAnimationToFinish();
            model.getParty().disableVampireLookFor(vampire);
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

    private void applyRaceSpecificEffect(Model model, GameState state, GameCharacter vampire, AdvancedAppearance victim) {
        if (victim.getRace().id() != vampire.getRace().id()) {
            if (victim.getRace().id() == Race.HALFLING.id()) {
                int spRecovered = MyRandom.rollD6();
                if (spRecovered + vampire.getSP() < vampire.getMaxSP()) {
                    state.println("The halfling's blood is not enough for " + vampire.getFirstName() + " to fully recover.");
                    vampire.addToSP(spRecovered);
                    state.println(vampire.getFirstName() + " recovers " + spRecovered + " Stamina.");
                } else {
                    fullyRecoverStamina(state, vampire);
                }
                return;
            } else if (victim.getRace().id() == Race.HALF_ORC.id()) {
                state.println("The half-orc's blood is slightly toxic to " + vampire.getFirstName() + ". " +
                        vampire.getFirstName() + " became poisoned.");
                vampire.addCondition(new PoisonCondition());
            } else if (victim.getRace() instanceof ElvenRace && !(vampire.getRace() instanceof ElvenRace)) {
                state.println("The purity of the elven blood burns " + vampire.getFirstName() + "!");
                if (vampire.getHP() < vampire.getMaxHP() / 2 && vampire.getHP() > 0) {
                    vampire.addToHP(-1);
                    state.println(vampire.getFirstName() + " loses 1 HP.");
                } else {
                    int loss = vampire.getMaxHP() / 2 - 1;
                    state.println(vampire.getFirstName() + " loses " + loss + " HP.");
                    vampire.addToHP(-loss);
                }
            } else if (victim.getRace().id() == Race.DWARF.id()) {
                state.println("The thickness of the dwarven blood weighs heavy inside of " + vampire.getFirstName() + "!");
                vampire.addCondition(new SlowedCondition(6, model.getDay()));
            } else if (victim.getRace() instanceof HumanRace && !(vampire.getRace() instanceof HumanRace)) {
                state.println("The richness of the human blood saturates " + vampire.getFirstName() +
                        "'s system and gives " + state.himOrHer(vampire.getGender()) + " an aura of strangeness.");
                vampire.addCondition(new StrangenessCondition(model.getDay()));
            }
        }
        fullyRecoverStamina(state, vampire);
    }

    private void fullyRecoverStamina(GameState state, GameCharacter vampire) {
        vampire.addToSP(9999);
        state.println(vampire.getFullName() + "'s Stamina has fully recovered.");
    }

}

package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.conditions.VampirismCondition;
import model.items.Inventory;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.quests.Quest;
import model.states.dailyaction.tavern.HireGuideAction;
import model.states.dailyaction.LodgingState;
import model.states.events.*;
import model.states.feeding.VampireFeedingState;
import model.tasks.BountyDestinationTask;
import model.tasks.DestinationTask;
import model.travellers.Traveller;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.LogView;
import view.help.HalfTimeDialog;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EveningState extends GameState {

    public static SubView subViewTent = new ImageSubView("thetent", "EVENING", "You make camp.");
    private static SubView subViewTevern = new ImageSubView("theinn", "EVENING", "You spend the night at the tavern.");

    private final boolean freeRations;
    private boolean freeLodging;
    private Quest goOnQuest;
    boolean doAutoSave;

    public EveningState(Model model, boolean freeLodging, boolean freeRations, boolean autoSave) {
        super(model);
        this.freeLodging = freeLodging;
        this.freeRations = freeRations;
        this.doAutoSave = autoSave;
    }

    public EveningState(Model model) {
        this(model, false, false, true);
    }

    @Override
    public final GameState run(Model model) {
        setSubView(model);
        print("Evening has come. ");
        model.getTutorial().evening(model);
        checkForQuest(model);
        checkForLeaderChange(model);
        checkBounties(model);
        checkTravellers(model);
        checkGuides(model);
        checkForVampireFeeding(model, this instanceof LodgingState);
        checkForNightTimeEvent(model);
        locationSpecificEvening(model);
        if (model.getDay() == 50) {
            model.transitionToDialog(new HalfTimeDialog(model.getView()));
        }
        super.stepToNextDay(model);
        return nextState(model);
    }

    private void checkForNightTimeEvent(Model model) {
        DailyEventState ambushEvent = model.getCurrentHex().getNightTimeAmbushEvent(model);
        if (ambushEvent != null) {
            GameState next = ambushEvent.run(model);
            if (next instanceof RunAwayState) {
                next.run(model);
                setSubView(model);
            }
        }
    }

    private void checkForLeaderChange(Model model) {
        if (model.getParty().size() < 3) {
            return;
        }
        List<GameCharacter> dissidents = new ArrayList<>(MyLists.filter(model.getParty().getPartyMembers(),
                (GameCharacter gc) -> gc.getAttitude(model.getParty().getLeader()) < 0));
        if (dissidents.isEmpty()) {
            return;
        }
        Collections.shuffle(dissidents);
        GameCharacter mainDissident = dissidents.get(0);
        List<GameCharacter> candidates = new ArrayList<>(model.getParty().getPartyMembers());
        candidates.remove(model.getParty().getLeader());
        candidates.remove(mainDissident);
        candidates.removeIf((GameCharacter gc) -> mainDissident.getAttitude(gc) < 0);

        if (candidates.isEmpty() && mainDissident.getAttitude(model.getParty().getLeader()) <= -10) {
            PartyMemberWantsToLeaveEvent wantsToLeaveEvent = new PartyMemberWantsToLeaveEvent(model);
            wantsToLeaveEvent.wantsToLeave(model, mainDissident, true);
            return;
        }

        candidates.add(mainDissident);
        Collections.sort(candidates, (gc1, gc2) -> {
            int total1 = gc1.getRankForSkill(Skill.Leadership) * 10 + gc1.getSpeed();
            int total2 = gc2.getRankForSkill(Skill.Leadership) * 10 + gc2.getSpeed();
            return total2 - total1;
        });

        partyMemberSay(mainDissident, MyRandom.sample(List.of("Perhaps it's time for new leadership?",
                "I think it's time for a new leader.", "This party needs new leadership, urgently.",
                "Who else thinks we need to appoint a new leader?")));
        if (dissidents.size() > 1) {
            partyMemberSay(dissidents.get(1), MyRandom.sample(List.of("I agree!", "Here here!", "Well said.",
                    "I concur.", "Couldn't have said it better myself!", "So true!")));
        }
        GameCharacter proposedLeader = candidates.get(0);
        if (proposedLeader == mainDissident) {
            partyMemberSay(mainDissident, "I think I would be a better leader.");
        } else {
            partyMemberSay(mainDissident, "I think " + proposedLeader.getFirstName() + " would be a great leader.");
        }
        print("Do you agree to make " + proposedLeader.getName() + " the leader of the party? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Fine... I give up. " + proposedLeader.getFirstName() + ", good luck...");
            model.getParty().setLeader(proposedLeader);
            leaderSay("Thanks. Okay people, follow my lead.");
            int dissidentAttitudeTowardNewLeader = mainDissident.getAttitude(model.getParty().getLeader());
            if (dissidentAttitudeTowardNewLeader < 0) {
                mainDissident.addToAttitude(model.getParty().getLeader(), -dissidentAttitudeTowardNewLeader);
            }
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc != model.getParty().getLeader()) {
                    gc.addToAttitude(model.getParty().getLeader(), 1);
                }
            }
            println(mainDissident.getName() + " has been appeased.");
        } else {
            if (MyRandom.flipCoin()) {
                leaderSay("No way " + mainDissident.getFirstName() + ".");
            } else {
                leaderSay("I refuse.");
            }
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc != model.getParty().getLeader() && gc.getAttitude(model.getParty().getLeader()) < 0) {
                    gc.addToAttitude(model.getParty().getLeader(), -3);
                }
            }
            println(mainDissident.getName() + " has been affronted.");
        }
    }

    public void setSubView(Model model) {
        if (showTentSubView()) {
            CollapsingTransition.transition(model, subViewTent);
        } else {
            CollapsingTransition.transition(model, subViewTevern);
        }
    }

    protected boolean showTentSubView() {
        return true;
    }

    protected void locationSpecificEvening(Model model) {
        if (freeLodging || model.getSpellHandler().creatureComfortsCastToday(model)) {
            println("The party receives food and lodging for free.");
            model.getParty().lodging(0);
        } else if (freeRations) {
            println("The party has received rations for free.");
            model.getParty().consumeRations(true);
        } else if (model.getCurrentHex().hasLodging()) {
            buyRations(model, this);
            lodging(model);
        } else {
            notLodging(model);
        }
    }

    protected GameState nextState(Model model) {
        if (model.getParty().isWipedOut()) {
            return new GameOverState(model);
        }
        if (this.goOnQuest == null) {
            if (doAutoSave && model.getSettings().autosaveEnabled()) {
                model.getLog().addAnimated(LogView.GRAY_COLOR + "Autosaving...\n" + LogView.DEFAULT_COLOR);
                model.getLog().waitForAnimationToFinish();
                model.saveToFile("auto");
            }
            return model.getCurrentHex().getDailyActionState(model);
        }
        Point beforeMoving = new Point(model.getParty().getPosition());
        goOnQuest.movePartyToRemoteLocation(model);
        return new QuestState(model, goOnQuest, beforeMoving);
    }

    protected void checkForQuest(Model model) {
        List<Quest> quests = new ArrayList<>();
        model.getMainStory().addQuests(model, quests);
        if (model.getCurrentHex().givesQuests()) {
            int mainQuests = quests.size();
            addHeldQuests(model, quests);
            addRandomQuests(model, quests, mainQuests);
            if (quests.size() == 0) {
                println("The party has not been offered any quests.");
            }
        }
        if (quests.size() > 0) {
            goOnQuest = offerQuests(model, this, quests);
        }
    }

    private void addHeldQuests(Model model, List<Quest> quests) {
        quests.addAll(model.getParty().getHeldQuests(model));
    }

    private void addRandomQuests(Model model, List<Quest> quests, int mainQuests) {
        int baseNumberOfQuests = 3;
        if (model.getQuestDeck().alreadyDone(model.getCurrentHex().getLocation())) {
            if (model.getQuestDeck().hadSuccessIn(model.getCurrentHex().getLocation())) {
                baseNumberOfQuests = 0;
            }
        }
        int numQuests = MyRandom.randInt(0, Math.max(0, baseNumberOfQuests + mainQuests - quests.size()));
        for (int i = numQuests; i > 0; --i) {
            Quest q;
            int tries = 0;
            do {
                q = model.getQuestDeck().getRandomQuest();
                if (tries++ > 1000) {
                    System.err.println("Abandoned getting random quests, tried 100 times");
                    return;
                }
            } while (model.getQuestDeck().alreadyDone(q) || quests.contains(q));
            q.setRemoteLocation(model);
            quests.add(q);
        }
    }

    public static Quest offerQuests(Model model, GameState state, List<Quest> quests) {
        Quest toReturn = null;
        state.println("The party has been offered " + MyStrings.numberWord(quests.size()) +
                " quest" + (quests.size() > 1?"s":"") + ".");
        state.print("Will you go tomorrow? ");
        boolean done = false;
        SubView previous = model.getSubView();
        SelectQuestSubView subView = new SelectQuestSubView(model.getSubView(), quests);
        model.setSubView(subView);
        do {
            model.getTutorial().questOffers(model);
            state.waitForReturnSilently();
            Point cursor = subView.getCursorPoint();
            if (subView.didSelectQuest()) {
                Quest q = subView.getSelectedQuest();
                List<String> options = new ArrayList<>(List.of("Accept", "Hold", "Back"));
                if (model.getParty().questIsHeld(q)) {
                    options.set(1, "Stop Holding");
                }
                int selectedOption = state.multipleOptionArrowMenu(model, cursor.x, cursor.y, options);
                if (selectedOption == 0) {
                    if (q.arePrerequisitesMet(model)) {
                        toReturn = q;
                        state.println("You have accepted quest '" + q.getName() + "'!");
                        if (model.getCurrentHex().getLocation() != null) {
                            model.getQuestDeck().accept(q, model.getCurrentHex().getLocation(), model.getDay());
                        }
                        done = true;
                    } else {
                        state.println(q.getPrerequisites(model));
                    }
                } else if (selectedOption == 1){
                    if (model.getParty().questIsHeld(q)) {
                        model.getParty().stopHoldingQuest(q);
                    } else {
                        if (q.canBeHeld()) {
                            model.getParty().holdQuest(q);
                            state.println("Quest will be held as long as you remain in your current location.");
                        } else {
                            state.println("That quest cannot be held.");
                        }
                    }
                }
            } else {
                done = true;
            }
        } while (!done);
        state.println("");
        model.setSubView(previous);
        return toReturn;
    }

    public static void buyRations(Model model, GameState state) {
        state.println("You can buy rations here at a rate of 5 per gold.");
        if (model.getParty().getGold() == 0) {
            state.println("But you can't afford any.");
            return;
        }
        int maxBuy = (model.getParty().getCarryingCapacity() - model.getParty().getEncumbrance()) /
                Inventory.WEIGHT_OF_FOOD;
        if (maxBuy <= 0) {
            state.println("You cannot carry anymore rations.");
            return;
        }

        final boolean[] done = {false};
        while (!done[0]) {
            maxBuy = (model.getParty().getCarryingCapacity() - model.getParty().getEncumbrance()) /
                    Inventory.WEIGHT_OF_FOOD;
            if (maxBuy <= 0 || model.getParty().getGold() == 0) {
                break;
            }
            String sitch = "Your party can carry an additional ";
            int cost = (int) Math.ceil(maxBuy / 5.0);
            if (cost > model.getParty().getGold()) {
                sitch = "You can afford to buy ";
                maxBuy = model.getParty().getGold() * 5;
                cost = model.getParty().getGold();
            }
            final int finalCost = cost;
            state.print(sitch + maxBuy + " rations.");
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    List.of("Buy 5", "Buy Max", "Done"), 28, 10, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    if (cursorPos == 0) {
                        model.getParty().addToGold(-1);
                        model.getParty().addToFood(5);
                    } else if (cursorPos == 1) {
                        model.getParty().addToGold(-finalCost);
                        model.getParty().addToFood(finalCost * 5);
                    } else {
                        done[0] = true;
                    }
                    model.setSubView(getPrevious());
                }
            });
            state.waitForReturn();
        }
    }

    protected void lodging(Model model) {
        int cost = lodgingCost(model);
        if (!partyCanAffordLodging(model)) {
            print("You cannot afford to pay for food and lodging here. ");
            notLodging(model);
        } else {
            print("Do you want to pay " + cost + " gold for food and lodging (y/n)? ");
            if (yesNoInput()) {
                model.getParty().lodging(cost);
            } else {
                notLodging(model);
            }
        }
    }

    public static boolean partyCanAffordLodging(Model model) {
        int cost = lodgingCost(model);
        return cost <= model.getParty().getGold();
    }

    public static int lodgingCost(Model model) {
        return model.getParty().size() + (model.getParty().size()+1) / 2 + model.getParty().getHorseHandler().size();
    }

    public static List<String> lodgingBreakdown(Model model) {
        List<String> result = new ArrayList<>();
        result.add("Food & Drink x" + model.getParty().size());
        int rooms = (model.getParty().size()+1) / 2;
        result.add("Room x" + rooms);
        if (model.getParty().hasHorses()) {
            result.add("Stable x" + model.getParty().getHorseHandler().size());
        }
        return result;
    }
    protected void notLodging(Model model) {
        if (hasEnoughFood(model)) {
            println("The party makes camp and consumes rations.");
            model.getParty().consumeRations();
            if (model.getParty().size() > 1) {
                model.getParty().randomPartyMemberSay(model, List.of(
                        "I think I'm lying on a root.#",
                        "This tent is nice, but a bit small.",
                        "Can somebody feed that fire, it's dying.",
                        "Anybody know a camp story?",
                        "I need some rest.",
                        "Let's hit the sack people.",
                        "Who's been using my sleeping bag?",
                        "Tomorrow's another day.",
                        "I'm about to fall asleep.",
                        "Bedtime. At least for me.",
                        "These rations are a bit stale.",
                        "It's been an alright day I suppose.",
                        "I wish we would stay at a tavern.",
                        "Yaaawn!", "Good night everybody."));
            }
        } else {
            print("There are not enough rations for everybody. ");
            List<GameCharacter> remaining = new ArrayList<>();
            remaining.addAll(model.getParty().getPartyMembers());
            while (model.getParty().getFood() > 0) {
                print("Please select who gets to eat: ");
                GameCharacter gc = model.getParty().partyMemberInput(model, this, remaining.get(0));
                if (remaining.contains(gc)) {
                    println(gc.getFirstName() + " consumes rations.");
                    model.getParty().addToFood(-1);
                    gc.addToHP(1);
                    remaining.remove(gc);
                } else {
                    println("That party member has already consumed rations this evening.");
                }
            }
            starveAndKill(model, remaining);
        }
    }

    protected void starveAndKill(Model model, List<GameCharacter> partyMembers) {
        List<GameCharacter> toRemove = new ArrayList<>();
        for (GameCharacter gc : partyMembers) {
            if (gc.getSP() > 0) {
                gc.addToSP(-1);
            } else {
                gc.addToHP(-1);
                if (gc.isDead()) {
                    toRemove.add(gc);
                }
            }
            println(gc.getFirstName() + " starves.");
            if (gc != model.getParty().getLeader()) {
                gc.addToAttitude(model.getParty().getLeader(), -3);
            }
        }

        for (GameCharacter gc : toRemove) {
            println(gc.getName() + " has starved to death! Press enter to continue.");
            waitForReturn();
            if (!DailyEventState.didResurrect(model, this, gc)) {
                model.getParty().remove(gc, true, false, 0);
            }
        }

    }

    private boolean hasEnoughFood(Model model) {
        return model.getParty().getFood() >= model.getParty().size();
    }

    private void checkTravellers(Model model) {
        for (Traveller t : new ArrayList<>(model.getParty().getActiveTravellers())) {
            HexLocation loc = t.getDestinationLocation(model);
            if (model.getWorld().getPositionForLocation(loc).equals(model.getParty().getPosition())) {
                t.complete(model, this);
            } else if (t.getRemainingDays(model) == 0) {
                t.complain(model, this);
            } else if (t.getRemainingDays(model) == -4) {
                t.abandon(model, this);
            }
        }
    }

    private void checkGuides(Model model) {
        if (model.getParty().getGuide() > 0) {
            model.getParty().addToGuide(-1);
            if (model.getParty().getGuide() == 0) {
                HireGuideAction.extendContract(model, this);
            }
        }
    }

    private void checkBounties(Model model) {
        for (DestinationTask dt : model.getParty().getDestinationTasks()) {
            if (dt instanceof BountyDestinationTask) {
                ((BountyDestinationTask)dt).turnInIfAble(model, this);
            }
        }
    }

    private void checkForVampireFeeding(Model model, boolean inTavern) {
        boolean isUrbanLocation = model.getCurrentHex().getLocation() != null &&
                (model.getCurrentHex().getLocation() instanceof UrbanLocation);
        boolean vampiresInParty = MyLists.any(model.getParty().getPartyMembers(), CheckForVampireEvent::isVampire);

        if (isUrbanLocation && !vampiresInParty && MyRandom.rollD6() + MyRandom.rollD6() <= 3) {
            new VampireProwlNightEvent(model, inTavern).run(model);
            return;
        }

        List<GameCharacter> vampires = MyLists.filter(model.getParty().getPartyMembers(),
                                    gc -> CheckForVampireEvent.isVampire(gc) && gc.getSP() < gc.getMaxSP());
        vampires.sort(Comparator.comparingInt(GameCharacter::getSP));
        List<GameCharacter> nonVampires = MyLists.filter(model.getParty().getPartyMembers(),
                gc -> !CheckForVampireEvent.isVampire(gc));
        while (!vampires.isEmpty()) {
            GameCharacter vampire = vampires.remove(0);
            if (isUrbanLocation) {
                if (partyVampireFeedInUrbanLocation(model, vampire, nonVampires)) {
                    break;
                }

            } else if (!nonVampires.isEmpty()) {
                print(vampire.getName() + " can feel the vampiric urge to feed. Does " + heOrShe(vampire.getGender()) +
                        " feed on another party member? (Y/N) ");
                if (yesNoInput()) {
                    new FeedOnPartyMemberEvent(model, vampire).doTheEvent(model);
                    break;
                }
            }
        }
    }

    private boolean partyVampireFeedInUrbanLocation(Model model, GameCharacter vampire, List<GameCharacter> nonVampires) {
        println(vampire.getName() + " can feel the vampiric urge to feed. Does " + heOrShe(vampire.getGender()) +
                " go on the prowl tonight? ");
        model.getLog().waitForAnimationToFinish();
        List<String> options = new ArrayList<>(List.of("Find victim in town", "Refrain"));
        if (!nonVampires.isEmpty()) {
            options.add(0, "Feed on party member");
        }
        int chosen = multipleOptionArrowMenu(model, 24, 24, options);
        if (options.get(chosen).contains("party")) {
            new FeedOnPartyMemberEvent(model, vampire).doTheEvent(model);
        } else if (options.get(chosen).contains("town")) {
            new VampireFeedingState(model, vampire).run(model);
        } else {
            return false;
        }
        return true;
    }

}

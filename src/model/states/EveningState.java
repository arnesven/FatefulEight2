package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.items.Inventory;
import model.quests.MainQuest;
import model.quests.Quest;
import util.MyRandom;
import util.MyStrings;
import view.help.HalfTimeDialog;
import view.subviews.*;

import java.util.ArrayList;
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
        locationSpecificEvening(model);
        if (model.getDay() == 50) {
            model.transitionToDialog(new HalfTimeDialog(model.getView()));
        }
        super.stepToNextDay(model);
        return nextState(model);
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
                model.getLog().addAnimated("Autosaving...\n");
                model.saveToFile("auto");
            }
            return model.getCurrentHex().getDailyActionState(model);
        }
        return new QuestState(model, goOnQuest);
    }

    protected void checkForQuest(Model model) {
        List<Quest> quests = new ArrayList<>();
        model.getMainStory().addQuests(model, quests);
        if (model.getCurrentHex().givesQuests()) {
            if (quests.size() == 0 &&
                    !model.getQuestDeck().alreadyDone(model.getCurrentHex().getLocation())) {
                randomQuests(model, quests);
            }
            if (quests.size() == 0) {
                println("The party has not been offered any quests.");
            }
        }
        if (quests.size() == 0) {
            return;
        }

        println("The party has been offered " + MyStrings.numberWord(quests.size()) + " quest" + (quests.size() > 1?"s":"") + ".");
        print("Will you go tomorrow? ");
        boolean done = false;
        do {
            SubView previous = model.getSubView();
            SelectQuestSubView subView = new SelectQuestSubView(model.getSubView(), quests);
            model.setSubView(subView);
            waitForReturn();
            if (subView.didAcceptQuest()) {
                Quest q = subView.getSelectedQuest();
                if (q.arePrerequisitesMet(model)) {
                    this.goOnQuest = q;
                    println("You have accepted quest '" + q.getName() + "'!");
                    if (model.getCurrentHex().getLocation() != null) {
                        model.getQuestDeck().accept(q, model.getCurrentHex().getLocation(), model.getDay());
                    }
                    done = true;
                } else {
                    println(q.getPrerequisites(model));
                }
            } else {
                println("You rejected the quest" + (quests.size() > 1 ? "s" : "") + ".");
                done = true;
            }
            model.setSubView(previous);
        } while (!done);
    }

    private void randomQuests(Model model, List<Quest> quests) {
        int numQuests = 2;
        int dieRoll = MyRandom.rollD10();
        if (dieRoll == 1) {
            return;
        } else if (dieRoll < 6) {
            numQuests = 1;
        }
        while (quests.size() < numQuests) {
            Quest q;
            do {
                q = model.getQuestDeck().getRandomQuest();
            } while (model.getQuestDeck().alreadyDone(q) || quests.contains(q));
            quests.add(q);
        }
    }

    public static void buyRations(Model model, GameState state) {
        state.println("You can buy rations here at a rate of 5 per gold.");
        if (model.getParty().getGold() == 0) {
            state.println("But you can't afford any.");
            return;
        }
        int maxBuy = (model.getParty().getCarryingCapacity() - model.getParty().getInventory().getTotalWeight()) /
                Inventory.WEIGHT_OF_FOOD;
        if (maxBuy <= 0) {
            state.println("You cannot carry anymore rations.");
            return;
        }

        final boolean[] done = {false};
        while (!done[0]) {
            maxBuy = (model.getParty().getCarryingCapacity() - model.getParty().getInventory().getTotalWeight()) /
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
                    List.of("Buy 5", "Buy Max", "Done"), 32, 18, ArrowMenuSubView.NORTH_WEST) {
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
                        "These rations are a bit stale.",
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

    private void starveAndKill(Model model, List<GameCharacter> partyMembers) {
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
}

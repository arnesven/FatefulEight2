package model.states;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.quests.Quest;
import view.subviews.ArrowMenuSubView;
import view.subviews.DailyActionMenu;

import java.util.List;

public class EveningState extends GameState {
    private final boolean freeRations;
    private boolean freeLodging;
    private Quest goOnQuest;

    public EveningState(Model model, boolean freeLodging, boolean freeRations) {
        super(model);
        this.freeLodging = freeLodging;
        this.freeRations = freeRations;
    }

    public EveningState(Model model) {
        this(model, false, false);
    }

    @Override
    public final GameState run(Model model) {
        setCurrentTerrainSubview(model);
        print("Evening has come. ");
        model.getTutorial().evening(model);
        checkForQuest(model);
        locationSpecificEvening(model);
        super.stepToNextDay(model);
        return nextState(model);
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
        if (this.goOnQuest == null) {
            model.saveToFile("auto");
            return model.getCurrentHex().getDailyActionState(model);
        }
        return new QuestState(model, goOnQuest);
    }

    protected void checkForQuest(Model model) {
        if (model.getCurrentHex().givesQuests() &&
                !model.getQuestDeck().alreadyDone(model.getCurrentHex().getLocation())) {
            Quest q = model.getQuestDeck().getRandomQuest();
            println("The party is offered a quest by " + q.getProviderName() + ".");
            print(q.getBeforehandInfo());
            print(" Will you go tomorrow (Y/N)? ");
            if (yesNoInput()) {
                this.goOnQuest = q;
                println("You accepted the quest.");
                model.getQuestDeck().accept(q, model.getCurrentHex().getLocation());
                q.accept(model.getParty());
            } else {
                println("You declined the quest.");
            }
        }
    }

    public static void buyRations(Model model, GameState state) {
        state.println("You can buy rations here at a rate of 5 per gold.");
        if (model.getParty().getGold() == 0) {
            state.println("But you can't afford any.");
            return;
        }
        if (model.getParty().getFood() == model.getParty().rationsLimit()) {
            state.println("You cannot carry anymore rations.");
            return;
        }

        final boolean[] done = {false};
        while (model.getParty().getInventory().getFood() < model.getParty().rationsLimit() && !done[0]) {
            int maxBuy = model.getParty().rationsLimit() - model.getParty().getInventory().getFood();
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
        return 2 * model.getParty().size();
    }

    protected void notLodging(Model model) {
        if (hasEnoughFood(model)) {
            println("The party makes camp and consumes rations.");
            model.getParty().consumeRations();
        } else {
            println("There are not enough rations for everybody. Everybody starves.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getSP() > 0) {
                    gc.addToSP(-1);
                } else if (gc.getHP() > 2) {
                    gc.addToHP(-1);
                }
            }
        }
    }

    private boolean hasEnoughFood(Model model) {
        return model.getParty().getFood() >= model.getParty().size();
    }
}

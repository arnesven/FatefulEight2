package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.quests.Quest;

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
    public GameState run(Model model) {
        setCurrentTerrainSubview(model);
        print("Evening has come. ");
        checkForQuest(model);
        if (freeLodging) {
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
        model.getTutorial().evening(model);
        super.stepToNextDay(model);
        return nextState(model);
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
        while (model.getParty().getInventory().getFood() < model.getParty().rationsLimit()) {
            int maxBuy = model.getParty().rationsLimit() - model.getParty().getInventory().getFood();
            String sitch = "Your party can carry an additional ";
            int cost = (int)Math.ceil(maxBuy / 5.0);
            if (cost > model.getParty().getGold()) {
                sitch = "You can afford to buy ";
                maxBuy = model.getParty().getGold() * 5;
                cost = model.getParty().getGold();
            }
            state.print(sitch + maxBuy + " rations.");
            state.print(" Do you want to Buy Max (M), Buy 5 (B) or are you done (Q)? ");
            char choice = state.lineInput().toUpperCase().charAt(0);
            if (choice == 'M') {
                model.getParty().addToGold(-cost);
                model.getParty().addToFood(cost*5);
                break;
            } else if (choice == 'Q') {
                break;
            } else if (choice == 'B') {
                model.getParty().addToGold(-1);
                model.getParty().addToFood(5);
            }
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
        // TODO: Different ration cost in different locations (Village/Inn = 2, Castle = 3, Temple = 0)
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
            println("There are not enough rations for everybody. Everybody starves and sleeps outside.");
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

package model.states.dailyaction.tavern;

import model.Model;
import model.actions.Loan;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.DailyActionState;
import model.states.GameState;
import model.states.events.CrimsonAssassinsInvitationEvent;
import model.tasks.AssassinationDestinationTask;
import model.tasks.WritOfExecution;
import util.MyLists;
import util.MyStrings;
import view.subviews.ArrowMenuSubView;
import view.subviews.SubView;
import view.subviews.TavernSubView;

import java.util.ArrayList;
import java.util.List;

public class TakeLoanAction extends GameState {

    public TakeLoanAction(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        List<String> options = new ArrayList<>();
        if (model.getParty().getNotoriety() > 0) {
            options.add("Clear bounty");
        }
        if (model.getParty().getLoan() == null) {
            options.add("Take loan");
        } else {
            options.add("Repay loan");
        }
        if (CrimsonAssassinsInvitationEvent.alreadyDoneEvent(model)) {
            options.add("Ask about writs");
        }

        if (options.size() == 1) {
            if (model.getParty().getLoan() == null) {
                offerLoan(model);
            } else {
                repayLoan(model);
            }
        } else {
            agentSay(model, "What can I do for you brother?");
            int choice = multipleOptionArrowMenu(model, 28, 20, options);
            if (options.get(choice).equals("Clear bounty")) {
                offerToClearNotoriety(model);
            } else if (options.get(choice).equals("Take loan")) {
                offerLoan(model);
            } else if (options.get(choice).equals("Repay loan")) {
                repayLoan(model);
            } else {
                offerWrit(model);
            }
        }
        return new DailyActionState(model);
    }

    private void repayLoan(Model model) {
        agentSay(model,  "You still owe us money brother.");
        int cost = model.getParty().getLoan().repayCost();
        if (cost > model.getParty().getGold()) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Don't worry, I'll get the money.");
        } else {
            print("Do you wish to repay your loan (" + cost + " gold)? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                        "Fine, take it. Now go tell your cronies to back off.");
                model.getParty().addToGold(-cost);
                model.getParty().setLoan(null);
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Not right now...");
            }
        }
    }

    private void agentSay(Model model, String text) {
        SubView view = model.getSubView();
        if (view instanceof TavernSubView) {
            ((TavernSubView)view).addCalloutAtAgentOrGuide(text.length());
        }
        printQuote("Brotherhood Agent", text);
    }

    private void offerLoan(Model model) {
        agentSay(model,  "Low on cash? The brotherhood will help you out.");
        model.getTutorial().loans(model);
        println("Do you wish to take a loan?");
        int choice = multipleOptionArrowMenu(model, 28, 20, List.of("Small Loan (50)", "Large Loan (100)", "No thank you!"));
        if (choice == 0) {
            model.getParty().setLoan(new Loan(50, model.getDay()));
            model.getParty().addToGold(50);
        } else if (choice == 1) {
            model.getParty().setLoan(new Loan(100, model.getDay()));
            model.getParty().addToGold(100);
        }
        if (choice < 2) {
            agentSay(model,  "Spend it wisely brother. We expect you to pay us back " +
                    model.getParty().getLoan().repayCost() + " gold within " + Loan.REPAY_WITHIN_DAYS + " days.");
            agentSay(model,  "Don't make us come after you!");
            model.getParty().randomPartyMemberSay(model, List.of("Relax. You'll get your money.",
                    "Don't worry. You can trust us!", "We won't!", "Ooh, scary!",
                    "Yeah yeah. That's what you always say."));
        }
    }

    private void offerToClearNotoriety(Model model) {
        if (model.getParty().getNotoriety() > 0) {
            agentSay(model,  "Oh it's you! Yeah I've heard of you, you're a wanted " +
                    (model.getParty().getLeader().getGender()?"woman":"man") + " around these parts.");
            leaderSay("So what? I'm not afraid of the law.");
            agentSay(model,  "Even so... My organization is well connected. I could make " +
                    "your troubles go away. For a small sum of course.");
            leaderSay("Really. What do you propose?");
            int cost = model.getParty().getNotoriety() * 3;
            agentSay(model,  "Just give me " + cost + " gold, and I'll make sure the authorities forget your face.");
            if (cost <= model.getParty().getGold()) {
                print("Do you agree? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("Sounds like a good deal. Here's the coin.");
                    model.getParty().addToGold(-cost);
                    model.getParty().addToNotoriety(-model.getParty().getNotoriety());
                    agentSay(model,  "Thank you. Go enjoy your innocence. I hope it will last, " +
                            "and if it doesn't, well you know where to find me.");
                } else {
                    leaderSay("I'll have to think about it some more.");
                }
            } else {
                leaderSay("I'll have to think about it some more.");
            }
        }
    }

    private void offerWrit(Model model) {
        leaderSay("Got any writs of execution for me?");
        if (MyLists.any(model.getParty().getDestinationTasks(),
                dt -> dt instanceof AssassinationDestinationTask && !dt.isCompleted() && !dt.isFailed(model))) {
            agentSay(model, "I heard you are already working on one. Come back when you're done with that one.");
        } else {
            WritOfExecution writ = new WritOfExecution(model);
            UrbanLocation castleOrTown = (UrbanLocation) model.getWorld().getHex(writ.getPosition()).getLocation();
            String deadlineDays = MyStrings.numberWord(WritOfExecution.getDeadlineInDays());
            agentSay(model, "As a matter of fact, I do. Got one here from the Crimson Assassins. It's for a " +
                    writ.getRace().getName().toLowerCase() + " in " + castleOrTown.getPlaceName() + ". You know the deal, " +
                    "If you take it, you get " + WritOfExecution.getPayment() + " gold, but you only have " +
                    deadlineDays + " days to complete it. Interested?"); // TODO: Add tutorial chapter
            print("Do you accept the writ? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Yes. Give me the gold.");
                println("The agent hands you " + WritOfExecution.getPayment() + " gold.");
                model.getParty().addToGold(WritOfExecution.getPayment());
                JournalEntry.printJournalUpdateMessage(model);
                model.getParty().addDestinationTask(new AssassinationDestinationTask(writ));
            } else {
                leaderSay("I think " + iOrWe() + " will pass on that one.");
            }

        }
    }
}

package model.states.dailyaction;

import model.Model;
import model.actions.Loan;
import model.states.DailyActionState;
import model.states.GameState;
import view.subviews.ArrowMenuSubView;

import java.util.List;

public class TakeLoanAction extends GameState {
    public TakeLoanAction(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        if (model.getParty().getLoan() == null) {
            println("Brotherhood Agent: \"Low on cash? The brotherhood will help you out.\"");
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
                println("Brotherhood Agent: \"Spend it wisely brother. We expect you to pay us back " +
                        model.getParty().getLoan().repayCost() + " gold within " + Loan.REPAY_WITHIN_DAYS + " days.\"");
                println("Brotherhood Agent: \"Don't make us come after you!\"");
                model.getParty().randomPartyMemberSay(model, List.of("Relax. You'll get your money.",
                        "Don't worry. You can trust us!", "We won't!", "Ooh, scary!",
                        "Yeah yeah. That's what you always say."));
            }
        } else {
            println("Brotherhood Agent: \"You still owe us money brother.\"");
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
        return new DailyActionState(model);
    }
}

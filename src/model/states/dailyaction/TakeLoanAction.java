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
            println("Loan Shark: \"Low on cash? The brotherhood will help you out.\"");
            print("Do you wish to take a loan?");
            int choice = multipleOptionArrowMenu(model, 28, 20, List.of("Small Loan (50)", "Large Loan (100)", "No thank you!"));
            if (choice == 0) {
                model.getParty().setLoan(new Loan(50, model.getDay()));
                model.getParty().addToGold(50);
            } else if (choice == 1) {
                model.getParty().setLoan(new Loan(100, model.getDay()));
                model.getParty().addToGold(100);
            }
            if (choice < 2) {
                println("Loan Shark: \"Spend it wisely brother.\"");
            }
        } else {
            println("Loan Shark: \"You still owe us money brother.\"");
            int cost = model.getParty().getLoan().repayCost();
            if (cost > model.getParty().getGold()) {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Don't worry, I'll get the money.");
            } else {
                print("Do you wish to repay your loan (" + cost + " gold)? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                            "Fine, take it. Now go tell your cronies to back off.");
                    model.getParty().addToGold(-cost);
                } else {
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Not right now...");
                }
            }
        }
        return new DailyActionState(model);
    }
}

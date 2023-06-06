package model.tutorial;

import model.actions.Loan;
import view.GameView;
import view.help.HelpDialog;

public class TutorialLoans extends HelpDialog {
    private static final String TEXT = "Loans can be taken in taverns at towns and castles " +
            "from an organization known as The Brotherhood.\n\n" +
            "Every third day, a Brotherhood Agent shows up at the tavern and offers loans. " +
            "You can take a small loan (50 gold) " +
            "or a big loan (100 gold). You can only have one loan at a time.\n\n" +
            "Loans are repaid to any Brotherhood Agent, at an interest of 50%. The loan is expected to " +
            "be repaid within " + Loan.REPAY_WITHIN_DAYS + " days. After which the Brotherhood will " +
            "send cronies after you to remind you of your debt. You can not take loans toward the end of your career.";

    public TutorialLoans(GameView view) {
        super(view, "Loans", TEXT);
    }
}

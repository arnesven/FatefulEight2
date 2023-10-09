package view.help;

import view.GameView;

public class TutorialOtherParties extends HelpDialog {
    private static final String TEXT =
            "You may encounter other parties of adventurers in your travels. You can interact with other parties in " +
            "a number of ways.\n\n" +
            "Trade: Buy things from the other party, or sell items you don't need anymore. When buying, prices " +
            "will be based on the other party's leader's attitude toward you.\n\n" +
            "Attack: Initiate combat with the other party. Other parties may initiate combat with you if you annoy " +
            "them too much.\n\n" +
            "Offer: Give a monetary gift, or food to the other party. Or cook them a meal or entertain them in some " +
            "other way. These may be ways for you to improve the other party's attitudes toward you.\n\n" +
            "Talk: Talk to the members of the other party to find out the internal dynamics of the party. " +
            "You may even persuade them to join your party instead. In some cases the party leader may agree " +
            "to merge the parties, if conditions are right. The other party's leader will not want to merge the parties " +
            "if the resulting party would be too large, if the difference in levels would be to high, or if he or she " +
            "dislikes you.";

    public TutorialOtherParties(GameView view) {
        super(view, "Other Parties", TEXT);
    }
}

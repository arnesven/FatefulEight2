package view.help;

import view.GameView;

public class TutorialWages extends HelpDialog {
    private static final String TEXT =
            "So you just got a big pay day? Then your party members will expect you to pay them their wages. " +
            "The more you pay them, the more content they will be, and their attitude toward the party leader will increase." +
            " Of course, you could just keep " +
            "the money for yourself, but that will sow serious discontent among the party members.\n\n" +
            "You can also pay wages to any single party member while you are visiting an Inn or Tavern.";

    public TutorialWages(GameView view) {
        super(view, "Wages", TEXT);
    }
}

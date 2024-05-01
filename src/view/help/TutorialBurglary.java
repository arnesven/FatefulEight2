package view.help;

import view.GameView;

public class TutorialBurglary extends HelpDialog {
    private static final String TEXT =
            "During evening, if you are in a town or castle, you can attempt to burgle a shop. " +
            "Not all party members need to participate in the burglary attempt. If you are successful in " +
            "breaking into the shop you can steal any number of items from the shop's stock. " +
            "However, there is a limit to how much loot the burgling characters can carry.\n\n" +
            "The more items you steal the more difficult it will be to make your escape, which means succeeding in " +
            "a collective sneak skill check. A failed escape attempt will result in your crime being noticed and " +
            "reported to the authorities, which will increase your Notoriety.\n\n" +
            "Think twice about what you steal. Stealing too many items from a shop may put " +
            "it out of business permanently!";

    public TutorialBurglary(GameView view) {
        super(view, "Burglary", TEXT);
    }
}

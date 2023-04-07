package model.tutorial;

import view.GameView;
import view.help.HelpDialog;

public class TutorialAlignment extends HelpDialog {
    private static final String TEXT =
            "Alignment is a measure which describes how aligned your party is " +
            "with law and order. It fully depends on the the party member's respective classes.\n\n" +
            "CLASS       ALIGNMENT MODIFIER\n" +
            "Assassin            -2\n" +
            "Black Knight        -2\n" +
            "Thief               -1\n" +
            "Sorcerer            -1\n" +
            "Witch               -1\n" +
            "Barbarian           -1\n" +
            "Spy                 -1\n" +
            "Priest              +1\n" +
            "Paladin             +1\n" +
            "Noble               +1\n\n" +
            "Characters who's class is not listed in the table above do not contribute " +
            "to the party's alignment.";

    public TutorialAlignment(GameView view) {
        super(view, "Alignment", TEXT);
    }
}

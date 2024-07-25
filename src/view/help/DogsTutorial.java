package view.help;

import view.GameView;

public class DogsTutorial extends HelpDialog {
    private static final String TEXT =
            "Dogs can assist you in your journeys. Dogs are good at finding things and may lead " +
            "you to more favorable events.\n\n" +
            "Dogs can accompany you almost anywhere and never cost any rations to keep.\n" +
            "Your dog will show up in the Inventory view under Mounts";

    public DogsTutorial(GameView view) {
        super(view, "Dogs", TEXT);
    }
}

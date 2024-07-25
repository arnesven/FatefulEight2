package view.help;

import view.GameView;

public class DogsTutorial extends HelpDialog {
    private static final String TEXT = "Dogs can assist you in your journeys. Dogs are good at finding things.";

    public DogsTutorial(GameView view) {
        super(view, "Dogs", TEXT);
    }
}

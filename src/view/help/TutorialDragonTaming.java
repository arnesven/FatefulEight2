package view.help;

import view.GameView;

public class TutorialDragonTaming extends HelpDialog {
    private static final String TEXT = "The Dragon Taming Spell lets the " +
            "caster attempt to tame dragons. Once a dragon is tamed it will obey its master, " +
            "fight for them in combat, and even let the master (and others) ride on its back.\n\n" +
            "A dragon can carry at most 4 characters on its back.\n\n" +
            "Tamed dragons will show up under 'Mounts' in the Inventory view." +
            "A tamed dragon will disappear if its master leaves the party or dies.";

    public TutorialDragonTaming(GameView view) {
        super(view, "Dragon Taming", TEXT);
    }
}

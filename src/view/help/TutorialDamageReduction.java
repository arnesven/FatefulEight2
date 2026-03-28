package view.help;

import view.GameView;

public class TutorialDamageReduction extends HelpDialog {
    private static final String[] TEXT = new String[]{"Enemies do not have Armor Points like characters, " +
            "instead they have Damage Reduction.\\" +
            "An enemy with Physical Damage Reduction (PDR) will reduce each incoming physical attack by the same amount.\\" +
            "An enemy with Magical Damage Reduction (MDR) will reduce each incoming magical attack by the same amount.\\"};

    public TutorialDamageReduction(GameView view) {
        super(view, "Damage Reduction", TEXT);
    }
}

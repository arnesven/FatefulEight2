package view.help;

import view.GameView;

public class TutorialWeaponPairing extends HelpDialog {
    private static final String TEXT = "Most one-handed weapons can be paired together, enabling a character " +
            "to dual-wield in combat. Naturally, fighting with two weapons is more difficult. " +
            "Weapon pairing can be performed from the Inventory Menu.\n\n" +
            "When pairing two weapons together the resulting 'weapon' has the following qualities:\n" +
            "-It is two-handed.\n\n" +
            "-The damage table will be an average of both weapons.\n\n" +
            "-It can only be used for ranged attack if both weapons are ranged.\n\n" +
            "-It has two attacks.\n\n" +
            "-It cannot be upgraded.\n\n" +
            "-Incurs a penalty to the Skill of the weapons.\n\n" +
            "-Other qualities will be a combination of those qualities from the weapons.\n\n"+
            "The penalty is:\n" +
            "Weapons of same type          -2\n" +
            "Weapon same category          -3\n" +
            "Weapon different categories   -4\n" +
            "Skill is Magic (Any)           0\n" +
            "Skill is Other Magic     above+1\n" +
            "Off hand is small blade  above+1";

    public TutorialWeaponPairing(GameView view) {
        super(view, "Weapon Pairing", TEXT);
    }
}

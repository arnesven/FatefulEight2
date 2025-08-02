package view.help;

import model.combat.abilities.AutomaticCombatAction;
import view.GameView;

public class TutorialAutoCombat extends SubChapterHelpDialog {
    private static final String[] TEXT =
            new String[]{"If you are getting tired of tedious combat, you can enable automatic combat. " +
                    "Doing so will fast forward through combat by automatically selecting actions for your " +
                    "characters. An action is taken by the following rules:\n\n" +
                    "* A character which can attack will attack the enemy with the lowest health.\n\n" +
                    "* Otherwise a character will attempt to use one of the following abilities: " +
                    "Fairy Heal, Invisibility, Regenerate, Magic Missile, Curse, Inspire, Rest, Defend.\n\n" +
                    "* If none of the above are applicable, the character will pass.\n\n" +
                    "Items or spells are never used in automatic combat.\n\n" +
                    "Formation is never changed during automatic combat, except when the party is Overrun.\n\n" +
                    "Automatic combat is disabled immediately" +
                    " if any character should die or fall below " + AutomaticCombatAction.STOP_IF_HP_FALLS_BELOW + " HP.",
                    "Automatic combat should be used with caution! Enabled it only when you are sure your party will have no " +
                    "trouble finishing off the enemies."};

    public TutorialAutoCombat(GameView view) {
        super(view, "Automatic Combat", TEXT);
    }
}

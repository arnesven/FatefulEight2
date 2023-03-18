package view.help;

import view.GameView;

public class TutorialSpells extends HelpDialog {
    private static final String TEXT =
            "Spells are arcane scriptures which can be cast for beneficial effects. Anyone can " +
            "attempt to cast a spell but characters skilled in Spellcasting and the skill associated " +
            "with the color of the Spell will be much more likely to successfully cast it. " +
            "In addition to passing a skill check, a spell caster must also suffer damage equal to the " +
            "Casting Cost of that spell.\n\n" +
            "Red spells are focused on destruction and combat. Green spells are focused on nature " +
            "and transfiguration. White spells are focused on healing, light and holy power. Black " +
            "spells revolve around death, darkness and decay. Blue spells are usually focused " +
            "on illusion, altering reality or meta-magic.\n\n" +
            "Spells come in two varieties, combat spells and non-combat spells:\n" +
            "Combat spells can only be cast in combat and are either straight-out attacks " +
            "or have some other negative or positive effect on the target.\n\n" +
            "Non-combat spells can be cast in different situations, like during events, quests " +
            "or other game phases. Non-combat spells are cast from the Inventory or Spell Menu.";

    public TutorialSpells(GameView view) {
        super(view, "Spells", TEXT);
    }
}

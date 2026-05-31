package rulebook.chapters;

import view.help.TutorialAmbush;
import view.help.TutorialSurpriseAttack;

import java.io.BufferedWriter;
import java.io.IOException;

public class CombatChapter extends RulebookChapter {
    public CombatChapter() {
        super("Combat");
    }

    @Override
    public void generate(BufferedWriter writer) throws IOException {
        writer.write("The players will inevitably find themselves in violent situations, such is the nature of adventuring. " +
                "In contrast to many other roleplaying games, combat in FatefulEight is somewhat abstracted. Special rules applies " +
                "to how time (in the form of <i>Combat Rounds</i>), how the player's position is handled (called <i>Formation</i>), " +
                "and what actions can be taken during a player's turn.\n");
        writer.newLine();

        generateInitiativeSubchapter(writer);
        generateFormationSubchapter(writer);
        generateAmbushAndSurpriseSubchapter(writer);
    }

    private void generateInitiativeSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Initiative\n");
        writer.write("Combat is played over several Combat Rounds. During each combat round, combatant gets a turn. " +
                "Players are combatants, as well as enemies (monsters and other bad guys) but also allies to the players. " +
                "The order of the combatant's turns are determined by <i>initiative</i>. Initiative order is set at the beginning of " +
                "combat and is based on the combatant's speed. Since a combatant's speed may change during combat, initiative " +
                "is re-evaluated after each combat round. If two combatants have the same initiative, randomly determine which of " +
                "them acts first.\n\n" +
                "Enemies act in groups. All enemies with the same speed are grouped together and will take their combat turns together.\n\n" +
                "Example: <i>Krusk and Vzani face off against a pair of Frogmen Scouts. Krusk has speed 2, Vzani has Speed 5. " +
                "The Frogmen have a Speed of 8 will act together in group 'A'. The initiative order is thus: Group A (Frogmen Scouts), " +
                "Vzani, Krusk. This means that the two Frogmen will take their turns first, " +
                "then Vzani and finally Krusk gets his combat turn.\n</i>");
        writer.newLine();
    }

    private void generateFormationSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Formation\n");
        writer.write("The party's physical configuration during combat is called formation. " +
                "The formation consist of several rows. The player character's are located in the " +
                "<i>front row</i>, or the <i>back row</i>. Characters in the front row " +
                "are exposed to attacks but can attack with melee weapons. Characters in the back " +
                "row are generally protected from attacks but can only attack if they have " +
                "ranged weapons. Characters in the back row can still perform other combat actions " +
                "like using items and casting spells. Enemies with ranged attacks " +
                "can target characters in the back row as well.\n\n" +
                "Enemies do not have a front or back row, but are simply grouped together in the <i>enemy row</i>. " +
                "Allies to the character are located in the <i>ally row</i>, which works like another front row, but is reserved for allies.\n" +
                "Formation is configured each combat round. If however, there are more than twice " +
                "as many enemies than living characters in the front row, the party will be overrun, and " +
                "all characters are moved to the front.\n\n" +
                "Moving to the back row once combat has begun will provoke an attack of opportunity. " +
                "Attacks of opportunity inflict 1 damage on the character unless they pass an Acrobatics 7 " +
                "skill check.\n\n" +
                "Example: <i>Krusk is a witch and wields a wand, a range weapon, he prefers to stand in the back row. " +
                "Vzani is an assassin and is armed with an Orcish Dagger, a melee-weapon and stands in the front row. " +
                "The Frogmen Scouts they are facing simply go in the enemy row. With this set up Krusk will be protected from attacks, unless the Frogmen " +
                "have ranged weapons.</i>\n");
        writer.newLine();
    }

    private void generateAmbushAndSurpriseSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Advantage\n");
        writer.write("Some situations can alter the conditions of combat. If either side (enemies or players) have " +
                "the advantage, this is called <i>ambush</i> and <i>surprise</i> respectively. Combat advantage modifies " +
                "how initiative and formation is handled in combat.\n");
        writer.newLine();

        writer.write("### Ambush\n");
        writer.write(TutorialAmbush.TEXT + "\n");
        writer.newLine();

        writer.write("### Surprise\n");
        writer.write(TutorialSurpriseAttack.TEXT + "\n");
        writer.newLine();
    }
}

package rulebook.chapters;

import model.combat.abilities.CombatAction;
import model.combat.conditions.FatigueCondition;
import util.MyStrings;
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
        generateCombatLoopSubchapter(writer);
        generateCharacterCombatActionsSubchapter(writer);
        generateEnemyCombatTurnsSubchapter(writer);
        generatePostCombatCleanup(writer);
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

    private void generateCombatLoopSubchapter(BufferedWriter writer)  throws IOException {
        writer.write("## Combat Round Procedure\n");
        writer.write("A combat is resolved by performing the following steps:\n\n");
        writer.write("1. Determine initiative order (see above).\n");
        writer.write("2. Set formation (unless Ambush).\n");
        writer.write("3. Run Quick Cast turns in initiative order (unless Ambush).\n");
        writer.write("4. Each character or enemy takes a turn in initiative order. " +
                "When resolving a group of enemies, the GM decides the order in which the enemies act. " +
                "See the chapters below for how enemies act in combat, and what actions characters are allowed to take. " +
                "Allies are also characters but are controlled by the GM. After each character's and enemy group's turn, " +
                "check if combat is over using the conditions below. When combat ends, skip the following steps and go straight " +
                "to <i>Post-Combat Cleanup</i>.\n");
        writer.write("5. If combat is not over (see conditions below), re-determine initiative order.\n");
        writer.write("6. Check if the character's have been <i>Overrun</i>. This happens when there are more than twice as many enemies as there are characters " +
                "in the front row (plus any characters in the Ally row). When the party is overrun, all characters are moved to the front row.\n");
        writer.write("7. If there was no Overrun, characters may now change formation (allies always stay in the Ally row). At this point, any character " +
                "which moves from the front to the back row must succeed a Solo Acrobatics skill check difficulty 7, " +
                "or take 1 damage from Opportunity Attack by the enemies.\n");
        writer.write("8. Some conditions have <i>end-of-round</i> triggers, which trigger at the end of each combat round.\n");

        writer.newLine();

        writer.newLine();
        writer.write("Combat progresses until one of the following conditions occurs:\n\n");
        writer.write("* All enemies have either fled or been defeated.\n");
        writer.write("* The players successfully flee the combat.\n");
        writer.write("* The combat is interrupted by some other event (controlled by the GM).\n");
    }

    private void generateCharacterCombatActionsSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Combat Actions\n");
        writer.write("When it is a character's turn in combat, he or she can take one action from the following list:\n\n");
        writer.write("* Attack an enemy\n");
        writer.write("* Attempt to flee combat\n");
        writer.write("* Equip or use an item (e.g. changing weapons, drink a potion, etc)\n");
        writer.write("* Cast a Spell (see Magic chapter)\n");
        writer.write("* Use an Ability (see Abilities chapter)\n");
        writer.write("* Delay\n");
        writer.newLine();
        writer.write("### Making Attack Rolls\n");
        writer.write("When a character makes an attack roll, he or she does a normal skill check using the damage " +
                "table listed on his or her weapon.\n\n" +
                "If the result is an unmodified 1, the attack is a miss, regardless of the attack bonus.\n\n" +
                "If the result is an unmodified '10' the attack is a critical attack and will deal double damage, " +
                "however some weapons modify this and will deal critical hits on lower results as well.\n\n" +
                "Also note that some weapons provide additional attacks, so the character will make multiple attack rolls (against the same target).\n\n");

        writer.write("Before being applied to the enemy's Health Points, the damage may be reduced by the enemy's damage reduction. " +
                "Enemies have either Physical Damage Reduction, or Magical Damage Reduction or both. Physical Damage reduction only reduces " +
                "physical damage, whereas Magical Damage reduction only reduces magical damage. In general, weapons deal physical damage except for Wands. " +
                "Spells and the <i>Magic Missile</i> ability deal magical damage.\n\n");
        writer.write("If by applying damage to an enemy's health, it brings that enemy to 0 Health Points, that enemy is defeated and removed from combat. " +
                "Enemy Health Points can not go below zero.\n\n");
        writer.write("Fighting in Heavy armor will fatigue the wearer. Beginning from the " + MyStrings.nthWord(CombatAction.FATIGUE_START_ROUND) + " combat round. " +
                "A character wearing heavy armor must make a Endurance check with difficulty equal to his or her current Physical Armor points. If the check is failed, " +
                "the character becomes fatigued. A fatigued character will lose 1 stamina point (and then 1 health point when no stamina remain) when performing any combat action " +
                "which involves physical exertion.\n Fatigue is removed at the end of combat but can be recovered earlier by <i>Resting</i>.");
        writer.newLine();

        writer.write("### Fleeing Combat\n");
        writer.write("Fleeing combat is not automatic and must be attempted. Any character can call for retreat but must successfully pass a " +
                "Solo Leadership roll of difficulty 3 plus the number of characters in the party (allies are not counted for this purpose). " +
                "If the attempt is not successful the character's action is simply wasted and combat continues.\n");

        writer.write("### Delaying Your Turn\n");
        writer.write("A character which delays his or her turn will act again last in the initiative sequence. If several characters delay their internal " +
                "initiative order is kept.\n");

        writer.newLine();
    }

    private void generateEnemyCombatTurnsSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Enemy Combat Turns\n");
        writer.write("How an enemy acts on its turn is largely up to the GM, but can be summerized in its <i>Behavior</i>. " +
                "Such a behavior can determine such parameters as:\n\n");
        writer.write("* How much damage the enemy deals with its attacks.\n");
        writer.write("* How many attacks it performs during its turn.\n");
        writer.write("* If the enemy can target the back row (ranged attack).\n");
        writer.write("* If the enemy deals physical or magical damage.\n");
        writer.newLine();
        writer.write("Here follows some common behaviors for enemies:\n\n");
        writer.write("<b>Melee</b>: This is the standard behavior. Cannot target characters in the back row.\n\n");
        writer.write("<b>Ranged</b>: Can target characters in the back row.\n\n");
        writer.write("<b>Magic</b>: Does magical damage (instead of Physical).\n\n");
        writer.write("<b>Multi (N)</b>: Does N attacks on turn (up to number of characters).\n\n");
        writer.write("<b>Poison</b>: Has a chance of applying Poison condition if attack does any damage.\n\n");
        writer.write("<b>Paralysis</b>: Has a chance of applying Paralysis condition if attack does any damage.\n\n");
        writer.write("<b>Bleed</b>: Has a chance of applying Bleed condition if attack does any damage.\n\n");
        writer.write("<b>Knock-Back</b>: Has a chance of moving the character to the back row if attack does any damage.\n\n");
        writer.newLine();

        writer.write("### Enemy Attacks\n");
        writer.write("When an enemy attacks a character (party member or ally) resolve the following steps:\n\n");
        writer.write("1. Check for evade: Roll a d10. If the result is less than half the difference in speed of the enemy " +
                "and the targeted character, the attack has been evaded and deals no damage.\n");
        writer.write("2. Check for block: If the type of attack is physical and " +
                "the target has any <i>Block</i>, for instance from a shield, roll a d10. " +
                "If the result is equal to or lower than the block value, the attack has been blocked and does no damage.\n");
        writer.write("3. Determine damage: Roll a d10. On a 10 the attack is a critical hit and will deal 2 extra damage. " +
                "If the die is 3 or less, the attack is weak and will deal 1 less damage.\n");
        writer.write("4. Reduce damage. Depending on the type of damage (physical or magical) and the targets matching physical or " +
                "magical armor the incoming damage may be reduced. Use this table to convert Armor Points to damage reduction:\n\n" +
                "| AP | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11| 12| 13| 14+|\n" +
                "|----|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n" +
                "| DR | 1 | 2 | 3 | 4 | 4 | 5 | 5 | 6 | 6 | 7 | 7 | 8 | 8 | 9 |\n");
        writer.write("5. Apply damage. If this brings a character to 0 Health Points, the character dies. " +
                "Health points can never go below 0.\n");
        writer.newLine();
    }

    private void generatePostCombatCleanup(BufferedWriter writer) throws IOException {
        writer.write("## Post-Combat Cleanup\n\n");
        writer.write("After combat, unless the party fled, the can loot the enemies.\n\n" +
                "Some conditions (like Fatigue) is removed at end of combat.");
    }
}

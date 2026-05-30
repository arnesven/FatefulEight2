package rulebook.chapters;

import java.io.BufferedWriter;
import java.io.IOException;

public class CharactersChapter extends RulebookChapter {
    public CharactersChapter() {
        super("Characters");
    }

    public void generate(BufferedWriter writer) throws IOException {
        writer.write("This section will take you through character creation, step by step. " +
                "You can find the character sheet at the end of this rulebook. Go ahead and make as many " +
                "copies of it as you want.\n");
        writer.newLine();

        writer.write("1. Choose a name for your character. " +
                "If you have a hard time coming up with a name, skip this step and come back once you've " +
                "selected your race or even later.\n");
        writer.newLine();
        writer.write("| Race | Common Male Names | Common Female Names |\n");
        writer.write("|------|-------------------|---------------------|\n");
        writer.write("| Human | John, Jack, Sven, Boris | Denise, Ivana, Samantha, Miriam |\n");
        writer.write("| Elf | Muldan, Melethain, Miklos, Leodor | Dara, Atalya, Alewyn, Zephyra |\n");
        writer.write("| Dwarf | Hurin, Megar, Nain, Thorbalt | Torhild, Ethelthane, Eldeth, Siw |\n");
        writer.write("| Half-Orc | Krusk, Mord, Gorg, Baz | Kruska, Gorga, Puyet, Vendela |\n");
        writer.write("| Halfling | Paddy, Fatty, Bungo, Voldo | Audry, Stella, Vzani, Emily |\n");
        writer.newLine();

        writer.write("2. Select a race for your character. See the Races chapter. " +
                "You can write down your Carry-Capacity, since it is only based on your race.\n");
        writer.write("3. Select a class for your character. See the Classes chapter.\n");
        writer.write("4. Set your level to 1 and your XP to 0. 'Next' refers to how much XP is left to level 2, set it to 100.\n");
        writer.write("5. Your class decides your Alignment, write it down.\n");
        writer.write("6. Calculated your health by adding your base health from your class plus your racial modifier.\n");
        writer.write("7. Set your stamina to 2.\n");
        writer.write("8. Calculate your speed by adding your base speed from your class plus your racial modifier.\n");
        writer.write("9. Set your Notoriety to 0.\n");
        writer.write("10. Your class decides your armor class, either Heavy or Light. Write it down.\n");
        writer.write("11. Fill in the ranks of your skills, based on your class, don't forget to add 1 for each skill listed for your race.\n");
        writer.write("12. For each skill, check if you qualify (have enough ranks) for any associated Abilities listed for that skill. See the Skills section.\n");
        writer.write("13. Select two starting items. If you select a weapon or clothing, they replace any default gear you would receive from your class. " +
                "Your starting items are determined by your class, but you can always take a Longsword and/or a Potion Package (one Health Potion and one Rejuvenation Potion) instead.\n");
        writer.write("14. Your character starts with 10 food, and 20 gold.\n");
        writer.write("15. If you still haven't settled on a name for your character, it's high time to do it now!\n");
    }
}

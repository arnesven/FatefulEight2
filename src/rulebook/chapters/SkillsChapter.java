package rulebook.chapters;

import model.classes.CharacterClass;
import model.classes.Skill;
import model.combat.abilities.SkillAbilityCombatAction;
import util.MyLists;
import view.help.SkillsHelpChapter;
import view.help.SpecificSkillHelpDialog;
import view.help.TutorialSkillChecksDialog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class SkillsChapter extends RulebookChapter {
    public SkillsChapter() {
        super("Skills");
    }

    public void generate(BufferedWriter writer) throws IOException {
        makeSkillCheckSections(writer);
        makeSkillWeightsTable(writer);
        makeSkillsPerClass(writer);
        makeSkillDescriptions(writer);
    }

    private static void makeSkillCheckSections(BufferedWriter writer) throws IOException {
        writer.write(TutorialSkillChecksDialog.MAIN_TEXT + "\n");

        writer.write("### Solo Checks\n");
        writer.write(TutorialSkillChecksDialog.SOLO_TEXT);
        writer.newLine();

        writer.write("### Collaborative Checks\n");
        writer.write(TutorialSkillChecksDialog.COLLABORATIVE_TEXT);
        writer.newLine();

        writer.write("### Collective Checks\n");
        writer.write(TutorialSkillChecksDialog.COLLECTIVE_TEXT);
        writer.newLine();

        writer.write("### Reactive Checks\n");
        writer.write(TutorialSkillChecksDialog.REACTIVE_TEXT);
        writer.newLine();

        writer.write("### Synergy Checks\n");
        writer.write(TutorialSkillChecksDialog.SYNERGY_TEXT);
        writer.newLine();
    }

    private static void makeSkillWeightsTable(BufferedWriter writer) throws IOException {
        writer.write("### Skill Weights\n");
        String skillIntroText = SkillsHelpChapter.makeText();
        Scanner scanner = new Scanner(skillIntroText);
        boolean inTable = false;
        for (String line = ""; scanner.hasNext(); line = scanner.nextLine()) {
            if (line.contains("Level  W1")) {
                inTable = true;
                line = line.replace("Level ", "Level");
                line = line.replace(" W", " | W");
                line = line + " |";
                writer.write(line + "\n");
                writer.write("|--|--|--|--|--|--|--|\n");
            } else {
                if (inTable) {
                    if (line.startsWith("   ")) {
                        line = line.replace("    ", " ");
                        line = line.replace("  ", " | ");
                        line = "|" + line;
                    } else {
                        inTable = false;
                    }
                }
                writer.write(line + "\n");
            }
        }
    }



    private static void makeSkillsPerClass(BufferedWriter writer) throws IOException {
        writer.write("### Class Skills\n");
        writer.write("The table below shows all skills and which are considered Class Skills for each class.\n\n");

        List<CharacterClass> classes = ClassesChapter.getBasicClasses();

        generateClassSkillsTable(writer, classes.subList(0, 9), true);
        generateClassSkillsTable(writer, classes.subList(9, classes.size()), false);
        forcePageBreak(writer);
    }

    private static void generateClassSkillsTable(BufferedWriter writer, List<CharacterClass> classes,
                                                 boolean firstHalf) throws IOException {
        forcePageBreak(writer);
        writer.write("<font size=\"1\">\n");
        writer.newLine();
        StringBuilder header;
        StringBuilder subHeader;
        if (firstHalf) {
            header = new StringBuilder("| Skill | Short | Attr |");
            subHeader = new StringBuilder("|-------|-------|------|");
        } else {
            header = new StringBuilder("| Skill |");
            subHeader = new StringBuilder("|-----|");
        }

        for (CharacterClass cls : classes) {
            header.append(cls.getShortName()).append(" |");
            subHeader.append("---|");
        }

        writer.write(header + "\n");
        writer.write(subHeader + "\n");
        for (Skill s : Skill.values()) {
            if (firstHalf) {
                String attribute = getAttributeForSkill(s);
                writer.write("| " + s.getName() + " | " + s.getShortName() + " | " + attribute + " |");
            } else {
                writer.write("| " + s.getName() + " | ");
            }
            for (CharacterClass cls : classes) {
                if (cls.getSkills().contains(s)) {
                    writer.write(cls.getWeightForSkill(s).asString() + " |");
                } else {
                    writer.write("   |");
                }
            }
            writer.write("\n");
        }

        writer.write("</font>\n");
        writer.newLine();
    }

    private static void forcePageBreak(BufferedWriter writer) throws IOException {
        writer.write("<div style=\"page-break-after: always;\"></div>\n");
        writer.newLine();
    }

    private static String getAttributeForSkill(Skill s) {
        Map<String, List<Skill>> map = Skill.getAttributeSets();
        String attribute = "None";
        for (String attr : map.keySet()) {
            if (map.get(attr).contains(s)) {
                attribute = attr;
                break;
            }
        }
        return attribute;
    }

    private static void makeSkillDescriptions(BufferedWriter writer) throws IOException {
        writer.write("### Skill Descriptions\n\n");
        writer.write("<b>Description Template</b>\n\n");
        writer.write("<i>Skill Description</i>\n\n");
        writer.write("Governing Attribute: <i>Attribute if applicable.</i>\n\n");
        writer.write("Abilities (Required Ranks): <i>Associated abilities</i>\n\n");

        for (Skill s : Skill.values()) {
            writer.write("#### " + s.getName() + " (" + s.getShortName() + ")\n");
            writer.write(s.getDescription() + "\n");
            writer.newLine();

            writer.write("Governing Attribute: " + getAttributeForSkill(s) + "\n\n");

            List<SkillAbilityCombatAction> skillAbilities = SpecificSkillHelpDialog.makeAbilityList(s);
            String abiExtra = "";
            if (!skillAbilities.isEmpty()) {
                abiExtra = "Abilities (Required Ranks): " + MyLists.commaAndJoin(skillAbilities,
                        skiAb -> skiAb.getName() + " (" + skiAb.getRequiredRanks() + ")");
                writer.write(abiExtra + "\n");
            }

            String misc = s.getMiscHelpText();
            if (!misc.isEmpty()) {
                writer.write("\n" + misc + "\n");
            }

            writer.newLine();

        }
    }
}

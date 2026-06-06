package rulebook.chapters;

import model.classes.*;
import model.items.Equipment;
import model.items.HorseStartingItem;
import model.items.InventoryDummyItem;
import model.items.Item;
import model.items.spells.Spell;
import util.MyStrings;
import view.help.SpecificClassHelpDialog;
import view.help.TutorialClassesDialog;
import view.sprites.Sprite;
import view.widget.DetailedClassNameStrategy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static rulebook.GenerateRulebook.PATH_BASE;

public class ClassesChapter extends RulebookChapter {

    public ClassesChapter() {
        super("Classes");
    }

    public static List<CharacterClass> getBasicClasses() {
        List<CharacterClass> classes = new ArrayList<>(Arrays.stream(Classes.allClasses).toList());
        classes.removeIf(cc -> cc instanceof NoClass);
        return classes;
    }

    public void generate(BufferedWriter writer) throws IOException {
        List<CharacterClass> classes = getBasicClasses();
        makeClassIcons(classes);
        generateClassesOverview(writer, classes);
        generateClassTemplateSubChapter(writer);
        for (CharacterClass cc : classes) {
            generateClassSubChapter(writer, cc);
        }

        generateChangeClassSubchapter(writer);
    }

    private static void makeClassIcons(List<CharacterClass> classes) {
        for (CharacterClass cc : classes) {
            try {
                Sprite spr = cc.getIconSprite();
                BufferedImage img = spr.getImage();
                File outFile = new File(PATH_BASE + "/images/" + cc.getShortName().toLowerCase() + "_class_icon.png");
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                ImageIO.write(img, "png", outFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void generateClassesOverview(BufferedWriter writer, List<CharacterClass> classes) throws IOException {
        writer.write("The following classes are available at the start of the game.\n\n");
        writer.write("| Class | Short Name | Description | | Class | Short Name | Description |\n");
        writer.write("|-------|------------|-------------|-|-------|------------|-------------|");
        int count = 0;
        for (CharacterClass cc : classes) {
            if (count % 2 == 0) {
                writer.write("\n|");
            }
            writer.write("<b>" + cc.getFullName() + "</b> | " + cc.getShortName() + " | " + DetailedClassNameStrategy.getClassShortDescription(cc) + "|");
            if (count % 2 == 0) {
                writer.write(" |");
            }
            count++;
        }
        writer.write("\n");
        writer.newLine();
        writer.newLine();
    }

    private static void generateClassTemplateSubChapter(BufferedWriter writer) throws IOException {
        writer.write("## Class Template (Short Name)\n");
        writer.write("<i>Class description</i>\n");
        writer.write("<p><b>Health Points:</b> Number of Health Point your character starts with, modified by your race.<br/>\n");
        writer.write("<b>Speed:</b> Your character's speed, modified by race.<br/>\n");
        writer.write("<b>Armor Class:</b> Determines if your character can equip Heavy armor or is limited to Light armor.<br/>\n");
        writer.write("<b>Default Gear</b> The gear your character starts the game with.<br/>\n");
        writer.write("<b>Starting Items</b> Your character may start with two of the items from " +
                "this list. If you select a weapon or piece of clothing from this list it replaces " +
                "any you would receive from your default gear.<br/>\n</p>");
        writer.write("<b>Skill Weights</b> Determines your character's Skill progression. " +
                "In general, a higher weight means your character will progress more quickly in that skill.<br/>\n");
        writer.write("<b>Skills and Abilities per Level</b> Shows your character's skills and abilities for each level. " +
                "Remember that a character's skill bonuses could be modified by his or her race.");
        writer.newLine();
        writer.newLine();
    }

    private static void generateClassSubChapter(BufferedWriter writer, CharacterClass cc) throws IOException {
        writer.write("## " + cc.getFullName() + " (" + cc.getShortName() + ")\n");
        writer.write("<p align=\"center\"><img width=64px src=\"../images/" + cc.getShortName().toLowerCase() + "_class_icon.png\" alt=\"\"/></p>\n");
        writer.newLine();
        writer.write(cc.getDescription());
        writer.newLine();
        writer.newLine();
        writer.write("<p><b>Health Points:</b> " + cc.getHP() + "<br/>\n");
        writer.write("<b>Speed:</b> " + cc.getSpeed() + "<br/>\n");
        writer.write("<b>Armor Class:</b> " + (cc.canUseHeavyArmor() ? "HEAVY" : "LIGHT") + "<br/>\n");
        int align = Classes.ALIGNMENT.getOrDefault(cc.id(), 0);
        writer.write("<b>Alignment: </b>" + MyStrings.withPlus(align) + "<br/>\n");
        Equipment eq = cc.getDefaultEquipment();
        writer.write("<b>Default Gear: </b>" + eq.getWeapon().getName() + ", " + eq.getClothing().getName() +
                (eq.getAccessory() != null ? (", " + eq.getAccessory().getName()) : "") + "<br/>\n");
        writer.write("<b>Starting Items:</b><ul style=\"margin-top: -15px;\">\n");
        for (Item it : cc.getStartingItems()) {
            String extra = "";
            if (it instanceof HorseStartingItem) {
                extra = "";
            } else if (it instanceof Spell) {
                extra = " (Spell)";
            } else if (it instanceof InventoryDummyItem) {
                extra = it.getShoppingDetails();
            }
            writer.write("<li>" + it.getName() + extra + "</li>\n");
        }
        writer.write("</ul></p>\n\n");
        writer.newLine();
        writer.newLine();

        writer.write("|         Skill         | Weight |         Skill         | Weight |\n");
        writer.write("|-----------------------|--------|-----------------------|--------|");
        int count = 0;
        for (Skill s : cc.getSkills()) {
            if (count % 2 == 0) {
                writer.write("\n|");
            }
            writer.write( String.format(" %-21s | %6s |", s.getName() + " (" + s.getShortName() + ")", cc.getWeightForSkill(s).asString()));
            count++;
        }
        if (count % 2 == 1) {
            writer.write("                       |        |\n");
        }
        writer.newLine();
        writer.newLine();

        Scanner scan = new Scanner(SpecificClassHelpDialog.makeClassTable(cc));
        writer.write("| Level | Skill Increases and Abilities |\n");
        writer.write("|-------|----|");
        while (scan.hasNext()) {
            if (scan.hasNextInt()) {
                int level = scan.nextInt();
                writer.write("\n| " + String.format("%6d", level) + " | ");
            }
            String rest = scan.nextLine();
            rest = rest.replaceAll("\\d", "$0,");
            writer.write(rest + " ");
        }
        writer.newLine();
        writer.newLine();
    }


    private void generateChangeClassSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Changing Class\n");
        boolean verbatim = false;
        String buf = TutorialClassesDialog.makeClassGraphTable();

        for (String line : buf.split("\n")) {
            boolean isGraphLine = line.contains("/") || line.contains("--") || line.contains(":");

            if (!verbatim && isGraphLine) {
                verbatim = true;
                writer.write("```\n");
            }

            writer.write(line + "\n");

            if (verbatim && !isGraphLine) {
                verbatim = false;
                writer.write("```\n");
            }
        }
    }
}

package rulebook;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.races.Race;
import util.MyLists;
import util.MyStrings;
import view.sprites.Sprite;
import view.subviews.PortraitSubView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static rulebook.GenerateRulebook.PATH_BASE;

public class RacesChapter {
    public static void generate(BufferedWriter writer) {
        try {
            writer.write("Players can select from the following set of races for their character.\n");
            for (Race r : Race.allRaces) {
                writer.write("* " + r.getQualifiedName() + "\n");
            }

            makeRaceImages();

            for (Race r : Race.allRaces) {
                generateSubChapter(writer, r);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void makeRaceImages() {
        for (Race r : Race.allRaces) {
            try {
                CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.None, r);
                ImageScreenHandler screenHandler = new ImageScreenHandler();
                app.drawYourself(screenHandler, 0, 0);
                BufferedImage img = screenHandler.getImage(7*8, 7*8);
                File outFile = new File(PATH_BASE + "/images/" + "race_" + r.id() + "_portrait.png");
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                ImageIO.write(img, "png", outFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void generateSubChapter(BufferedWriter writer, Race r) throws IOException {
        writer.write("### " + r.getQualifiedName() + "\n");
        writer.write("<p align=\"center\"><img width=112px src=\"../images/race_" + r.id() + "_portrait.png\" alt=\"\"/></p>\n");
        writer.write(r.getDescription() + "\n");
        writer.newLine();
        writer.write("<p>");
        writer.write("<b>Health Modifier:</b> " + MyStrings.withPlus(r.getHPModifier()) + "<br/>\n");
        writer.write("<b>Speed Modifier:</b> " + MyStrings.withPlus(r.getSpeedModifier()) + "<br/>\n");
        writer.write("<b>Carry Capacity:</b> " + r.getCarryingCapacity() + "<br/>\n");
        writer.write("<b>Skill Bonuses:</b> " + MyLists.commaAndJoin(r.getSkills(), Skill::getName) + "<br/>\n");
        writer.write("</p>");
        writer.newLine();
        writer.newLine();
    }
}

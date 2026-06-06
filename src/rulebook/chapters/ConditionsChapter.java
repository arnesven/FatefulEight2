package rulebook.chapters;

import model.combat.conditions.Condition;
import model.items.Item;
import view.help.ConditionsHelpChapter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static rulebook.GenerateRulebook.PATH_BASE;

public class ConditionsChapter extends RulebookChapter {
    public ConditionsChapter() {
        super("Conditions");
        makeIcons();
    }

    private void makeIcons() {
        List<Condition> conds = ConditionsHelpChapter.makeAllConditions();
        for (Condition cond : conds) {
            try {
                BufferedImage img = cond.getSymbol().getImage();
                File outFile = new File(PATH_BASE + "/images/" + getFileName(cond));
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                ImageIO.write(img, "png", outFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getFileName(Condition cond) {
        return "condition_" + cond.getName().toLowerCase().replaceAll(" ", "_") + ".png";
    }

    private String imgTag(Condition c) {
        return "<img width=16px src=\"../images/" + getFileName(c) + "\">";
    }

    @Override
    public void generate(BufferedWriter writer) throws IOException {
        writer.write("Many things can happen to characters, and to enemies. When something has a lasting " +
                "effect (either in combat or otherwise) that is represented by a <i>condition</i>. The " +
                "following is a list of conditions and their effects.\n\n");

        List<Condition> conds = ConditionsHelpChapter.makeAllConditions();
        for (Condition cond : conds) {
            writer.write("### " + cond.getName() + " " + imgTag(cond) + "\n");
            writer.write("<b>Short name:</b> " + cond.getShortName() + "\n\n");
            writer.write("<b>Duration:</b> " + (cond.removeAtEndOfCombat() ? "End of combat" : "Longer") + "\n\n");
            writer.write("<b>Description:</b> " + cond.getHelpView(null).getText() + "\n");
        }
    }
}

package view.help;

import model.classes.CharacterClass;
import model.classes.ClassGraph;
import model.classes.Classes;
import view.GameView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorialClassesDialog extends ExpandableHelpDialog {
    private static final String[] text = new String[]{
            "A character's class defines that character's baseline of Health Points and Speed, " +
            "their skills and whether or not that character can wear heavy armor. A character's " +
            "class also affects starting items and how much gold they contribute to the party when joining and " +
            "how much gold they would claim if being dismissed.\n\n" +
            "The Skill Weights (denoted W1, W2 etc.) indicate how a character's ranks in a skill will increase each " +
            "level. See the Skills Chapter for details.\n\n" +
            "Each class has four related classes (see next page). Various events will allow " +
            "characters to change their class into a related class. Think carefully before switching classes, you may not " +
            "get a chance to switch back soon!", makeClassGraphTable()};

    public TutorialClassesDialog(GameView view) {
        super(view, "Classes", text, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> subsections = new ArrayList<>();
        for (CharacterClass characterClass : Classes.allClasses) {
            subsections.add(new SpecificClassHelpDialog(view, characterClass));
        }
        subsections.add(new TutorialPrestigeClasses(view));
        return subsections;
    }

    private static String makeClassGraphTable() {
        String description = "The following graph shows how each class relates to other classes. " +
                "A character can only change class into a class related to his or her current class.\n\n";
        StringBuilder bldr = new StringBuilder();
        for (CharacterClass cls : CharacterClass.getSelectableClasses()) {
            bldr.append(cls.getShortName()).append(":");
            for (CharacterClass cls2 : ClassGraph.get(cls.id())) {
                bldr.append(cls2.getShortName()).append(" ");
            }
            bldr.append("\n");
        }
        String caption = "\n\nIn addition to the edges seen, each class also relates to the one 'across' from it, e.g. " +
                Classes.CAP.getShortName() + " is related to " + ClassGraph.getWrapNeighbor(Classes.CAP.id()).getShortName() + " and " +
                Classes.ART.getShortName() + " is related to " + ClassGraph.getWrapNeighbor(Classes.ART.id()).getShortName() + ".";
        return description + ClassGraph.printNoCosts() + caption;
    }

}

package view.help;

import model.classes.CharacterClass;
import model.classes.Classes;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class TutorialClassesDialog extends ExpandableHelpDialog {
    private static final String text =
            "A character's class defines that character's baseline of Health Points and Speed, " +
            "their skills and whether or not that character can wear heavy armor. A character's " +
            "class also affects starting items and how much gold they contribute to the party when joining and " +
            "how much gold they would claim if being dismissed.\n\n" +
            "The Skill Weights (denoted W1, W2 etc.) indicate how a character's ranks in a skill will increase each " +
            "level. See the Skills Chapter for details.\n\n" +
            "Each character has four classes which he or she may assume. Various events will allow " +
            "characters to change their class. Think carefully before switching classes, you may not " +
            "get a chance to switch back soon!";

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

}

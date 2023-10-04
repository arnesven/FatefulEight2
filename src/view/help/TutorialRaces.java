package view.help;

import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.Race;
import view.GameView;
import view.party.CharacterCreationView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class TutorialRaces extends ExpandableHelpDialog {
    private static final String text = "A character's race defines that character's cultural and ethnic origins. " +
            "The world has eight known races; Humans (which come in two varieties, Northern and Southern), Dwarves, " +
            "Half-Orcs, Halflings and three kinds of elves, Wood Elves, High Elves and Dark Elves.\n\n" +
            "Each race gives modifies a character's health and speed attributes and gives a small bonus to a handful of " +
            "skills.";

    public TutorialRaces(GameView view) {
        super(view, "Races", text);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> subsections = new ArrayList<>();
        for (Race race : Race.allRaces) {
            subsections.add(new SpecificRaceHelpDialog(view, race, PortraitSubView.makeRandomPortrait(Classes.None, race)));
        }
        return subsections;
    }
}

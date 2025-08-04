package view.help;

import model.characters.appearance.LizardmanAppearance;
import model.characters.special.GoblinAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.EasternHuman;
import model.races.FrogmanAppearance;
import model.races.OrcAppearance;
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
            "Each race gives modifies a character's health, speed, and carrying capacity attributes and gives a small bonus to a handful of " +
            "skills.";

    public TutorialRaces(GameView view) {
        super(view, "Races", text, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> subsections = new ArrayList<>();
        subsections.add(new SpecificRaceHelpDialog(view, Race.EASTERN_HUMAN, PortraitSubView.makeRandomPortrait(Classes.None, Race.EASTERN_HUMAN)));
        for (Race race : Race.allRaces) {
            subsections.add(new SpecificRaceHelpDialog(view, race, PortraitSubView.makeRandomPortrait(Classes.None, race)));
        }
        subsections.add(new SpecificRaceHelpDialog(view, Race.FROGMAN, new FrogmanAppearance()));
        subsections.add(new SpecificRaceHelpDialog(view, Race.ORC, new OrcAppearance()));
        subsections.add(new SpecificRaceHelpDialog(view, Race.GOBLIN, new GoblinAppearance()));
        subsections.add(new SpecificRaceHelpDialog(view, Race.LIZARDMAN, new LizardmanAppearance()));
        // FEATURE: Trolls, Ogres, Lizardmen
        return subsections;
    }
}

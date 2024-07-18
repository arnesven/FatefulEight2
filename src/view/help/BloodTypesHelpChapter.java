package view.help;

import view.GameView;

public class BloodTypesHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT =
            "Vampires prefer to feed on members of their own race. Deviating from this " +
            "comes with certain risk:\n\n" +
            "Non-human vampires are vulnerable to the richness of human blood. " +
            "It gives them an aura of strangeness which will impair charisma-based " +
            "skills for three days.\n\n" +
            "Non-elven vampires are vulnerable to the purity of elven blood. " +
            "It burns the vampire's intestines, dealing direct damage.\n\n" +
            "Non-dwarven vampires are vulnerable to the thickness of dwarven blood. " +
            "It clogs the vampire's system and makes them sluggish and slow, bringing a " +
            "penalty to speed for three days.\n\n" +
            "Non-half-orc vampires cannot resist the natural toxicity of half-orc blood and " +
            "will be poisoned by consuming it.\n\n" +
            "Non-halfling vampires may have difficulty regaining full stamina from consuming a " +
            "halflings blood. There's simply not enough blood there to satisfy them! " +
            "Halfling victims provide at most 1D6 worth of stamina.";

    public BloodTypesHelpChapter(GameView view) {
        super(view, "Blood Types", TEXT);
    }
}

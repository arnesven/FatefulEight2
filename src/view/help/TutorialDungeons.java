package view.help;

import view.GameView;

public class TutorialDungeons extends HelpDialog {
    private static final String TEXT =
            "Dungeons can be found in several places in the world, but always under " +
            "ruins. Dungeons often contain several labyrinthine levels filled with " +
            "monsters, treasures and traps. Clearing dungeons unlock achievements.\n\n" +
            "While exploring a dungeon, your party fills in a map as you go. You can " +
            "look at it anytime by using the MAP button.\n\n" +
            "The EXIT button lets you escape the dungeon immediately. If the party is getting too exhausted " +
            "it may be better to retreat to the surface, rest a bit and try again another day.\n\n" +
            "Be careful in the dungeon! The final level of the dungeon will contain a challenging adversary.";

    public TutorialDungeons(GameView view) {
        super(view, "Dungeons", TEXT);
    }
}

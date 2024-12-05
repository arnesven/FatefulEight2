package view.help;

import model.Model;
import view.GameView;
import view.PuzzleTubeView;

public class TutorialPuzzleTubes extends HelpDialog {
    private static final String TEXT = "These devices are of dwarven origin and have an unknown purpose. " +
            "There are many of them scattered all over the realm. You can have a party member " +
            "attempt to solve a puzzle tube by inspecting it from the Inventory menu.\n\n" +
            "Puzzle tubes are composed of multiple rings with 10 letters on each. By turning the rings and forming " +
            "the secret password, the tube will be unlocked. Use the arrow keys to select which ring to " +
            "manipulate and to turn it.\n\n" +
            "Rings can be marked with the SPACE key so that they belong to a group. All rings in the " +
            "same group will turn together. This is particularly useful when you find rings which have " +
            "an audio que when interlocking. Each puzzle may have up to four such pair of rings but you may use " +
            "lockpicks to add more (the F3 key). The chance of success when doing so is based on the inspecting character's Security skill.";

    public TutorialPuzzleTubes(GameView view) {
        super(view, "Puzzle Tubes", TEXT);
    }

    @Override
    public void transitionedFrom(Model model) {
        if (getPrevious() instanceof PuzzleTubeView) {
            ((PuzzleTubeView)getPrevious()).setMadeChanges();
        }
    }
}

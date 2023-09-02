package view.help;

import view.GameView;

public class TutorialHorseRacing extends HelpDialog {
    private static final String TEXT =
            "Horse Races occur in various places in the world. During a horse race, you can strafe left or " +
            "right with the arrow keys, and jump with the space key. Your horse will automatically accelerate " +
            "as much as is possible on the terrain you are currently on. Horses run the fastest on paths, less " +
            "through high grass, and even slower through water puddles. In some places obstacles may block " +
            "the path, like trees and large boulders. Fallen tree trunks can be jumped over.\n\n" +
            "Watch out for other racers, bumping in to them will slow you down. Furthermore, strafing too often " +
            "will tire your horse out and slow you down.\n\n" +
            "A character's ability to ride a horse is dependent on their Survival skill.";

    public TutorialHorseRacing(GameView view) {
        super(view, "Horse Racing", TEXT);
    }
}

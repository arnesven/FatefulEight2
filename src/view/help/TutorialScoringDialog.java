package view.help;

import model.GameScore;
import view.GameView;

public class TutorialScoringDialog extends HelpDialog {

    public TutorialScoringDialog(GameView previous) {
        super(previous, "Scoring", GameScore.makeTutorialText());
    }
}

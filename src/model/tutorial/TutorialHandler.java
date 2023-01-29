package model.tutorial;

import model.Model;
import view.help.TutorialStartDialog;

import java.io.Serializable;

public class TutorialHandler implements Serializable {

    private boolean tutorialEnabled = true;

    public void start(Model model) {
        model.getLog().waitForAnimationToFinish();
        model.transitionToDialog(new TutorialStartDialog(model.getView()));
    }

    public void setTutorialEnabled(boolean b) {
        tutorialEnabled = b;
    }
}

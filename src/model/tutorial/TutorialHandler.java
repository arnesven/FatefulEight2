package model.tutorial;

import model.Model;
import view.help.TutorialInnDialog;
import view.help.TutorialRecruitDialog;
import view.help.TutorialStartDialog;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TutorialHandler implements Serializable {

    private Set<String> keys = new HashSet<>();
    private boolean tutorialEnabled = true;

    private void runOnce(String key, TutorialStep step) {
        if (!keys.contains(key) && tutorialEnabled) {
            step.doStep();
            keys.add(key);
        }
    }

    public void start(Model model) {
        runOnce("start", () -> {
                model.getLog().waitForAnimationToFinish();
                model.transitionToDialog(new TutorialStartDialog(model.getView()));
        });
    }

    public void setTutorialEnabled(boolean b) {
        tutorialEnabled = b;
    }

    public void theInn(Model model) {
        runOnce("theInn", () -> {
            model.getLog().waitForAnimationToFinish();
            if (tutorialEnabled) {
                model.transitionToDialog(new TutorialInnDialog(model.getView()));
            }
        });
    }

    public void recruit(Model model) {
        runOnce("recruit", () -> {
            model.transitionToDialog(new TutorialRecruitDialog(model.getView()));
        });
    }

    private interface TutorialStep {
        void doStep();
    }
}

package model.tutorial;

import model.Model;
import model.states.DailyEventState;
import model.states.events.PeskyCrowEvent;
import view.help.*;

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
                model.transitionToDialog(new TutorialDailyActions(model.getView()));
            }
        });
    }

    public void recruit(Model model) {
        runOnce("recruit", () -> {
            model.transitionToDialog(new TutorialRecruitDialog(model.getView()));
        });
    }

    public void shopping(Model model) {
        runOnce("shopping", () -> {
            model.transitionToDialog(new TutorialShoppingDialog(model.getView()));
        });
    }

    public void evening(Model model) {
        runOnce("evening", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialEveningDialog(model.getView()));
        });
    }

    public void travel(Model model) {
        runOnce("travel", () -> {
            model.transitionToDialog(new TutorialTravelDialog(model.getView()));
        });
    }

    public DailyEventState getTutorialEvent(Model model) {
        if (!tutorialEnabled || keys.contains("event")) {
            return null;
        }
        keys.add("event");
        return new PeskyCrowEvent(model);
    }

    public void combatFormation(Model model) {
        runOnce("combatFormation", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCombatFormationDialog(model.getView()));
        });
    }

    public void combatActions(Model model) {
        runOnce("combatActions", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCombatActionsDialog(model.getView()));
        });
    }

    public void combatDamage(Model model) {
        runOnce("combatDamage", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCombatDamageDialog(model.getView()));
        });
    }

    public void leader(Model model) {
        runOnce("leader", () -> {
           model.getLog().waitForAnimationToFinish();
           model.transitionToDialog(new TutorialLeaderDialog(model.getView()));
        });
    }

    public void skillChecks(Model model) {
        runOnce("skillChecks", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialSkillChecksDialog(model.getView()));
        });
    }

    public void equipment(Model model) {
        runOnce("equipment", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialEquipmentDialog(model.getView()));
        });
    }

    public void classes(Model model) {
        runOnce("classes", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialClassesDialog(model.getView()));
        });
    }

    public void quests(Model model) {
        runOnce("quests", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialQuests(model.getView()));
        });
    }

    private interface TutorialStep {
        void doStep();
    }
}

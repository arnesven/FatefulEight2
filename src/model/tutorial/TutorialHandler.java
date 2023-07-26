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
    private boolean tutorialOn = true;

    private void runOnce(String key, TutorialStep step) {
        if (!keys.contains(key) && tutorialOn) {
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
        tutorialOn = b;
    }

    public void theInn(Model model) {
        runOnce("theInn", () -> {
            model.getLog().waitForAnimationToFinish();
            if (tutorialOn) {
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
        if (!tutorialOn || keys.contains("event")) {
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

    public void spells(Model model) {
        runOnce("spells", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialSpells(model.getView()));
        });
    }

    public void alchemy(Model model) {
        runOnce("alchemy", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialAlchemy(model.getView()));
        });
    }

    public void crafting(Model model) {
        runOnce("crafting", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCrafting(model.getView()));
        });
    }

    public void dungeons(Model model) {
        runOnce("dungeons", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialDungeons(model.getView()));
        });
    }

    public void allies(Model model) {
        runOnce("allies", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialAllies(model.getView()));
        });
    }

    public void training(Model model) {
        runOnce("training", () -> {
           model.getLog().waitForAnimationToFinish();
           model.transitionToDialog(new TutorialTraining(model.getView()));
        });
    }

    public void alignment(Model model) {
        runOnce("alignment", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialAlignment(model.getView()));
        });
    }

    public void loans(Model model) {
        runOnce("loans", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialLoans(model.getView()));
        });
    }

    public void attitudes(Model model) {
        runOnce("attitudes", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialAttitudes(model.getView()));
        });
    }

    public void combatAttacks(Model model) {
        runOnce("attacks", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCombatAttacks(model.getView()));
        });
    }

    public boolean isTutorialEnabled() {
        return tutorialOn;
    }

    private interface TutorialStep {
        void doStep();
    }
}

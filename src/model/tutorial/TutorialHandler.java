package model.tutorial;

import view.help.TutorialDragonTaming;
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
            model.transitionToDialog(new TutorialGoingOnQuests(model.getView()));
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

    public void evading(Model model) {
        runOnce("evading", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialEvading(model.getView()));
        });
    }

    public void blocking(Model model) {
        runOnce("blocking", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialBlocking(model.getView()));
        });
    }

    public void sneakAttack(Model model) {
        runOnce("sneak attack", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialSneakAttack(model.getView()));
        });
    }
    public void defending(Model model) {
        runOnce("defending", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialDefending(model.getView()));
        });
    }

    public void combatResting(Model model) {
        runOnce("resting", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCombatResting(model.getView()));
        });
    }

    public void inspire(Model model) {
        runOnce("inspire", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialInspire(model.getView()));
        });
    }

    public void riposte(Model model) {
        runOnce("reposte", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialRiposte(model.getView()));
        });
    }


    public void heavyBlow(Model model) {
        runOnce("heavy blow", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialHeavyBlow(model.getView()));
        });
    }

    public void sniperShot(Model model) {
        runOnce("sniper shot", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialSniperShot(model.getView()));
        });
    }

    public void ambush(Model model) {
        runOnce("ambush", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialAmbush(model.getView()));
        });
    }

    public void horses(Model model) {
        runOnce("horses", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialHorses(model.getView()));
        });
    }

    public void fatigue(Model model) {
        runOnce("fatigue", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialFatigue(model.getView()));
        });
    }

    public boolean isTutorialEnabled() {
        return tutorialOn;
    }

    public void enemyAttacks(Model model) {
        runOnce("enemyattacks1", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialEnemyAttacks1(model.getView()));
        });
    }

    public void enemyAttacks2(Model model) {
        runOnce("enemyattacks2", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialEnemyAttacks2(model.getView()));
        });
    }

    public void horseRacing(Model model) {
        runOnce("horseracing", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialHorseRacing(model.getView()));
        });
    }

    public void obols(Model model) {
        runOnce("obols", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialObols(model.getView()));
        });
    }

    public void cardGameRunny(Model model) {
        runOnce("cardgamerunny", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCardGameRunny(model.getView()));
        });
    }

    public void fairyHeal(Model model) {
        runOnce("fairyheal", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialFairyHeal(model.getView()));
        });
    }

    public void regenerate(Model model) {
        runOnce("regenerate", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialRegenerate(model.getView()));
        });
    }

    public void invisibility(Model model) {
        runOnce("invisibility", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialInvisibility(model.getView()));
        });
    }

    public void magicMissile(Model model) {
        runOnce("magicmissile", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialMagicMissile(model.getView()));
        });
    }

    public void curseAbility(Model model) {
        runOnce("curseability", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCurseAbility(model.getView()));
        });
    }

    public void salvaging(Model model) {
        runOnce("salvaging", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialSalvaging(model.getView()));
        });
    }

    public void otherParties(Model model) {
        runOnce("otherparties", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialOtherParties(model.getView()));
        });
    }

    public void pickPocketing(Model model) {
        runOnce("pickpocketing", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new PickPocketing(model.getView()));
        });
    }

    public void notoriety(Model model) {
        runOnce("notoriety", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialNotoriety(model.getView()));
        });
    }

    public void carryingCapacity(Model model) {
        runOnce("carryingcapacity", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialCarryingCapacity(model.getView()));
        });
    }

    public void upgrading(Model model) {
        runOnce("upgrading", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialUpgrading(model.getView()));
        });
    }

    public void rituals(Model model) {
        runOnce("rituals", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialRituals(model.getView()));
        });
    }

    public void ritualBeams(Model model) {
        runOnce("ritualbeams", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialBeams(model.getView()));
        });
    }

    public void fishing(Model model) {
        runOnce("fishing", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialFishing(model.getView()));
        });
    }

    public void spellMasteries(Model model) {
        runOnce("spellmasteries", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new SpellMasteryHelpChapter(model.getView(), ""));
        });
    }

    public void basicControls(Model model) {
        runOnce("basiccontrols", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialBasicControls(model.getView()));
        });
    }

    public void gameLog(Model model) { // TODO: Change name of method to experience
        runOnce("experience", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new AttributesHelpChapter(model.getView()));
        });
    }

    public void quickCasting(Model model) {
        runOnce("quickcast", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialQuickCasting(model.getView()));
        });
    }

    public void questOffers(Model model) {
        runOnce("questoffers", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialQuestOffers(model.getView()));
        });
    }

    public void dragonTaming(Model model) {
        runOnce("dragontaming", () -> {
            model.getLog().waitForAnimationToFinish();
            model.transitionToDialog(new TutorialDragonTaming(model.getView()));
        });
    }

    private interface TutorialStep {
        void doStep();
    }
}

package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.conditions.FatigueCondition;
import model.combat.conditions.VampirismCondition;
import model.states.CombatEvent;
import util.MyRandom;
import view.help.HelpDialog;
import view.help.TutorialCombatResting;

public class RestCombatAction extends CombatAction {
    private boolean another;

    public RestCombatAction() {
        super("Rest", false);
        another = false;
    }

    public static boolean canDoRestAbility(Model model, GameCharacter performer) {
        return performer.getLevel() >= 3 && model.getParty().getBackRow().contains(performer);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialCombatResting(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().combatResting(model);
        boolean fullHP = performer.getHP() == performer.getMaxHP();
        boolean fullSP = performer.getSP() == performer.getMaxSP();
        if (fullHP && fullSP) {
            combat.println(performer.getFirstName() + " is already fully rested!");
            this.another = true;
        } else if (fullHP) {
            recoverSP(combat, performer);
        } else if (fullSP) {
            recoverHP(combat, performer);
        } else {
            if (MyRandom.flipCoin()) {
                recoverHP(combat, performer);
            } else {
                recoverSP(combat, performer);
            }
        }
        if (performer.hasCondition(FatigueCondition.class)) {
            SkillCheckResult result = performer.testSkillHidden(Skill.Endurance, performer.getAP(), 0);
            if (result.isSuccessful()) {
                combat.println(performer.getFirstName() + " has recovered from fatigue (Endurance " + result.asString() + ").");
                performer.removeCondition(FatigueCondition.class);
            }
        }
    }

    private void recoverHP(CombatEvent combat, GameCharacter performer) {
        combat.println(performer.getFirstName() + " recovered 1 Health Point.");
        performer.addToHP(1);
    }

    private void recoverSP(CombatEvent combat, GameCharacter performer) {
        if (!performer.hasCondition(VampirismCondition.class)) {
            combat.println(performer.getFirstName() + " recovered 1 Stamina Point.");
            performer.addToSP(1);
        } else {
            combat.println(performer.getFirstName() + " does not recover any stamina (vampirism).");
        }
    }

    @Override
    public boolean takeAnotherAction() {
        return another;
    }
}

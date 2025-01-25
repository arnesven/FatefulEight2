package model.combat.abilities;

import model.Model;
import model.actions.StaminaCombatAbility;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.conditions.ExposedCondition;
import model.items.weapons.BladedWeapon;
import model.states.CombatEvent;
import view.help.FeintAbilityHelpChapter;
import view.help.HelpDialog;

import java.util.List;

public class FeintAbility extends StaminaCombatAbility implements SkillAbilityCombatAction {
    public static final int BLADES_RANKS_REQUIREMENT = 4;
    public static final int DIFFICULTY = 7;

    public FeintAbility() {
        super("Feint", true);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new FeintAbilityHelpChapter(model.getView());
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.print(performer.getName() + " performs a feint... ");
        model.getTutorial().feintAbility(model);
        SkillCheckResult result = performer.testSkill(model, Skill.Blades, DIFFICULTY);
        if (result.isSuccessful()) {
            combat.println(result.asString() + ", " + target.getName() + " becomes exposed!");
            ExposedCondition cond = new ExposedCondition();
            cond.setDuration(1);
            target.addCondition(cond);
            performer.performAttack(model, combat, target);
        } else {
            combat.println(result.asString() + ".");
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return model.getParty().getFrontRow().contains(performer) &&
                target.canBeAttackedBy(performer) &&
                performer.getEquipment().getWeapon().isOfType(BladedWeapon.class);
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Blades);
    }

    @Override
    public int getRequiredRanks() {
        return BLADES_RANKS_REQUIREMENT;
    }
}

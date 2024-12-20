package model.combat.abilities;

import model.Model;
import model.actions.StaminaCombatAbility;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.*;
import model.items.weapons.PolearmWeapon;
import model.states.CombatEvent;
import model.states.GameState;
import view.help.HelpDialog;
import view.help.ImpaleAbilityHelpChapter;
import view.sprites.StrikeEffectSprite;

import java.util.List;

public class ImpaleAbility extends StaminaCombatAbility implements SkillAbilityCombatAction {
    public static final int POLEARMS_RANKS_REQUIREMENT = 5;

    public ImpaleAbility() {
        super("Impale", true);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new ImpaleAbilityHelpChapter(model.getView());
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getName() + " attempts to impale " + target.getName() +
                " with " + GameState.hisOrHer(performer.getGender()) + " " +
                performer.getEquipment().getWeapon().getName().toLowerCase() + ".");
        model.getTutorial().impaleAbility(model);
        int hpBefore = target.getHP();
        performer.doOneAttack(model, combat, target, false, 0,
                performer.getEquipment().getWeapon().getCriticalTarget(), new StrikeEffectSprite());
        if (hpBefore > target.getHP() && !target.isDead() && !target.hasCondition(ImpaledCondition.class)) {
            combat.println(performer.getFirstName() + " has impaled " + target.getName() + ". " + target.getName() + " is weakened, exposed and begins bleeding.");
            ImpaledCondition impCond = new ImpaledCondition(performer);
            target.addCondition(impCond);
            target.addCondition(new BleedingCondition());
            target.addCondition(new WeakenCondition());
            target.addCondition(new ExposedCondition());
            combat.println(performer.getFirstName() + " is clinched with " + target.getName() + ".");
            performer.addCondition(new ClinchedCondition(target, impCond));
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return model.getParty().getFrontRow().contains(performer) &&
                performer.getEquipment().getWeapon().isOfType(PolearmWeapon.class);
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Polearms);
    }

    @Override
    public int getRequiredRanks() {
        return POLEARMS_RANKS_REQUIREMENT;
    }
}

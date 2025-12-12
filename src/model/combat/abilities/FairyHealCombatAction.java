package model.combat.abilities;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.items.weapons.*;
import model.states.CombatEvent;
import view.help.HelpDialog;
import view.help.TutorialFairyHeal;
import view.sprites.DamageValueEffect;

import java.util.List;

public class FairyHealCombatAction extends SpecialAbilityCombatAction implements SkillAbilityCombatAction {
    private static final int DIFFICULTY = 7;
    public static final int REQUIRED_RANKS = 2;
    public static final Skill SKILL_TO_USE = Skill.MagicWhite;

    public FairyHealCombatAction() {
        super("Fairy Heal", false, false);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialFairyHeal(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().fairyHeal(model);
        SkillCheckResult result = performer.testSkill(model, SKILL_TO_USE);
        combat.println(performer.getFirstName() + " attempts Fairy Heal on " + target.getName() + ", " +
                SKILL_TO_USE.getName() + " " + result.asString() + ".");
        if (result.getModifiedRoll() < DIFFICULTY) {
            combat.println("But it failed.");
        } else { // FEATURE: Add "fairy" animation
            int health = (result.getModifiedRoll() - DIFFICULTY + 2) / 2;
            int hpBefore = target.getHP();
            target.addToHP(health);
            int totalRecovered = target.getHP() - hpBefore;
            combat.println(target.getName() + " recovers " + totalRecovered + " health points.");
            combat.addFloatyDamage(target, totalRecovered, DamageValueEffect.HEALING);
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        if (performer == target) {
            return false;
        }
        if (target.isDead()) {
            return false;
        }
        return (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(SKILL_TO_USE);
    }

    @Override
    public int getRequiredRanks() {
        return REQUIRED_RANKS;
    }
}

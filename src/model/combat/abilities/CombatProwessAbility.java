package model.combat.abilities;

import model.Model;
import model.actions.PassiveCombatAction;
import model.characters.GameCharacter;
import model.classes.Skill;
import view.help.CombatProwessAbilityHelpChapter;
import view.help.HelpDialog;

import java.util.List;

public class CombatProwessAbility extends PassiveCombatAction implements SkillAbilityCombatAction {
    public static final int REQUIRED_LEVEL = 7;
    public static final int REQUIRED_ACROBATICS_RANKS = 2;

    public CombatProwessAbility() {
        super("Combat Prowess");
    }

    public static PassiveCombatAction getPassiveCombatAbility() {
        return new CombatProwessAbility();
    }

    @Override
    public boolean canDoAbility(GameCharacter gc) {
        return gc.getLevel() >= REQUIRED_LEVEL && hasRequiredRanks(gc);
    }

    public static boolean checkForMultiTargetAttack(GameCharacter gc) {
        boolean canDo = getPassiveCombatAbility().canDoAbility(gc);
        boolean isMelee = !gc.getEquipment().getWeapon().isRangedAttack();
        return canDo && isMelee;
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new CombatProwessAbilityHelpChapter(model.getView());
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Acrobatics);
    }

    @Override
    public int getRequiredRanks() {
        return REQUIRED_ACROBATICS_RANKS;
    }
}

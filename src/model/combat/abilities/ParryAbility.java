package model.combat.abilities;

import model.Model;
import model.actions.PassiveCombatAction;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.Enemy;
import model.items.weapons.BladedWeapon;
import model.states.CombatEvent;
import util.MyRandom;
import view.help.HelpDialog;
import view.help.ParryAbilityHelpChapter;

import java.util.List;

public class ParryAbility extends PassiveCombatAction implements SkillAbilityCombatAction {
    public static final int BLADES_RANKS_REQUIREMENT = 3;

    private ParryAbility() {
        super("Parry");
    }

    public static PassiveCombatAction getPassiveCombatAbility() {
        return new ParryAbility();
    }

    public boolean canDoAbility(GameCharacter gc) {
        return hasRequiredRanks(gc);
    }

    public static boolean checkForParry(Model model, CombatEvent combatEvent, GameCharacter character, Enemy enemy) {
        if (character.getEquipment().getWeapon().isOfType(BladedWeapon.class) &&
            enemy.getAttackBehavior().isPhysicalAttack()) {
            combatEvent.println(character.getFirstName() + " parried " + enemy.getName() + "'s attack!");
            int chance = Math.min(5, character.getRankForSkill(Skill.Blades) - BLADES_RANKS_REQUIREMENT + 1);
            return MyRandom.rollD10() <= chance;
        }
        return false;
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new ParryAbilityHelpChapter(model.getView());
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

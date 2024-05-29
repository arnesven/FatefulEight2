package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.weapons.AxeWeapon;
import model.items.weapons.BluntWeapon;
import model.states.CombatEvent;
import view.help.HelpDialog;
import view.help.TutorialHeavyBlow;

public class HeavyBlowCombatAction extends StaminaCombatAbility {
    public static final int LABOR_RANKS_REQUIREMENT = 3;

    public HeavyBlowCombatAction() {
        super("Heavy Blow", true);
    }

    @Override
    public void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().heavyBlow(model);
        combat.println(performer.getFirstName() + " does a powerful swing!");
        performer.doOneAttack(model, combat, target, false, 2, 10);
    }

    public static boolean canDoHeavyBlowAbility(GameCharacter performer) {
        return performer.getRankForSkill(Skill.Labor) >= HeavyBlowCombatAction.LABOR_RANKS_REQUIREMENT &&
                (performer.getEquipment().getWeapon().isOfType(BluntWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(AxeWeapon.class));
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialHeavyBlow(model.getView());
    }
}

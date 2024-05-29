package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.states.CombatEvent;
import view.help.HelpDialog;
import view.help.TutorialSniperShot;

public class SniperShotCombatAction extends StaminaCombatAbility {
    public static final int PERCEPTION_RANKS_REQUIREMENT = 3;

    public SniperShotCombatAction() {
        super("Sniper Shot", false);
    }

    public static boolean canDoSniperShotAbility(GameCharacter performer) {
        return performer.getRankForSkill(Skill.Perception) >= PERCEPTION_RANKS_REQUIREMENT &&
                performer.getEquipment().getWeapon().isRangedAttack();
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().sniperShot(model);
        combat.println(performer.getFirstName() + " is zeroing in on a weak spot.");
        performer.doOneAttack(model, combat, target, false, 0, 6);

    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialSniperShot(model.getView());
    }
}

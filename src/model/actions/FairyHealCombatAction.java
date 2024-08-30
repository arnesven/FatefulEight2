package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.items.weapons.*;
import model.states.CombatEvent;
import view.help.HelpDialog;
import view.help.TutorialFairyHeal;
import view.sprites.DamageValueEffect;

public class FairyHealCombatAction extends CombatAction {
    private static final int DIFFICULTY = 7;
    public static final int REQUIRED_RANKS = 2;
    public static final Skill SKILL_TO_USE = Skill.MagicWhite;

    public FairyHealCombatAction() {
        super("Fairy Heal", false);
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
        } else {
            int health = (result.getModifiedRoll() - DIFFICULTY + 2) / 2;
            int hpBefore = target.getHP();
            target.addToHP(health);
            int totalRecovered = target.getHP() - hpBefore;
            combat.println(target.getName() + " recovers " + totalRecovered + " health points.");
            combat.addFloatyDamage(target, totalRecovered, DamageValueEffect.HEALING);
        }
    }

    public static boolean canDoAbility(GameCharacter performer, Combatant target) {
        if (performer == target) {
            return false;
        }
        if (target.isDead()) {
            return false;
        }
        return performer.getUnmodifiedRankForSkill(SKILL_TO_USE) >= REQUIRED_RANKS &&
                (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }
}

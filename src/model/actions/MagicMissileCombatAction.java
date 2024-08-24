package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.weapons.StaffWeapon;
import model.items.weapons.WandWeapon;
import model.states.CombatEvent;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialMagicMissile;
import view.sprites.DamageValueEffect;
import view.sprites.EntropicBoltEffect;

public class MagicMissileCombatAction extends CombatAction {
    public static final Skill SKILL_TO_USE = Skill.MagicRed;
    public static final int DIFFICULTY = 7;
    public static final int REQUIRED_RANKS = 2;

    public MagicMissileCombatAction() {
        super("Magic Missile", true);
    }

    public static boolean canDoAbility(GameCharacter performer, Combatant target) {
        return target instanceof Enemy &&
                performer.getUnmodifiedRankForSkill(SKILL_TO_USE) >= REQUIRED_RANKS &&
                (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialMagicMissile(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.print(performer.getFirstName() + " attempts Magic Missile on " + target.getName());
        model.getTutorial().magicMissile(model);
        SkillCheckResult result = performer.testSkill(model, SKILL_TO_USE);
        combat.println(", " + SKILL_TO_USE.getName() + " " + result.asString() + ".");
        if (result.getModifiedRoll() < DIFFICULTY) {
            combat.println("But it failed.");
        } else {
            int damage = (result.getModifiedRoll() - DIFFICULTY + 3) / 3;
            damage *= damage;
            combat.println(target.getName() + " was hit by the magic missile, took " + damage + " damage.");
            combat.addSpecialEffect(target, new EntropicBoltEffect(MyColors.LIGHT_RED));
            combat.addFloatyDamage(target, damage, DamageValueEffect.MAGICAL_DAMAGE);
            combat.doDamageToEnemy(target, damage, performer);
        }
    }
}

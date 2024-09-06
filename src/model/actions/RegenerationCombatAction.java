package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.items.weapons.StaffWeapon;
import model.items.weapons.WandWeapon;
import model.states.CombatEvent;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialRegenerate;
import view.sprites.CurlySpiralAnimation;
import view.sprites.RunOnceAnimationSprite;

public class RegenerationCombatAction extends SpecialAbilityCombatAction {
    public static final int REQUIRED_RANKS = 2;
    public static final Skill SKILL_TO_USE = Skill.MagicGreen;
    public static final int DIFFICULTY = 7;

    public RegenerationCombatAction() {
        super("Regenerate", false, false);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialRegenerate(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().regenerate(model);
        SkillCheckResult result = performer.testSkill(model, SKILL_TO_USE);
        combat.println(performer.getFirstName() + " attempts Regenerate on " + target.getName() + ", " +
                SKILL_TO_USE.getName() + " " + result.asString() + ".");
        if (result.getModifiedRoll() < DIFFICULTY || target.hasCondition(RegenerationCondition.class)) {
            combat.println("But it failed.");
        } else {
            int turns = result.getModifiedRoll() - DIFFICULTY + 1;
            RunOnceAnimationSprite restAni = new CurlySpiralAnimation(MyColors.GREEN);
            combat.addSpecialEffect(target, restAni);
            combat.waitUntil(restAni, RunOnceAnimationSprite::isDone);
            combat.println(target.getName() + " starts regenerating health.");
            target.addCondition(new RegenerationCondition(turns));
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(SKILL_TO_USE) >= REQUIRED_RANKS;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return !target.isDead() && (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }
}

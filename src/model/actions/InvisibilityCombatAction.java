package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.combat.conditions.InvisibilityCondition;
import model.items.weapons.StaffWeapon;
import model.items.weapons.WandWeapon;
import model.states.CombatEvent;
import view.help.HelpDialog;
import view.help.TutorialInvisibility;
import view.sprites.SmokeBallAnimation;
import view.sprites.SmokePuffAnimation;

public class InvisibilityCombatAction extends SpecialAbilityCombatAction {
    public static final Skill SKILL_TO_USE = Skill.MagicBlue;
    public static final int REQUIRED_RANKS = 2;
    public static final int DIFFICULTY = 7;

    public InvisibilityCombatAction() {
        super("Invisibility", false, false);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialInvisibility(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().invisibility(model);
        SkillCheckResult result = performer.testSkill(model, SKILL_TO_USE);
        combat.println(performer.getFirstName() + " attempts Invisibility on " + target.getName() + ", " +
                SKILL_TO_USE.getName() + " " + result.asString() + ".");
        if (result.getModifiedRoll() < DIFFICULTY || target.hasCondition(InvisibilityCondition.class)) {
            combat.println("But it failed.");
        } else {
            int turns = (result.getModifiedRoll() - DIFFICULTY + 3) / 3;
            combat.println(target.getName() + " turns invisible!");
            combat.addSpecialEffect(target, new SmokePuffAnimation());
            target.addCondition(new InvisibilityCondition(turns));
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(SKILL_TO_USE) >= REQUIRED_RANKS;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return target instanceof GameCharacter &&
                (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }
}

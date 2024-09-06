package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.weapons.AxeWeapon;
import model.items.weapons.BluntWeapon;
import model.states.CombatEvent;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialHeavyBlow;
import view.sprites.RunOnceAnimationSprite;

public class HeavyBlowCombatAction extends StaminaCombatAbility {
    public static final int LABOR_RANKS_REQUIREMENT = 3;

    public HeavyBlowCombatAction() {
        super("Heavy Blow", true);
    }

    @Override
    public void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().heavyBlow(model);
        combat.println(performer.getFirstName() + " does a powerful swing!");
        performer.doOneAttack(model, combat, target, false,
                2, 10, new HeavyBlowStrikeEffectSprite());
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialHeavyBlow(model.getView());
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(Skill.Labor) >= HeavyBlowCombatAction.LABOR_RANKS_REQUIREMENT;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return model.getParty().getFrontRow().contains(performer) &&
                (performer.getEquipment().getWeapon().isOfType(BluntWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(AxeWeapon.class));
    }

    private static class HeavyBlowStrikeEffectSprite extends RunOnceAnimationSprite {
        public HeavyBlowStrikeEffectSprite() {
            super("hvyblowstrike", "combat.png",
                    8, 11, 32, 32, 6, MyColors.WHITE);
            setAnimationDelay(5);
        }
    }
}

package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.states.CombatEvent;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialSniperShot;
import view.sprites.RunOnceAnimationSprite;

public class SniperShotCombatAction extends StaminaCombatAbility {
    public static final int PERCEPTION_RANKS_REQUIREMENT = 3;

    public SniperShotCombatAction() {
        super("Sniper Shot", false);
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().sniperShot(model);
        combat.println(performer.getFirstName() + " is zeroing in on a weak spot.");
        performer.doOneAttack(model, combat, target, false, 0, 6,
                new SniperShotStrikeEffectSprite());

    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialSniperShot(model.getView());
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(Skill.Perception) >= PERCEPTION_RANKS_REQUIREMENT;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return performer.getEquipment().getWeapon().isRangedAttack();
    }

    private static class SniperShotStrikeEffectSprite extends RunOnceAnimationSprite {
        public SniperShotStrikeEffectSprite() {
            super("sniperstrike", "combat.png",
                    8, 12, 32, 32, 8, MyColors.WHITE);
            setAnimationDelay(4);
        }
    }
}

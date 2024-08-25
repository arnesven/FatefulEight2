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

    public static boolean canDoSniperShotAbility(GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(Skill.Perception) >= PERCEPTION_RANKS_REQUIREMENT &&
                performer.getEquipment().getWeapon().isRangedAttack();
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().sniperShot(model);
        combat.println(performer.getFirstName() + " is zeroing in on a weak spot.");
        performer.doOneAttack(model, combat, target, false, 0, 6,
                new SniperShotStrikeEffectSprite()); // TODO: Add special animation

    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialSniperShot(model.getView());
    }

    private static class SniperShotStrikeEffectSprite extends RunOnceAnimationSprite {
        public SniperShotStrikeEffectSprite() {
            super("sniperstrike", "combat.png",
                    8, 12, 32, 32, 8, MyColors.WHITE);
            setAnimationDelay(4);
        }
    }
}

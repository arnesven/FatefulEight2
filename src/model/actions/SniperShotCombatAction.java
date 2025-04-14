package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.abilities.SkillAbilityCombatAction;
import model.enemies.Enemy;
import model.states.CombatEvent;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialSniperShot;
import view.sprites.RunOnceAnimationSprite;

import java.util.List;

public class SniperShotCombatAction extends StaminaCombatAbility implements SkillAbilityCombatAction {
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
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return performer.getEquipment().getWeapon().isRangedAttack() && target instanceof Enemy;
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Perception);
    }

    @Override
    public int getRequiredRanks() {
        return PERCEPTION_RANKS_REQUIREMENT;
    }

    private static class SniperShotStrikeEffectSprite extends RunOnceAnimationSprite {
        public SniperShotStrikeEffectSprite() {
            super("sniperstrike", "combat.png",
                    8, 12, 32, 32, 8, MyColors.WHITE);
            setAnimationDelay(4);
        }
    }
}

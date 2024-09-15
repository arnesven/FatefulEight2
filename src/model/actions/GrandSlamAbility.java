package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.TimedParalysisCondition;
import model.items.weapons.AxeWeapon;
import model.items.weapons.BluntWeapon;
import model.states.CombatEvent;
import view.MyColors;
import view.help.GrandSlamHelpChapter;
import view.help.HelpDialog;
import view.sprites.RunOnceAnimationSprite;

public class GrandSlamAbility extends StaminaCombatAbility {
    public static final int BLUNT_RANKS_REQUIRED = 5;

    public GrandSlamAbility() {
        super("Grand Slam", true);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new GrandSlamHelpChapter(model.getView());
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getFirstName() + " does a grand slam!");
        model.getTutorial().grandSlamAbility(model);
        performer.doOneAttack(model, combat, target, false,
                3, performer.getEquipment().getWeapon().getCriticalTarget(),
                new GrandSlamStrikeEffectSprite());
        if (!target.isDead()) {
            combat.println(target.getName() + " has been knocked down!");
            target.addCondition(new TimedParalysisCondition());
        }
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return model.getParty().getFrontRow().contains(performer) &&
                performer.getEquipment().getWeapon().isOfType(BluntWeapon.class);
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(Skill.BluntWeapons) >= BLUNT_RANKS_REQUIRED;
    }

    private static class GrandSlamStrikeEffectSprite extends RunOnceAnimationSprite {
        public GrandSlamStrikeEffectSprite() {
            super("grandslamstrike", "combat.png",
                    2, 11, 32, 32, 6, MyColors.BLACK);
            setColor2(MyColors.WHITE);
            setAnimationDelay(5);
        }
    }
}

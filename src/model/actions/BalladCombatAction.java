package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.items.weapons.Lute;
import model.states.CombatEvent;
import sound.SoundEffects;

public class BalladCombatAction extends InspireCombatAction {
    @Override
    public String getName() {
        return "Ballad";
    }

    protected SkillCheckResult doSkillCheck(Model model, CombatEvent combat, GameCharacter performer) {
        combat.println(performer.getFirstName() + " attempts to inspire the party with a powerful ballad.");
        SoundEffects.playSound("lute");
        return model.getParty().doSkillCheckWithReRoll(model, combat, performer,
                Skill.Entertain, 8, 0, 1);
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return super.possessesAbility(model, performer) && meetsOtherRequirements(model, performer, null);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return Lute.canDoAbility(performer);
    }
}

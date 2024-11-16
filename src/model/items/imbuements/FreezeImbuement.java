package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.TimedParalysisCondition;
import model.states.CombatEvent;
import util.MyRandom;

public class FreezeImbuement extends WeaponImbuement {
    @Override
    public String getText() {
        return "20% Chance to apply Freeze";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (!target.isDead() && MyRandom.rollD10() >= 9 && !target.hasCondition(TimedParalysisCondition.class)) {
            target.addCondition(new FreezeCondition());
            combatEvent.println(target.getName() + " is paralyzed by the freezing cold!");
        }
    }

    private static class FreezeCondition extends TimedParalysisCondition {
        public FreezeCondition() {
            super(1);
        }
    }
}

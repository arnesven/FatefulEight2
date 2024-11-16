package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.BleedingCondition;
import model.enemies.HumanoidEnemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.DamageValueEffect;

public class DismembermentImbuement extends WeaponImbuement {

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (!target.isDead() && target instanceof HumanoidEnemy && MyRandom.rollD10() <= 3) {
            combatEvent.println(gameCharacter.getName() + " chopped a limb off of " + target.getName() + "!");
            int extraDamage = MyRandom.randInt(1, 4);
            combatEvent.doDamageToEnemy(target, extraDamage, gameCharacter);
            combatEvent.addFloatyDamage(target, extraDamage, DamageValueEffect.CRITICAL_DAMAGE);
            target.addCondition(new BleedingCondition());
        }
    }

    @Override
    public String getText() {
        return "30% chance of dismemberment on humanoid opponents.";
    }
}

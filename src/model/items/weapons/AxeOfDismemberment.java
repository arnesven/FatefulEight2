package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.BleedingCondition;
import model.enemies.HumanoidEnemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class AxeOfDismemberment extends AxeWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 5);

    public AxeOfDismemberment() {
        super("Axe of Dismemberment", 90, new int[]{6, 8, 10, 13}, true);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

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
    public String getExtraText() {
        return ", 30% chance of dismemberment on humanoid opponents.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new AxeOfDismemberment();
    }
}

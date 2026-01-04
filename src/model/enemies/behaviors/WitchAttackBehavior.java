package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.FeebleCondition;
import model.combat.conditions.PoisonCondition;
import model.enemies.BlueGelatinousBlobEnemy;
import model.enemies.Enemy;
import model.enemies.GreenGelatinousBlobEnemy;
import model.enemies.MountainWolfEnemy;
import model.items.potions.PoisonPotion;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.RangedMagicStrikeEffectSprite;
import view.sprites.RunOnceAnimationSprite;

public class WitchAttackBehavior extends SummonAttackBehavior {

    private int summonCount = 0;

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll == 1 && summonCount < 3) {
            super.performAttack(model, enemy, target, combatEvent);
            summonCount++;
        } else if (dieRoll < 4) {
            combatEvent.println(enemy.getName() + " tosses a poison vial!");
            if (!PoisonPotion.didResistWeakPotion(target)) {
                combatEvent.println(target.getName() + " became poisoned.");
                target.addCondition(new PoisonCondition());
            } else {
                combatEvent.println(target.getName() + " resisted the poison!");
            }
        } else if (dieRoll < 7 && !target.hasCondition(FeebleCondition.class)) {
            combatEvent.print(enemy.getName() + " casts a curse on " + target.getName() + ". ");
            target.addCondition(new FeebleCondition());
            combatEvent.println(target.getName() + " becomes Enfeebled!");
            combatEvent.addSpecialEffect(target, new RangedMagicStrikeEffectSprite(MyColors.PURPLE));
        } else {
            new MagicRangedAttackBehavior().performAttack(model, enemy, target, combatEvent);
        }
    }


    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new RangedMagicStrikeEffectSprite();
    }

    @Override
    protected Enemy makeEnemy(Model model) {
        return new GreenGelatinousBlobEnemy('B');
    }

    @Override
    public String getUnderText() {
        return "Summon/Poison/Curse";
    }
}

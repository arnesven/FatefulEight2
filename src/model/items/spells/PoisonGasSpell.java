package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.EnemyPoisonCondition;
import model.enemies.AutomatonEnemy;
import model.enemies.Enemy;
import model.enemies.UndeadEnemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class PoisonGasSpell extends CombatSpell {
    private static final Sprite SPRITE = new BlackSpellSprite(5,true);

    public PoisonGasSpell() {
        super("Poison Gas", 20, MyColors.BLACK, 8, 1, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PoisonGasSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        addPoisonGasEffect(combat, performer, target);
    }

    public void addPoisonGasEffect(CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target instanceof UndeadEnemy || target instanceof AutomatonEnemy ||
                target.hasCondition(EnemyPoisonCondition.class)) {
            combat.println(getName() + " has no effect on " + target.getName());
            return;
        }
        combat.addSpecialEffect(target, new GreenSmokeAnimation());
        combat.println(target.getName() + " has been poisoned!");
        target.addCondition(new EnemyPoisonCondition(performer));
    }

    @Override
    public String getDescription() {
        return "Releases a poisonous gas cloud that damages enemies over time.";
    }

    private static class GreenSmokeAnimation extends RunOnceAnimationSprite {
        public GreenSmokeAnimation() {
            super("greensmokeball", "combat.png", 0, 16, 32, 32, 8, MyColors.LIGHT_GREEN);
            setColor1(MyColors.WHITE);
        }
    }
}

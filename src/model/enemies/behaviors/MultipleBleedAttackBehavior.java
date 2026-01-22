package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import view.sprites.RunOnceAnimationSprite;

public class MultipleBleedAttackBehavior extends MultiAttackBehavior {
    private final BleedAttackBehavior bleed;

    public MultipleBleedAttackBehavior() {
        super(2);
        this.bleed = new BleedAttackBehavior(3);
    }

    @Override
    public String getUnderText() {
        return super.getUnderText() + " " + bleed.getUnderText();
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        bleed.performAttack(model, enemy, target, combatEvent);
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return bleed.getStrikeEffect();
    }
}

package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;

import view.sprites.MagmaBlastEffectSprite;

import java.util.List;

public class DestructionSpellAttackBehavior extends SpellAttackBehavior {

    public DestructionSpellAttackBehavior() {
        super("Destruction", 1);
    }

    @Override
    protected void resolveSpell(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        List<GameCharacter> targets = super.getCharacterAndAlliesTargets(model, combat, enemy, target, 3);
        for (GameCharacter t : targets) {
            combat.print(t.getName() + " was struck by the blast. ");
            combat.addSpecialEffect(t, new MagmaBlastEffectSprite());
            t.getAttackedBy(enemy, model, combat);
        }
    }
}

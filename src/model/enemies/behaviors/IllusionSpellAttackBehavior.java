package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.TimedParalysisCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.PhantasmEffect;

import java.util.List;

public class IllusionSpellAttackBehavior extends SpellAttackBehavior {

    public IllusionSpellAttackBehavior() {
        super("Illusion", 2);
    }

    @Override
    protected void resolveSpell(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        List<GameCharacter> targets = getCharacterAndAlliesTargets(model, combat, enemy,
                target, MyRandom.randInt(1, 3));
        for (GameCharacter gc : targets) {
            combat.println(gc.getName() + " has been paralyzed with fear!");
            gc.addCondition(new TimedParalysisCondition());
            combat.addSpecialEffect(gc, new PhantasmEffect());
        }
    }
}

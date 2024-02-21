package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.ParalysisCondition;
import model.combat.TimedParalysisCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class ConjurePhantasmSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(4, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public ConjurePhantasmSpell() {
        super("Conjure Phantasm", 24, MyColors.BLUE, 10, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ConjurePhantasmSpell();
    }

    @Override
    public String getDescription() {
        return "Terrifies enemies and prevents their attacking for 2 rounds.";
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        List<Enemy> targets = getTargets(combat, target, 3 + getMasteryLevel(performer));
        for (Enemy e : targets) {
            combat.println(e.getName() + " has been paralyzed with fear!");
            e.addCondition(new TimedParalysisCondition());
            combat.addSpecialEffect(e, new PhantasmEffect());
        }
    }

    private static class PhantasmEffect extends RunOnceAnimationSprite {
        private int shift = 0;

        public PhantasmEffect() {
            super("phantasmeffect", "combat.png", 2, 8, 32, 32, 6, MyColors.WHITE);
        }

        @Override
        public void stepAnimation(long elapsedTimeMs, Model model) {
            super.stepAnimation(elapsedTimeMs, model);
            shift += 1;
        }

        @Override
        public int getYShift() {
            return shift;
        }
    }
}

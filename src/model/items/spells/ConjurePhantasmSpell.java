package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.TimedParalysisCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;

import java.util.List;

public class ConjurePhantasmSpell extends CombatSpell {
    public static final String SPELL_NAME = "Conjure Phantasm";
    private static final Sprite SPRITE = new CombatSpellSprite(4, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public ConjurePhantasmSpell() {
        super(SPELL_NAME, 24, MyColors.BLUE, 10, 2);
    }

    public static String getMagicExpertTips() {
        return "When you gain mastery levels in Conjure Phantasm, you can target more enemies.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
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
        List<Enemy> targets = getTargets(combat, target, 2 + getMasteryLevel(performer));
        for (Enemy e : targets) {
            if (!e.isFearless()) {
                combat.println(e.getName() + " has been paralyzed with fear!");
                e.addCondition(new TimedParalysisCondition());
                combat.addSpecialEffect(e, new PhantasmEffect());
            } else {
                combat.println(this.getName() + " has no effect on " + e.getName() + ".");
            }
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

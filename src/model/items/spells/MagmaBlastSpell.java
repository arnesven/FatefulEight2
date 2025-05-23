package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class MagmaBlastSpell extends CombatSpell {
    public static final String SPELL_NAME = "Magma Blast";
    private static final Sprite SPRITE = new RedSpellSprite(1, true);

    public MagmaBlastSpell() {
        super(SPELL_NAME, 36, MyColors.RED, 11, 4);
    }

    public static String getMagicExpertTips() {
        return "When you gain levels of mastery in " + SPELL_NAME +
                ", you both deal more damage and can target more opponents.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MagmaBlastSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        List<Enemy> targets = getTargets(combat, target, MyRandom.randInt(1, 3) + getMasteryLevel(performer));
        for (Enemy e : targets) {
            int damage = MyRandom.randInt(7) + 1 + getMasteryLevel(performer);
            combat.println(e.getName() + " was struck by the blast, took " + damage + " damage.");
            combat.addFloatyDamage(e, damage, DamageValueEffect.MAGICAL_DAMAGE);
            combat.addSpecialEffect(e, new MagmaBlastEffectSprite());
            combat.doDamageToEnemy(e, damage, performer);
        }
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Hurls a fiery ball against your enemies which explodes upon contact.";
    }

    private static class MagmaBlastEffectSprite extends RunOnceAnimationSprite {
        private int shift = 48;
        public MagmaBlastEffectSprite() {
            super("magmablasteffect", "combat.png", 0, 11, 32, 32, 8, MyColors.RED);
            setColor2(MyColors.ORANGE);
        }

        @Override
        public int getYShift() {
            return shift;
        }

        @Override
        public void stepAnimation(long elapsedTimeMs, Model model) {
            super.stepAnimation(elapsedTimeMs, model);
            shift -= 2;
        }
    }
}

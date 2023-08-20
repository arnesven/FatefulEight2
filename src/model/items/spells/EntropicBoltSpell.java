package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;

public class EntropicBoltSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(7, 8, MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);

    public EntropicBoltSpell() {
        super("Entropic Bolt", 32, MyColors.RED, 10, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EntropicBoltSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        int damage = 4;
        combat.println(target.getName() + " was hit by entropic bolt, took " + damage + " damage.");
        combat.addFloatyDamage(target, damage, true);
        combat.addSpecialEffect(target, new EntropicBoltEffect());
        combat.doDamageToEnemy(target, damage, performer);
    }

    @Override
    public String getDescription() {
        return "A spell which projects an energy missile at an enemy.";
    }

    private static class EntropicBoltEffect extends RunOnceAnimationSprite {
        private int shift = 48;

        public EntropicBoltEffect() {
            super("entropicbolt", "combat.png", 0, 12, 32, 32, 4, MyColors.WHITE);
            setColor2(MyColors.LIGHT_BLUE);
        }

        @Override
        public int getYShift() {
            return shift;
        }

        @Override
        public void stepAnimation(long elapsedTimeMs, Model model) {
            super.stepAnimation(elapsedTimeMs, model);
            shift -= 4;
        }
    }
}

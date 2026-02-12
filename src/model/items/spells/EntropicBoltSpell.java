package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class EntropicBoltSpell extends CombatSpell {
    public static final String SPELL_NAME = "Entropic Bolt";
    private static final Sprite SPRITE = new RedSpellSprite(2, true);

    public EntropicBoltSpell() {
        super(SPELL_NAME, 32, MyColors.RED, 10, 2);
    }

    public static String getMagicExpertTips() {
        return "The damage of Entropic Bolt is based on your level, along with your level of mastery.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
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
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        int damage = 4 + performer.getLevel() / 2 + getMasteryLevel(performer)*2;
        combat.println(target.getName() + " was hit by entropic bolt, took " + damage + " damage.");
        combat.addFloatyDamage(target, damage, DamageValueEffect.MAGICAL_DAMAGE);
        combat.addSpecialEffect(target, new EntropicBoltEffect(MyColors.LIGHT_BLUE));
        combat.doDamageToEnemy(target, damage, performer);
    }

    @Override
    public boolean canBeUsedWithMass() {
        return true;
    }

    @Override
    public String getDescription() {
        return "A spell which projects an energy missile at an enemy.";
    }

}

package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.WeakenCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.BlackSpellSprite;
import view.sprites.DownArrowAnimation;
import view.sprites.Sprite;

import java.util.List;

public class WeakenSpell extends CombatSpell {
    public static final String SPELL_NAME = "Weaken";
    private static final Sprite SPRITE = new BlackSpellSprite(3, true);

    public WeakenSpell() {
        super(SPELL_NAME, 18, MyColors.BLACK, 9, 1);
    }

    public static String getMagicExpertTips() {
        return "The effect of Weaken lasts longer if you gain some levels of mastery in that spell.";
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
        return new WeakenSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getName() + " has weakened the enemy.");
        List<Enemy> targets = getTargets(combat, target, 4);
        for (Enemy e : targets) {
            e.addCondition(new WeakenCondition(3 + getMasteryLevel(performer)));
            combat.addSpecialEffect(e, new DownArrowAnimation());
        }
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Weakens up to 4 enemies, reducing their damage for 4 turns.";
    }

}

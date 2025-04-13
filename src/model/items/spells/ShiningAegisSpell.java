package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.ShiningAegisCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class ShiningAegisSpell extends CombatSpell {
    public static final String SPELL_NAME = "Shining Aegis";
    private static final Sprite SPRITE = new WhiteSpellSprite(1, true);

    public ShiningAegisSpell() {
        super(SPELL_NAME, 12, MyColors.WHITE, 8, 1);
    }

    public static String getMagicExpertTips() {
        return "When you become a master of the Shining Aegis spell, the effect will last longer.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ShiningAegisSpell();
    }

    @Override
    public String getDescription() {
        return "Conjures a shield of light which gives the target 3 AP for 5 rounds.";
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target.hasCondition(ShiningAegisCondition.class)) {
            combat.println(getName() + " had no effect on " + target.getName() + ".");
        } else {
            target.addCondition(new ShiningAegisCondition(5 + getMasteryLevel(performer)));
            combat.addSpecialEffect(target, new UpArrowAnimation());
        }
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }
}

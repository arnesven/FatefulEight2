package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import view.sprites.ColorlessSpellSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class QuickeningSpell extends CombatSpell {
    public static final int SPEED_BONUS = 6;
    public static final String SPELL_NAME = "Quickening";
    private static final Sprite SPRITE = new ColorlessSpellSprite(3, true);

    public QuickeningSpell() {
        super(SPELL_NAME, 14, COLORLESS, 7, 0, false);
    }

    public static String getMagicExpertTips() {
        return "When you become a master at Quickening, the speed boost last longer on your target.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new QuickeningSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (!target.hasCondition(QuickenedCondition.class)) {
            target.addCondition(new QuickenedCondition(5 + getMasteryLevel(performer)*2));
            combat.println(target.getName() + " has sped up!");
            combat.addSpecialEffect(target, new UpArrowAnimation());
        } else {
            combat.println(getName() + " had no effect on " + target.getName() + ".");
        }
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Grants the target +6 speed for 5 turns.";
    }

}

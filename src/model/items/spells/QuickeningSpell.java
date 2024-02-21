package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class QuickeningSpell extends CombatSpell {
    public static final int SPEED_BONUS = 6;
    private static final Sprite SPRITE = new CombatSpellSprite(2, 8, MyColors.BEIGE, MyColors.GREEN, MyColors.WHITE);

    public QuickeningSpell() {
        super("Quickening", 14, MyColors.GREEN, 7, 0, false);
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
    protected boolean masteriesEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Grants the target +6 speed for 5 turns.";
    }

}

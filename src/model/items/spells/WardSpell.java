package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.WardCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class WardSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(5, 8, MyColors.BROWN, MyColors.WHITE, MyColors.GOLD);

    public WardSpell() {
        super("Ward", 12, MyColors.WHITE, 8, 0, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WardSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (!target.hasCondition(WardCondition.class)) {
            target.addCondition(new WardCondition());
            combat.addSpecialEffect(target, new UpArrowAnimation());
        } else {
            combat.println(getName() + " had no effect on " + target.getName() + ".");
        }
    }

    @Override
    public String getDescription() {
        return "Protection which makes the target immune to magic attacks for 2 rounds.";
    }
}

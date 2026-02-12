package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.ErodeCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.RedSpellSprite;
import view.sprites.Sprite;

public class ErodeSpell extends CombatSpell {
    private static final Sprite SPRITE = new RedSpellSprite(5, true);

    public ErodeSpell() {
        super("Erode", 12, MyColors.RED, 7, 1, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ErodeSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (((Enemy)target).getDamageReduction() == 0) {
            combat.println(getName() + " has no effect on " + target.getName() + ".");
        } else {
            combat.println(target.getName() + "'s armor has been nullified!");
            target.addCondition(new ErodeCondition());
        }
    }

    @Override
    public boolean canBeUsedWithMass() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Wears down a physical object until it crumbles to dust.";
    }
}

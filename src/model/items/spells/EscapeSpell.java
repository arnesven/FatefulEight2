package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.BlueSpellSprite;
import view.sprites.Sprite;

public class EscapeSpell extends CombatSpell {

    private static final Sprite SPRITE = new BlueSpellSprite(4, true);

    public EscapeSpell() {
        super("Escape", 24, MyColors.BLUE, 7, 2, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EscapeSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getFirstName() + " teleports the party out of combat!");
        combat.setTimeLimit(combat.getCurrentRound());
    }

    @Override
    public boolean canBeUsedWithMass() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Teleports the party out of combat.";
    }
}

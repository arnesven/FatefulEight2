package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class FireWallSpell extends CombatSpell {

    private static final Sprite SPRITE = new CombatSpellSprite(12, 8, MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);

    public FireWallSpell() {
        super("Fire Wall", 28, MyColors.RED, 9, 3, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    protected boolean masteriesEnabled() {
        return true;
    }

    @Override
    public Item copy() {
        return new FireWallSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getName() + " summons a wall of flames!");
        combat.setFlameWall(performer, 1 + getMasteryLevel(performer));
    }

    @Override
    public String getDescription() {
        return "Conjures a barrier of fire for one combat turn, " +
                "dealing damage to combatants that come close.";
    }
}

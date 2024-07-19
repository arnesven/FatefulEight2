package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.enemies.UndeadEnemy;
import model.items.Item;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TurnUndeadSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(4, 8, MyColors.BROWN, MyColors.WHITE, MyColors.DARK_GRAY);

    public TurnUndeadSpell() {
        super("Turn Undead", 20, MyColors.WHITE, 10, 1, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "A spell for disintegrating undead.";
    }

    @Override
    public Item copy() {
        return new TurnUndeadSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target instanceof UndeadEnemy) {
            int dieRoll = MyRandom.rollD10();
            if (dieRoll > 1 && dieRoll < target.getMaxHP() / 2) {
                combat.println(target.getName() + " resisted the spell!");
            } else {
                combat.println(target.getName() + " took " + target.getHP() + " damage and was disintegrated into dust!");
                combat.doDamageToEnemy(target, target.getHP(), performer);
            }
        } else {
            combat.println(getName() + " has no effect on " + target.getName() + ".");
        }
    }
}

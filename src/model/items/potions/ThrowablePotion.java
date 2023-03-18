package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

public abstract class ThrowablePotion extends Potion {
    public ThrowablePotion(String name, int cost) {
        super(name, cost);
    }

    public abstract void throwYourself(Model model, CombatEvent combat, GameCharacter performer, Combatant target);
}

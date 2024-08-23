package model.items.spells;

import model.Model;
import model.combat.Combatant;
import model.states.GameState;

public interface FullRoundSpell {
    void castingComplete(Model model, GameState state, Combatant performer, Combatant target);

    int getCastTime();
}

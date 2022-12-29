package model.states;

import model.characters.GameCharacter;
import model.items.spells.Spell;

public class SpellCastException extends RuntimeException {

    private final Spell spell;
    private final GameCharacter caster;

    public SpellCastException(Spell castSpell, GameCharacter caster) {
        this.spell = castSpell;
        this.caster = caster;
    }

    @Override
    public String getMessage() {
        return "No handler for spell cast!";
    }

    public Spell getSpell() {
        return spell;
    }

    public GameCharacter getCaster() {
        return caster;
    }
}

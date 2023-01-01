package model;

import model.characters.GameCharacter;
import model.items.spells.Spell;
import util.MyPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SpellHandler extends ArrayList<MyPair<Spell, GameCharacter>> {
    private Set<String> acceptedSpells = new HashSet<>();

    public boolean tryCast(Spell spell, GameCharacter gc) {
        if (acceptedSpells.contains(spell.getName())) {
            this.add(new MyPair<>(spell, gc));
            acceptedSpells.remove(spell.getName());
            return true;
        }
        return false;
    }

    public void acceptSpell(String nameOfSpell) {
        acceptedSpells.add(nameOfSpell);
    }

    public boolean spellReady() {
        return !isEmpty();
    }

    public MyPair<Spell, GameCharacter> getCastSpell() {
        MyPair<Spell, GameCharacter> p = get(size()-1);
        remove(p);
        return p;
    }

    public void unacceptSpell(String spellName) {
        acceptedSpells.remove(spellName);
    }
}

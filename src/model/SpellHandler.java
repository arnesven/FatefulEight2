package model;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.spells.SkillBoostingSpell;
import model.items.spells.Spell;
import util.MyLists;
import util.MyPair;

import java.util.*;

public class SpellHandler extends ArrayList<MyPair<Spell, GameCharacter>> {
    private Set<String> acceptedSpells = new HashSet<>();
    private int creatureComfortsCastOn = 0;

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

    public void acceptSkillBoostingSpells(Party party, Skill skill) {
        for (Spell spell : party.getSpells()) {
            if (spell instanceof SkillBoostingSpell && ((SkillBoostingSpell) spell).boostsSkill(skill)) {
                acceptSpell(spell.getName());
            }
        }
    }

    public void unacceptSkillBoostingSpells(Skill skill) {
        acceptedSpells.removeIf((String name) -> name.equals(skill.name()));
    }

    public boolean creatureComfortsCastToday(Model model) {
        return creatureComfortsCastOn == model.getDay();
    }

    public void setCreatureComfortsCastOnDay(int day) {
        creatureComfortsCastOn = day;
    }

    public void pollCastSpells() {
        if (spellReady()) {
            MyPair<Spell, GameCharacter> pair = getCastSpell();
            pair.first.triggerInterrupt(pair.second);
        }
    }

    public boolean isAlreadyCasting(GameCharacter gc) {
        return MyLists.any(this, pair -> pair.second == gc);
    }
}

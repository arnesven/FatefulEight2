package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.states.CombatEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CombatAction implements Serializable {
    private final String name;
    public CombatAction(String name) {
        this.name = name;
    }

    public abstract void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target);

    public String getName() {
        return name;
    }

    public boolean hasInnerMenu() {
        return false;
    }

    public List<CombatAction> getInnerActions(Model model) {
        return new ArrayList<>();
    }
}

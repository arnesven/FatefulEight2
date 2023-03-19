package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public abstract class CombatSpell extends Spell {
    public CombatSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    public abstract boolean canBeCastOn(Model model, Combatant target);

    public abstract void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target);

    protected List<Enemy> getTargets(CombatEvent combat, Combatant target, int number) {
        List<Enemy> targets = new ArrayList<>(combat.getEnemies());
        targets.remove(target);
        while (targets.size() > number - 1) {
            targets.remove(MyRandom.randInt(targets.size()));
        }
        targets.add((Enemy)target);
        return targets;
    }

    @Override
    protected int getExperience() {
        return 5;
    }

    @Override
    public String castFromMenu(Model model, GameCharacter gc) {
        return getName() + " can only be cast in combat.";
    }
}

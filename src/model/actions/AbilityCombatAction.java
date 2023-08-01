package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.states.CombatEvent;

import java.util.ArrayList;
import java.util.List;

public class AbilityCombatAction extends CombatAction {
    private final GameCharacter performer;
    private final Combatant target;

    public AbilityCombatAction(GameCharacter performer, Combatant target) {
        super("Ability");
        this.performer = performer;
        this.target = target;
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // Unused
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> list = new ArrayList<>();
        if (performer.getRankForSkill(Skill.Sneak) > 0 && target instanceof Enemy) {
            list.add(new SneakAttackCombatAction());
        }
        return list;
    }
}

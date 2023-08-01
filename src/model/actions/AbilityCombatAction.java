package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.weapons.BladedWeapon;
import model.items.weapons.PolearmWeapon;
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
        if (canDoDefendAbility(performer)) {
            list.add(new DefendCombatAction());
        }
        if (performer.getLevel() >= 3 && model.getParty().getBackRow().contains(performer)) {
            list.add(new RestCombatAction());
        }
        if (performer.getRankForSkill(Skill.Leadership) >= InspireCombatAction.LEADERSHIP_RANKS_REQUIREMENT) {
            list.add(new InspireCombatAction());
        }
        if (canDoRiposteAbility(performer)) {
            list.add(new RiposteCombatAction());
        }
        return list;
    }

    private boolean canDoRiposteAbility(GameCharacter performer) {
        return performer.getRankForSkill(Skill.Acrobatics) >= RiposteCombatAction.ACROBATICS_RANKS_REQUIREMENT &&
                (performer.getEquipment().getWeapon() instanceof BladedWeapon ||
                performer.getEquipment().getWeapon() instanceof PolearmWeapon);
    }

    private boolean canDoDefendAbility(GameCharacter performer) {
        Skill[] skills = new Skill[]{Skill.Axes, Skill.Blades, Skill.BluntWeapons, Skill.Polearms};
        for (Skill s : skills) {
            if (performer.getRankForSkill(s) >= DefendCombatAction.DEFEND_SKILL_RANKS) {
                return true;
            }
        }
        return false;
    }
}

package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.weapons.AxeWeapon;
import model.items.weapons.BladedWeapon;
import model.items.weapons.BluntWeapon;
import model.items.weapons.PolearmWeapon;
import model.states.CombatEvent;

import java.util.ArrayList;
import java.util.List;

public class AbilityCombatAction extends CombatAction {
    private final GameCharacter performer;
    private final Combatant target;

    public AbilityCombatAction(GameCharacter performer, Combatant target) {
        super("Ability", false);
        this.performer = performer;
        this.target = target;
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // Unused
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> list = new ArrayList<>();
        if (model.getParty().getFrontRow().contains(performer)) {
            if (performer.getRankForSkill(Skill.Sneak) > 0 && target instanceof Enemy && target.canBeAttackedBy(performer)) {
                list.add(new SneakAttackCombatAction());
            }
            if (RiposteCombatAction.canDoRiposteAbility(performer) && target.canBeAttackedBy(performer)) {
                list.add(new RiposteCombatAction());
            }
            if (HeavyBlowCombatAction.canDoHeavyBlowAbility(performer) && target.canBeAttackedBy(performer)) {
                list.add(new HeavyBlowCombatAction());
            }
        }

        if (DefendCombatAction.canDoDefendAbility(performer)) {
            list.add(new DefendCombatAction());
        }
        if (RestCombatAction.canDoRestAbility(model, performer)) {
            list.add(new RestCombatAction());
        }
        if (InspireCombatAction.canDoInspireAbility(performer)) {
            list.add(new InspireCombatAction());
        }
        if (SniperShotCombatAction.canDoSniperShotAbility(performer)) {
            list.add(new SniperShotCombatAction());
        }
        if (FairyHealCombatAction.canDoAbility(performer)) {
            list.add(new FairyHealCombatAction());
        }
        if (RegenerationCombatAction.canDoAbility(performer)) {
            list.add(new RegenerationCombatAction());
        }
        if (InvisibilityCombatAction.canDoAbility(performer, target)) {
            list.add(new InvisibilityCombatAction());
        }
        if (MagicMissileCombatAction.canDoAbility(performer, target)) {
            list.add(new MagicMissileCombatAction());
        }
        // TODO: Curse combat ability (Black Magic)
        return list;
    }

}

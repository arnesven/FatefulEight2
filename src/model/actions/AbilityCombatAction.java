package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.combat.conditions.BatFormVampireAbility;
import model.combat.conditions.CelerityVampireAbility;
import model.combat.conditions.ClawsVampireAbility;
import model.combat.conditions.MesmerizeVampireAbility;
import model.enemies.Enemy;
import model.items.weapons.*;
import model.states.CombatEvent;
import view.help.HelpDialog;

import java.util.ArrayList;
import java.util.List;

public class AbilityCombatAction extends CombatAction {
    private final GameCharacter performer;
    private final Combatant target;

    public AbilityCombatAction(GameCharacter performer, Combatant target) {
        super("Ability", false, false);
        this.performer = performer;
        this.target = target;
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return null; // unused
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // Unused
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> list = new ArrayList<>();
        List<SpecialAbilityCombatAction> abilities = getAllCombatAbilities(performer);
        for (SpecialAbilityCombatAction ability : abilities) {
            if (ability.canPerformAbility(model, performer, target)) {
                list.add(ability);
            }
        }
        return list;
    }

    public static List<SpecialAbilityCombatAction> getAllCombatAbilities(GameCharacter performer) {
        List<SpecialAbilityCombatAction> list = new ArrayList<>();
        list.add(new SneakAttackCombatAction());
        list.add(new RiposteCombatAction());
        list.add(new HeavyBlowCombatAction());
        list.add(new DefendCombatAction());
        list.add(new RestCombatAction());
        list.add(new InspireCombatAction());
        list.add(new SniperShotCombatAction());
        list.add(new FairyHealCombatAction());
        list.add(new RegenerationCombatAction());
        list.add(new InvisibilityCombatAction());
        list.add(new MagicMissileCombatAction());
        list.add(new CurseCombatAction());
        list.add(new BalladCombatAction());
        if (StaffOfDeimosItem.canDoAbility(performer)) {
            list.add(StaffOfDeimosItem.makeCombatAbility(performer));
        }
        list.add(BatFormVampireAbility.makeCombatAbility());
        list.add(MesmerizeVampireAbility.makeCombatAbility());
        list.add(new MultiShotCombatAction());
        list.add(new CleaveAbility());
        // TODO: Grand Slam (Requires Blunt Weapons - 6)
        // TODO: Feint/Parry (Requires Blades - 6)
        // TODO: Multi-Shot (Requires Bows - 6)
        // TODO: Impale (Requires Polearms - 6)
        return list;
    }

    public static List<PassiveCombatAction> getPassiveCombatActions(GameCharacter gc) {
        List<PassiveCombatAction> list = new ArrayList<>();
        if (QuickCastPassiveCombatAction.canDoAbility(gc)) {
            list.add(QuickCastPassiveCombatAction.getInstance());
        }
        if (CelerityVampireAbility.canDoAbility(gc)) {
            list.add(CelerityVampireAbility.getPassiveCombatAbility());
        }
        if (ClawsVampireAbility.canDoAbility(gc)) {
            list.add(ClawsVampireAbility.getPassiveCombatAbility());
        }
        return list;
    }
}

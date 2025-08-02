package model.combat.abilities;

import model.Model;
import model.actions.*;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.BatFormVampireAbility;
import model.combat.conditions.CelerityVampireAbility;
import model.combat.conditions.ClawsVampireAbility;
import model.combat.conditions.MesmerizeVampireAbility;
import model.items.weapons.*;
import model.states.CombatEvent;
import util.MyLists;
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
        if (performer != null) {
            list.add(new BalladCombatAction());
        }
        if (performer != null && StaffOfDeimosItem.canDoAbility(performer)) {
            list.add(StaffOfDeimosItem.makeCombatAbility(performer));
        }
        list.add(BatFormVampireAbility.makeCombatAbility());
        list.add(MesmerizeVampireAbility.makeCombatAbility());
        list.add(new MultiShotCombatAction());
        list.add(new CleaveAbility());
        list.add(new GrandSlamAbility());
        list.add(new ImpaleAbility());
        list.add(new FeintAbility());
        return list;
    }

    public static List<PassiveCombatAction> getAllPassiveCombatActions() {
        return List.of(QuickCastPassiveCombatAction.getInstance(),
                CelerityVampireAbility.getPassiveCombatAbility(),
                ClawsVampireAbility.getPassiveCombatAbility(),
                ParryAbility.getPassiveCombatAbility(),
                CombatProwessAbility.getPassiveCombatAbility()
                // list.add(new CombatProwess()); // Multiple attacks carries over to other enemies
        );
    }

    public static List<PassiveCombatAction> getPassiveCombatActions(GameCharacter gc) {
        return MyLists.filter(getAllPassiveCombatActions(), pa -> pa.canDoAbility(gc));
    }
}

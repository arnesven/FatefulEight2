package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
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
        super("Ability", false);
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
        if (model.getParty().getFrontRow().contains(performer)) {
            if (performer.getUnmodifiedRankForSkill(Skill.Sneak) > 0 && target instanceof Enemy && target.canBeAttackedBy(performer)) {
                list.add(new SneakAttackCombatAction());
            }
            if (RiposteCombatAction.canDoRiposteAbility(performer)) {
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
        if (FairyHealCombatAction.canDoAbility(performer, target)) {
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
        if (CurseCombatAction.canDoAbility(performer, target)) {
            list.add(new CurseCombatAction());
        }
        if (Lute.canDoAbility(performer, target)) {
            list.add(new BalladCombatAction());
        }
        if (StaffOfDeimosItem.canDoAbility(performer, target)) {
            list.add(StaffOfDeimosItem.makeCombatAbility(performer, target));
        }
        if (BatFormVampireAbility.canDoAbility(performer)) {
            list.add(BatFormVampireAbility.makeCombatAbility());
        }
        if (MesmerizeVampireAbility.canDoAbility(performer, target)) {
            list.add(MesmerizeVampireAbility.makeCombatAbility());
        }
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

package model.combat.abilities;

import model.Model;
import model.actions.EquipItemCombatAction;
import model.actions.ItemCombatAction;
import model.actions.SpellCombatAction;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.combat.conditions.FatigueCondition;
import model.items.UsableItem;
import model.items.spells.AuxiliarySpell;
import model.items.spells.CombatSpell;
import model.items.spells.Spell;
import model.states.CombatEvent;
import view.help.HelpDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CombatAction {
    public static final int FATIGUE_START_ROUND = 3;
    private final String name;
    private final boolean fatigue;
    private final boolean isMeleeAttack;

    public CombatAction(String name, boolean doesFatigue, boolean isMeleeAttack) {
        this.name = name;
        this.fatigue = doesFatigue;
        this.isMeleeAttack = isMeleeAttack;
    }

    public void executeCombatAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        doAction(model, combat, performer, target);
        if (isMeleeAttack) {
            combat.checkFlameWallDamage(model, performer);
        }
        checkFatigue(model, combat, performer);
    }

    private void checkFatigue(Model model, CombatEvent combat, GameCharacter performer) {
        if (fatigue && performer.getEquipment().anyHeavy()) {
            if (performer.hasCondition(FatigueCondition.class)) {
                if (performer.getSP() > 0) {
                    performer.addToSP(-1);
                    combat.println(performer.getFirstName() + " exhausts 1 Stamina Point from fatigue.");
                } else {
                    performer.addToHP(-1);
                    combat.println(performer.getFirstName() + " loses 1 Health Point from fatigue.");
                    if (performer.isDead()) {
                        combat.printAlert(performer.getName() + " has perished from the effects of the fatigue!");
                    }
                }
            } else if (combat.getCurrentRound() >= FATIGUE_START_ROUND) {
                SkillCheckResult result = performer.testSkillHidden(Skill.Endurance, performer.getAP(), 0);
                if (!result.isSuccessful()) {
                    performer.addCondition(new FatigueCondition());
                    if (performer.hasCondition(FatigueCondition.class)) {
                        model.getTutorial().fatigue(model);
                        combat.println(performer.getFirstName() + " has been fatigue due to wearing heavy armor (Endurance " +
                                result.asString() + ")");
                    }
                }
            }
        }
    }

    public abstract HelpDialog getHelpChapter(Model model);

    protected abstract void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target);

    public String getName() {
        return name;
    }

    public boolean hasInnerMenu() {
        return false;
    }

    public List<CombatAction> getInnerActions(Model model) {
        return new ArrayList<>();
    }

    public boolean takeAnotherAction() {
        return false;
    }

    public static List<CombatAction> getCombatActions(Model model, GameCharacter character, Combatant target, CombatEvent combatEvent) {
        List<CombatAction> result = new ArrayList<>();
        addAttack(result, combatEvent, character, target);
        if (model.getParty().getPartyMembers().contains(character)) {
            addAbility(result, combatEvent, character, model, target);
            addSpell(result, model, target);
            addItem(result, combatEvent, model, target);
            addEquip(result, combatEvent, character, model, target);
        }
        result.add(new AutomaticCombatAction());
        addFlee(result, combatEvent, character);
        result.add(new PassCombatAction());
        addDelay(result, combatEvent, character);

        for (Condition cond : character.getConditions()) {
            cond.manipulateCombatActions(result);
        }
        return result;
    }

    private static void addAttack(List<CombatAction> result, CombatEvent combatEvent, GameCharacter character, Combatant target) {
        if (character.canAttackInCombat() && target.canBeAttackedBy(character) && !combatEvent.isInQuickCast()) {
            result.add(new AttackCombatAction(!character.getEquipment().getWeapon().isRangedAttack()));
        }
    }

    private static void addAbility(List<CombatAction> result, CombatEvent combatEvent, GameCharacter character, Model model, Combatant target) {
        AbilityCombatAction abilities = new AbilityCombatAction(character, target);
        if (!abilities.getInnerActions(model).isEmpty() && !combatEvent.isInQuickCast()) {
            result.add(abilities);
        }
    }

    private static void addSpell(List<CombatAction> result, Model model, Combatant target) {
        List<CombatSpell> combatSpells = getCombatSpells(model.getParty().getSpells());
        if (!combatSpells.isEmpty()) {
            result.add(new SpellCombatAction(combatSpells, target));
        }
    }

    private static void addItem(List<CombatAction> result, CombatEvent combatEvent, Model model, Combatant target) {
        Set<UsableItem> usableItems = new HashSet<>();
        usableItems.addAll(model.getParty().getInventory().getPotions());
        usableItems.addAll(model.getParty().getInventory().getCombatScrolls());
        if (!usableItems.isEmpty() && !combatEvent.isInQuickCast()) {
            result.add(new ItemCombatAction(usableItems, target));
        }
    }

    private static void addEquip(List<CombatAction> result, CombatEvent combatEvent, GameCharacter character, Model model, Combatant target) {
        if (character == target && !combatEvent.isInQuickCast()) {
            EquipItemCombatAction eqAction = new EquipItemCombatAction(model);
            if (eqAction.isValid()) {
                result.add(eqAction);
            }
        }
    }

    private static void addFlee(List<CombatAction> result, CombatEvent combatEvent, GameCharacter character) {
        if (character.isLeader() && !combatEvent.isInQuickCast()) {
            result.add(new FleeCombatAction());
        }
    }

    private static void addDelay(List<CombatAction> result, CombatEvent combatEvent, GameCharacter character) {
        if (combatEvent.canDelay(character)  && !combatEvent.isInQuickCast()) {
            result.add(new DelayCombatAction());
        }
    }

    private static List<CombatSpell> getCombatSpells(List<Spell> spells) {
        List<CombatSpell> result = new ArrayList<>();
        for (Spell sp : spells) {
            if (sp instanceof CombatSpell) {
                result.add((CombatSpell) sp);
            }  else if (sp instanceof AuxiliarySpell && ((AuxiliarySpell)sp).canBeCastInCombat()) {
                result.add(((AuxiliarySpell)sp).getCombatSpell());
            }
        }
        return result;
    }

}

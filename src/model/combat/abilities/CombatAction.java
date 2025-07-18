package model.combat.abilities;

import model.GameStatistics;
import model.Model;
import model.actions.BasicCombatAction;
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
import sprites.CombatSpeechBubble;
import util.MyLists;
import util.MyRandom;
import view.help.HelpDialog;
import view.subviews.CombatSubView;

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
        if (character.canAttackInCombat() && target.canBeAttackedBy(character) && !combatEvent.isInQuickCast()) {
            result.add(new BasicCombatAction("Attack", true, !character.getEquipment().getWeapon().isRangedAttack()) {
                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    performer.performAttack(model, combat, target);
                }
            });
        }
        if (character.isLeader() && combatEvent.fleeingEnabled() && !combatEvent.isInQuickCast()) {
            result.add(new BasicCombatAction("Flee", false, false) {
                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    performFleeFromBattle(model, combat, performer);
                }
            });
        }

        if (character == target && !combatEvent.isInQuickCast()) {
            EquipItemCombatAction eqAction = new EquipItemCombatAction(model);
            if (eqAction.isValid()) {
                result.add(eqAction);
            }
        }

        if (model.getParty().getPartyMembers().contains(character)) {
            Set<UsableItem> usableItems = new HashSet<>();
            usableItems.addAll(model.getParty().getInventory().getPotions());
            usableItems.addAll(model.getParty().getInventory().getCombatScrolls());
            if (usableItems.size() > 0  && !combatEvent.isInQuickCast()) {
                result.add(new ItemCombatAction(usableItems, target));
            }

            List<CombatSpell> combatSpells = getCombatSpells(model.getParty().getSpells());
            if (!combatSpells.isEmpty()) {
                result.add(new SpellCombatAction(combatSpells, target));
            }

            AbilityCombatAction abilities = new AbilityCombatAction(character, target);
            if (abilities.getInnerActions(model).size() > 0  && !combatEvent.isInQuickCast()) {
                result.add(abilities);
            }
        }

        if (combatEvent.canDelay(character)  && !combatEvent.isInQuickCast()) {
            result.add(new BasicCombatAction("Delay", false, false) {
                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    combat.delayCombatant(performer);
                }
            });
        }

        result.add(new BasicCombatAction("Pass", false, false) {
            @Override
            protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                combat.println("");
            }
        });
        for (Condition cond : character.getConditions()) {
            cond.manipulateCombatActions(result);
        }
        return result;
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

    private static void performFleeFromBattle(Model model, CombatEvent combatEvent, GameCharacter character) {
        boolean fleeSuccess = false;
        if (model.getParty().size() > 1) {
            SkillCheckResult result = character.testSkill(model, Skill.Leadership,
                    3 + model.getParty().size() - model.getParty().getBench().size());
            combatEvent.println("Trying to escape from combat (Leadership " + result.asString() + ").");
            if (result.isSuccessful()) {
                combatEvent.leaderSay(MyRandom.sample(List.of("Retreat!", "Fall back!", "Let's get out of here!",
                        "They're too strong, let's go!", "There's no point, let's get out of here.")));
                combatEvent.addSpecialEffect(model.getParty().getLeader(), new CombatSpeechBubble());
                fleeSuccess = true;
            }
        } else {
            int d10 = MyRandom.rollD10();
            combatEvent.print("Trying to escape from combat (D10 roll=" + d10 + ")");
            if (d10 >= 5) {
                combatEvent.println(" >=5, SUCCESS.");
                combatEvent.leaderSay("I'm out of here.");
                combatEvent.addSpecialEffect(model.getParty().getLeader(), new CombatSpeechBubble());
                fleeSuccess = true;
            } else {
                combatEvent.println(" <5 FAIL. Can't get away!");
            }
        }
        if (fleeSuccess) {
            GameStatistics.incrementCombatsFled();
            if (model.getSubView() instanceof CombatSubView) {
                ((CombatSubView)model.getSubView()).enableFleeingAnimation();
            }
            combatEvent.setPartyFled(true);
            for (GameCharacter gc : new ArrayList<>(combatEvent.getAllies())) {
                combatEvent.removeAlly(gc);
            }
        }
    }
}

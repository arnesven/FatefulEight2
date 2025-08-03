package model.combat.abilities;

import model.Model;
import model.actions.BasicCombatAction;
import model.actions.PassiveCombatAction;
import model.actions.RegenerationCondition;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.InvisibilityCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutomaticCombatAction extends BasicCombatAction {
    public static final int STOP_IF_HP_FALLS_BELOW = 3;

    public AutomaticCombatAction() {
        super("Auto", false, false);
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.print("Are you sure you want to enable automatic combat? (Y/N) ");
        model.getTutorial().automaticCombat(model);
        if (!combat.yesNoInput()) {
            return;
        }
        combat.setAutoCombatEnabled(true);
    }

    public static MyPair<CombatAction, Combatant> takeAutoCombatAction(Model model, CombatEvent combat, GameCharacter character, boolean isQuickCast) {
        if (isQuickCast) {
            return new MyPair<>(new PassCombatAction(), character);
        }

        MyPair<CombatAction, Combatant> act;
        act = tryAttack(model, combat, character);
        if (act != null) {
            return act;
        }

        act = tryProtectiveAbilities(model, combat, character);
        if (act != null) {
            return act;
        }

        act = tryMagicMissileOrCurse(model, combat, character);
        if (act != null) {
            return act;
        }

        act = tryInspire(model, combat, character);
        if (act != null) {
            return act;
        }

        act = tryRestOrDefend(model, combat, character);
        if (act != null) {
            return act;
        }

        return new MyPair<>(new PassCombatAction(), character);
    }

    private static MyPair<CombatAction, Combatant> tryAttack(Model model, CombatEvent combat, GameCharacter character) {
        List<Enemy> enemies = getAttackableEnemies(combat, character);
        if (!enemies.isEmpty()) {
            enemies.sort(Comparator.comparingInt(Combatant::getHP));
            Combatant target = enemies.getFirst();
            List<CombatAction> acts = CombatAction.getCombatActions(model, character, target, combat);
            CombatAction atk = MyLists.find(acts, a -> a instanceof AttackCombatAction);
            if (atk != null) {
                return new MyPair<>(atk, target);
            }
        }
        return null;
    }

    private static MyPair<CombatAction, Combatant> tryProtectiveAbilities(Model model, CombatEvent combat, GameCharacter character) {
        List<GameCharacter> candidates = getWoundedPartyMembers(combat);
        if (!candidates.isEmpty()) {
            candidates.sort(Comparator.comparingInt(Combatant::getHP));
            for (GameCharacter target : candidates) {
                List<CombatAction> abis = getAbilityCombatActions(model, character, target, combat);
                if (!abis.isEmpty()) {
                    CombatAction fairyHealAct = MyLists.find(abis, ab -> ab instanceof FairyHealCombatAction);
                    if (fairyHealAct != null) {
                        return new MyPair<>(fairyHealAct, target);
                    }
                    if (!target.hasCondition(InvisibilityCondition.class)) {
                        CombatAction invisAct = MyLists.find(abis, ab -> ab instanceof InvisibilityCombatAction);
                        if (invisAct != null) {
                            return new MyPair<>(invisAct, target);
                        }
                    }
                    if (!target.hasCondition(RegenerationCondition.class)) {
                        CombatAction regenAct = MyLists.find(abis, ab -> ab instanceof RegenerationCombatAction);
                        if (regenAct != null) {
                            return new MyPair<>(regenAct, target);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static MyPair<CombatAction, Combatant> tryMagicMissileOrCurse(Model model, CombatEvent combat, GameCharacter character) {
        List<Enemy> enemies = getAttackableEnemies(combat, character);
        if (!enemies.isEmpty()) {
            boolean preferCurse = character.getRankForSkill(Skill.MagicBlack) > character.getRankForSkill(Skill.MagicRed);
            Combatant target = MyRandom.sample(enemies);
            List<CombatAction> abis = getAbilityCombatActions(model, character, target, combat);
            if (!abis.isEmpty()) {
                if (preferCurse) {
                    CombatAction curseAct = MyLists.find(abis, ab -> ab instanceof CurseCombatAction);
                    if (curseAct != null) {
                        return new MyPair<>(curseAct, target);
                    }
                    CombatAction missileAct = MyLists.find(abis, ab -> ab instanceof MagicMissileCombatAction);
                    if (missileAct != null) {
                        return new MyPair<>(missileAct, target);
                    }
                } else {
                    CombatAction missileAct = MyLists.find(abis, ab -> ab instanceof MagicMissileCombatAction);
                    if (missileAct != null) {
                        return new MyPair<>(missileAct, target);
                    }
                    CombatAction curseAct = MyLists.find(abis, ab -> ab instanceof CurseCombatAction);
                    if (curseAct != null) {
                        return new MyPair<>(curseAct, target);
                    }
                }
            }
        }
        return null;
    }

    private static MyPair<CombatAction, Combatant> tryInspire(Model model, CombatEvent combat, GameCharacter character) {
        List<Combatant> partyMembers = new ArrayList<>(combat.getAllCombatants());
        partyMembers.removeAll(combat.getEnemies());
        partyMembers.removeAll(combat.getAllies());
        if (MyLists.all(partyMembers, pm -> pm.hasCondition(InspiredCondition.class))) {
            return null;
        }
        List<CombatAction> abis = getAbilityCombatActions(model, character, character, combat);
        if (!abis.isEmpty()) {
            CombatAction inspireAct = MyLists.find(abis, ab -> ab instanceof InspireCombatAction);
            if (inspireAct != null) {
                return new MyPair<>(inspireAct, character);
            }
        }
        return null;
    }

    private static MyPair<CombatAction, Combatant> tryRestOrDefend(Model model, CombatEvent combat, GameCharacter character) {
        List<CombatAction> abis = getAbilityCombatActions(model, character, character, combat);
        if (!abis.isEmpty()) {
            if (!RestCombatAction.isFullyRested(character)) {
                CombatAction restAct = MyLists.find(abis, ab -> ab instanceof RestCombatAction);
                if (restAct != null) {
                    return new MyPair<>(restAct, character);
                }
            }
            CombatAction defendAct = MyLists.find(abis, ab -> ab instanceof DefendCombatAction);
            if (defendAct != null) {
                return new MyPair<>(defendAct, character);
            }
        }
        return null;
    }

    private static List<Enemy> getAttackableEnemies(CombatEvent combat, GameCharacter character) {
        return MyLists.filter(combat.getEnemies(), e -> e.canBeAttackedBy(character));
    }

    private static List<GameCharacter> getWoundedPartyMembers(CombatEvent combat) {
        List<Combatant> targets = new ArrayList<>(combat.getAllCombatants());
        targets.removeAll(combat.getEnemies());
        targets.removeAll(combat.getAllies());
        return MyLists.transform(MyLists.filter(targets,
                        comb -> comb.getHP() < comb.getMaxHP()),
                comb -> (GameCharacter)comb);
    }

    private static List<CombatAction> getAbilityCombatActions(Model model, GameCharacter character, Combatant target, CombatEvent combat) {
        List<CombatAction> acts = CombatAction.getCombatActions(model, character, target, combat);
        CombatAction topAct = MyLists.find(acts, act -> act instanceof AbilityCombatAction);
        if (topAct == null) {
            return List.of();
        }
        return ((AbilityCombatAction)topAct).getInnerActions(model);
    }

}

package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.CombatEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CombatAction {
    public static final int FATIGUE_START_ROUND = 3;
    private final String name;
    private final boolean fatigue;

    public CombatAction(String name, boolean doesFatigue) {
        this.name = name;
        this.fatigue = doesFatigue;
    }

    public void executeCombatAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        doAction(model, combat, performer, target);
        checkFatigue(model, combat, performer);
    }

    private void checkFatigue(Model model, CombatEvent combat, GameCharacter performer) {
        if (fatigue && wearingHeavyArmor(performer)) {
            if (performer.hasCondition(FatigueCondition.class)) {
                if (performer.getSP() > 0) {
                    performer.addToSP(-1);
                    combat.println(performer.getFirstName() + " exhausts 1 Stamina Point from fatigue.");
                } else {
                    performer.addToHP(-1);
                    combat.println(performer.getFirstName() + " loses 1 Health Point from fatigue.");
                    if (performer.isDead()) {
                        combat.println("!" + performer.getName() + " has perished from the effects of the fatigue!");
                    }
                }
            } else if (combat.getCurrentRound() >= FATIGUE_START_ROUND) {
                SkillCheckResult result = performer.testSkill(Skill.Endurance, performer.getAP());
                if (!result.isSuccessful()) {
                    model.getTutorial().fatigue(model);
                    combat.println(performer.getFirstName() + " has been fatigue due to wearing heavy armor (Endurance " +
                            result.asString() + ")");
                    performer.addCondition(new FatigueCondition());
                }
            }
        }
    }

    private boolean wearingHeavyArmor(GameCharacter performer) {
        return performer.getEquipment().getClothing().isHeavy() ||
                (performer.getEquipment().getAccessory() != null &&
                        performer.getEquipment().getAccessory().isHeavy());
    }

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
}

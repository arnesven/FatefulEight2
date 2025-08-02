package model.combat.abilities;

import model.Model;
import model.characters.GameCharacter;
import model.classes.normal.BardClass;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.items.weapons.Lute;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialInspire;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.List;

public class InspireCombatAction extends SpecialAbilityCombatAction implements SkillAbilityCombatAction {
    public static final int LEADERSHIP_RANKS_REQUIREMENT = 4;

    public InspireCombatAction() {
        super("Inspire", false, false);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialInspire(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().inspire(model);
        SkillCheckResult result = doSkillCheck(model, combat, performer);
        if (result.isSuccessful()) {
            String word = "a";
            int bonus = 1;
            if (result.getModifiedRoll() >= 14) {
                word = "an extremely";
                bonus = 3;
            } else if (result.getModifiedRoll() >= 11) {
                word = "a very";
                bonus = 2;
            }
            String typeOfInspiration = "speech";
            if (performer.getCharClass() instanceof BardClass ||
                    performer.getEquipment().getWeapon().isOfType(Lute.class)) {
                typeOfInspiration = "song";
            } else if (MyRandom.flipCoin()) {
                typeOfInspiration = "chant";
            }
            combat.println(performer.getFirstName() + " inspires the party with " + word + " compelling " + typeOfInspiration + ".");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc != performer && !model.getParty().getBench().contains(gc) && !gc.hasCondition(InspiredCondition.class)) {
                    gc.addCondition(new InspiredCondition(bonus));
                }
            }
        } else {
            combat.println("However, nobody seems particularly inspired.");
        }
    }

    protected SkillCheckResult doSkillCheck(Model model, CombatEvent combat, GameCharacter performer) {
        int isLeader = model.getParty().getLeader() == performer? 1 : 0;
        combat.println(performer.getFirstName() + " attempts to inspire the party.");
        return model.getParty().doSkillCheckWithReRoll(model, combat, performer,
                Skill.Leadership, 8, 0, isLeader);
    }

    public Condition getCondition() {
        return new InspiredCondition(1);
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return true;
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Leadership);
    }

    @Override
    public int getRequiredRanks() {
        return LEADERSHIP_RANKS_REQUIREMENT;
    }

}

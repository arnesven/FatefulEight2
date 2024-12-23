package model.combat.abilities;

import model.Model;
import model.actions.StaminaCombatAbility;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.enemies.Enemy;
import model.items.weapons.BladedWeapon;
import model.items.weapons.PolearmWeapon;
import model.states.CombatEvent;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.help.HelpDialog;
import view.help.TutorialRiposte;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.List;

public class RiposteCombatAction extends StaminaCombatAbility implements SkillAbilityCombatAction {
    public static final int ACROBATICS_RANKS_REQUIREMENT = 3;

    public RiposteCombatAction() {
        super("Riposte", false);
    }

    public static void doRiposte(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Enemy enemy) {
        if (gameCharacter.hasCondition(RiposteStanceCondition.class)) {
            combatEvent.println(gameCharacter.getFirstName() + " counter-attacks!");
            gameCharacter.doOneAttack(model, combatEvent, enemy, false, 0, 10);
            gameCharacter.removeCondition(RiposteStanceCondition.class);
        }
    }

    public static int getEvadeBonus(GameCharacter gameCharacter) {
        return gameCharacter.hasCondition(RiposteStanceCondition.class) ? 2 : 0;
    }

    private static final Sprite SPRITE = CharSprite.make((char)(0xD0), MyColors.LIGHT_BLUE, MyColors.BLACK, MyColors.CYAN);

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().riposte(model);
        combat.println(performer.getFirstName() + " gets ready to perform a riposte.");
        performer.addCondition(new RiposteCombatAction.RiposteStanceCondition());
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialRiposte(model.getView());
    }

    public Condition getCondition() {
        return new RiposteStanceCondition();
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return model.getParty().getFrontRow().contains(performer) &&
                (performer.getEquipment().getWeapon().isOfType(BladedWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(PolearmWeapon.class));
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Acrobatics);
    }

    @Override
    public int getRequiredRanks() {
        return ACROBATICS_RANKS_REQUIREMENT;
    }

    private static class RiposteStanceCondition extends Condition {
        public RiposteStanceCondition() {
            super("Riposte", "RIP");
            setDuration(2);
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public Sprite getSymbol() {
            return SPRITE;
        }

        @Override
        public boolean removeAtEndOfCombat() {
            return true;
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this, "A condition indicating that this character is " +
                    "currently in a Riposte Stance and may perform counter-attacks when attacked.");
        }
    }
}

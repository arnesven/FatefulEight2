package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.Condition;
import model.enemies.Enemy;
import model.items.weapons.BladedWeapon;
import model.items.weapons.PolearmWeapon;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class RiposteCombatAction extends StaminaCombatAbility {
    public static final int ACROBATICS_RANKS_REQUIREMENT = 3;

    public RiposteCombatAction() {
        super("Riposte");
    }

    public static void doRiposte(CombatEvent combatEvent, GameCharacter gameCharacter, Enemy enemy) {
        combatEvent.println(gameCharacter.getFirstName() + " counter-attacks!");
        gameCharacter.doOneAttack(combatEvent, enemy, false, 0, 10);
        gameCharacter.removeCondition(RiposteStanceCondition.class);
    }


    public static int getEvadeBonus(GameCharacter gameCharacter) {
        return gameCharacter.hasCondition(RiposteStanceCondition.class) ? 2 : 0;
    }

    private static final Sprite SPRITE = CharSprite.make((char)(0xD0), MyColors.LIGHT_BLUE, MyColors.BLACK, MyColors.CYAN);

    public static boolean canDoRiposteAbility(GameCharacter performer) {
        return performer.getRankForSkill(Skill.Acrobatics) >= RiposteCombatAction.ACROBATICS_RANKS_REQUIREMENT &&
                (performer.getEquipment().getWeapon() instanceof BladedWeapon ||
                        performer.getEquipment().getWeapon() instanceof PolearmWeapon);
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().riposte(model);
        combat.println(performer.getFirstName() + " gets ready to perform a riposte.");
        performer.addCondition(new RiposteCombatAction.RiposteStanceCondition());
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
    }
}

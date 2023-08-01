package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.combat.Condition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class RiposteCombatAction extends CombatAction {
    public static final int ACROBATICS_RANKS_REQUIREMENT = 3;

    public RiposteCombatAction() {
        super("Riposte");
    }

    public static void doRiposte(CombatEvent combatEvent, GameCharacter gameCharacter, Enemy enemy) {
        combatEvent.println(gameCharacter.getFirstName() + " counter-attacks!");
        gameCharacter.doOneAttack(combatEvent, enemy, false);
        gameCharacter.removeCondition(RiposteStanceCondition.class);
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (performer.getSP() > 0) {
            model.getTutorial().riposte(model);
            combat.println(performer.getFirstName() + " exhausts 1 Stamina Point and gets ready to perform a riposte.");
            performer.addToSP(-1);
            performer.addCondition(new RiposteStanceCondition());
        } else {
            combat.println(performer.getFirstName() + " is too exhausted to perform a riposte.");
        }
    }

    public static int getEvadeBonus(GameCharacter gameCharacter) {
        return gameCharacter.hasCondition(RiposteStanceCondition.class) ? 2 : 0;
    }

    private static final Sprite SPRITE = CharSprite.make((char)(0xD0), MyColors.LIGHT_BLUE, MyColors.BLACK, MyColors.CYAN);

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

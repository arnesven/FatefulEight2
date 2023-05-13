package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import model.states.GameState;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class PoisonCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC1), MyColors.GREEN, MyColors.BLACK, MyColors.CYAN);

    public PoisonCondition() {
        super("Poison", "PSN");
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
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        state.println(comb.getName() + " takes damage from the effects of the poison.");
        comb.addToHP(-2);
        if (comb.isDead() && comb instanceof GameCharacter) {
            DailyEventState.characterDies(model, state, (GameCharacter)comb, " succumbed to the evil of the poison and died.");
        }
    }
}

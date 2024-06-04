package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.*;
import model.items.Item;
import model.states.GameState;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class SouthernCrossSpell extends ImmediateSpell {
    private static final Sprite SPRITE = new ItemSprite(6, 8, MyColors.BROWN, MyColors.WHITE, MyColors.DARK_GRAY);;

    public SouthernCrossSpell() {
        super("Southern Cross", 26, MyColors.WHITE, 11, 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SouthernCrossSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != caster) {
                int hpBefore = gc.getHP();
                gc.addToHP(8 + getMasteryLevel(caster) * 2);
                int totalRecovered = gc.getHP() - hpBefore;
                state.println(gc.getName() + " recovers " + totalRecovered + " HP!");
                model.getParty().partyMemberSay(model, gc,
                        List.of("Thank you so much!3", "Much obliged!3",
                                "I really needed that!3", "Aah, I feel great!3"));
                gc.removeCondition(PoisonCondition.class);
                gc.removeCondition(BleedingCondition.class);
                gc.removeCondition(BurningCondition.class);
                gc.removeCondition(ParalysisCondition.class);
                gc.removeCondition(IntoxicatedCondition.class);
            }
        }
    }

    @Override
    protected boolean masteriesEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Restores 8 HP of each party member (excluding the caster) and removes negative conditions.";
    }
}

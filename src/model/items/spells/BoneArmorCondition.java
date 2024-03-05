package model.items.spells;

import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.Condition;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class BoneArmorCondition extends Condition {
    private final int ap;
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD1), MyColors.BLACK, MyColors.BEIGE, MyColors.CYAN);


    public BoneArmorCondition(int ap) {
        super("Bone Armor", "BNA");
        this.ap = ap;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }


    @Override
    public int getArmorBonus() {
        return ap;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this combatant has summed bone armor to protect them.");
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public void wasRemoved(Combatant combatant) {
        if (combatant instanceof GameCharacter) {
            ((GameCharacter) combatant).removeSpecificClothing();
        }
    }
}

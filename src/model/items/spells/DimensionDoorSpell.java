package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.states.GameState;
import model.states.dailyaction.HeadquartersDailyActionState;
import view.MyColors;
import view.sprites.BlueSpellSprite;
import view.sprites.Sprite;

public class DimensionDoorSpell extends ImmediateSpell {
    private static final Sprite SPRITE = new BlueSpellSprite(8, false);

    public DimensionDoorSpell() {
        super("Dimenson Door", 38, MyColors.BLUE, 8, 4);
    }

    public String castFromMenu(Model model, GameCharacter gc) {
        if (model.getParty().getHeadquarters() == null) {
            return  "You cannot cast " + getName() + " right now. (You do not have a Headquarters.)";
        }
        return super.castFromMenu(model, gc);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DimensionDoorSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        state.println("A magic door appears in front of the party. " +
                model.getParty().getLeader().getFirstName() + " opens it and steps through.");
        new HeadquartersDailyActionState(model, state, true).run(model);
    }

    @Override
    public String getDescription() {
        return "Opens a portal to your headquarters, if you have one.";
    }
}

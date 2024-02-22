package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.subviews.MapSubView;
import view.subviews.SubView;

import java.awt.*;

public class TeleportSpell extends ImmediateSpell {

    private static final Sprite SPRITE = new ItemSprite(2, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);
    private static final String X_KEY = "Teleport-X";
    private static final String Y_KEY = "Teleport-Y";

    public TeleportSpell() {
        super("Teleport", 46, MyColors.BLUE, 1, 0); // TODO: Difficulty 11, HP cost 4
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TeleportSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.println(caster.getName() + " is preparing to cast teleport.");
        Integer markedX = model.getSettings().getMiscCounters().get(X_KEY);
        Integer markedY = model.getSettings().getMiscCounters().get(Y_KEY);
        if (markedX != null && markedY != null) {
            Point position = new Point(markedX, markedY);
            state.print("You previously marked " + model.getHexInfo(position));
        } else {
            state.print("You have no previously marked location");
        }
        state.print(", do you want to continue casting teleport? (Y/N) ");
        return state.yesNoInput();
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        Integer markedX = model.getSettings().getMiscCounters().get(X_KEY);
        Integer markedY = model.getSettings().getMiscCounters().get(Y_KEY);
        if (markedX != null && markedY != null) {
            Point position = new Point(markedX, markedY);
            state.print("Do you want to return to your previously marked location (Y) or " +
                    " do you want to mark your current location (N)? ");
            if (state.yesNoInput()) {
                model.getParty().setPosition(position);
                state.printAlert("Your party teleported to a new location!");
                SubView previousSubView = model.getSubView();
                MapSubView mapSubView = new MapSubView(model);
                CollapsingTransition.transition(model, mapSubView);
                state.println("Press enter to continue.");
                state.waitForReturn();
                CollapsingTransition.transition(model, previousSubView);
                return;
            }
        }
        markLocation(model, state, caster);
    }

    private void markLocation(Model model, GameState state, GameCharacter caster) {
        Point position = model.getParty().getPosition();
        model.getSettings().getMiscCounters().put(X_KEY, position.x);
        model.getSettings().getMiscCounters().put(Y_KEY, position.y);
        state.println("Your current location has been marked so that you may return here later with the teleport spell.");
    }

    @Override
    public String getDescription() {
        return "Either mark current location, or magically transport the party to a previous mark.";
    }
}

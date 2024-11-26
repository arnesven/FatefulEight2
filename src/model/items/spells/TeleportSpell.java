package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.Prevalence;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.subviews.MapSubView;
import view.subviews.SubView;
import view.subviews.TeleportingTransition;

import java.awt.*;

public class TeleportSpell extends ImmediateSpell {

    private static class TeleportPosition {
        private Point position;
        private boolean inCaves;

        public TeleportPosition(Integer markedX, Integer markedY, Boolean caves) {
            this.position = new Point(markedX, markedY);
            this.inCaves = caves;
        }
    }

    private static final Sprite SPRITE = new ItemSprite(2, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);
    private static final String X_KEY = "Teleport-X";
    private static final String Y_KEY = "Teleport-Y";
    private static final String CAVES_KEY = "Teleport-Caves";

    public TeleportSpell() {
        super("Teleport", 46, MyColors.BLUE, 11, 4);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new TeleportSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.println(caster.getName() + " is preparing to cast teleport.");
        TeleportPosition tpPos = loadMarkedPosition(model);
        if (tpPos != null) {
            state.print("You previously marked " + model.getHexInfo(tpPos.position));
        } else {
            state.print("You have no previously marked location");
        }
        state.print(", do you want to continue casting teleport? (Y/N) ");
        return state.yesNoInput();
    }

    private TeleportPosition loadMarkedPosition(Model model) {
        Integer markedX = model.getSettings().getMiscCounters().get(X_KEY);
        Integer markedY = model.getSettings().getMiscCounters().get(Y_KEY);
        Boolean caves = model.getSettings().getMiscFlags().get(CAVES_KEY);
        if (markedX == null || markedY == null || caves == null) {
            return null;
        }
        return new TeleportPosition(markedX, markedY, caves);
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        TeleportPosition tpPos = loadMarkedPosition(model);
        if (tpPos != null) {
            state.print("Do you want to return to your previously marked location (Y) or" +
                    " do you want to mark your current location (N)? ");
            if (state.yesNoInput()) {
                teleportPartyToPosition(model, state, tpPos.position, tpPos.inCaves);
                return;
            }
        }
        markLocation(model, state, caster);
    }

    public static void teleportPartyToPosition(Model model, GameState state, Point position, boolean inCaves) {
        MapSubView mapSubView = new MapSubView(model);
        CollapsingTransition.transition(model, mapSubView);
        state.println("Preparing to teleport, press enter to continue.");
        state.waitForReturn();
        TeleportingTransition.transition(model, mapSubView, position, inCaves);
        state.println("Press enter to continue.");
        state.waitForReturn();
        CollapsingTransition.transition(model, model.getCurrentHex().getImageSubView());
    }

    private void markLocation(Model model, GameState state, GameCharacter caster) {
        Point position = model.getParty().getPosition();
        model.getSettings().getMiscCounters().put(X_KEY, position.x);
        model.getSettings().getMiscCounters().put(Y_KEY, position.y);
        model.getSettings().getMiscFlags().put(CAVES_KEY, model.isInCaveSystem());
        state.println("Your current location has been marked so that you may return here later with the teleport spell.");
    }

    @Override
    public String getDescription() {
        return "Either mark current location, or magically transport the party to a previous mark.";
    }
}

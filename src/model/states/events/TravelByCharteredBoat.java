package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.map.Direction;
import model.map.SeaHex;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.RunAwayState;
import model.states.TravelBySeaState;
import util.MyLists;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.MapSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TravelByCharteredBoat extends AlternativeTravelEvent {
    private static final String VISITED_FAITH_ISLAND_KEY = "visitedfaithisland";
    private boolean endAtSea = false;

    public TravelByCharteredBoat(Model model) {
        super(model, true);
    }

    @Override
    protected boolean allowCheckForFlee() {
        return false;
    }

    @Override
    protected Sprite getSprite() {
        return TravelBySeaState.SHIP_AVATAR;
    }

    @Override
    protected boolean eventIntro(Model model) {
        return true;
    }

    @Override
    protected void eventOutro(Model model) {
        endAtSea = model.getCurrentHex() instanceof SeaHex;
        if (!endAtSea) {
            if (model.getCurrentHex().getRivers() != Direction.NONE) {
                print("You are currently in a coastal hex. Do you want to continue your journey by sea tomorrow? (Y/N) ");
                endAtSea = yesNoInput();
            }
        }
        if (!endAtSea) {
            println("You say farewell to the captain and the crew and disembark from the boat.");
        }
        System.out.println("End at sea is " + endAtSea);
    }

    @Override
    protected boolean allowPartyEvent() {
        return false;
    }

    @Override
    protected GameState getEveningState(Model model) {
        System.out.println("Getting evening state for travel by chartered boat");
        if (!endAtSea) {
            System.out.println("Did not end at sea, calling super.");
            return super.getEveningState(model);
        }
        DailyEventState event = null;
        if (model.getCurrentHex() instanceof SeaHex) {
            if (checkForPirateEvent(model)) {
                event = new PirateShipEvent(model);
            } else if (checkForStormEvent(model)) {
                event = new StormAtSeaEvent(model);
            } else if (checkForSeaMonsterEvent(model)) {
                event = new SeaMonsterEvent(model);
            } // TODO: Add MermaidEvent
        }
        if (event != null) {
            event.doTheEvent(model);
            if (event.haveFledCombat()) {
                return setAdrift(model);
            }
        }
        return new EveningAtSeaState(model);
    }

    private GameState setAdrift(Model model) {
        println("Your party escapes onto makeshift rafts and are set adrift on the surf.");
        if (WorldBuilder.isInExtendedRegion(model.getParty().getPosition())) {
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-100));
            while (model.getCurrentHex() instanceof SeaHex) {
                new DriftingAtSeaState(model).run(model);
            }
        } else {
            println("You wash ashore on an island.");
            if (model.getSettings().getMiscFlags().get(VISITED_FAITH_ISLAND_KEY) == null) {
                leaderSay("Where are we?");
                if (model.getParty().size() > 1) {
                    GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    partyMemberSay(gc, "Well, we'd better make camp here anyway.");
                }
                model.getSettings().getMiscFlags().put(VISITED_FAITH_ISLAND_KEY, true);
            } else {
                leaderSay("Looks like we're back on Faith Island again.");
            }
            model.getParty().setPosition(WorldBuilder.FAITH_ISLAND_POSITION);
        }
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private boolean checkForPirateEvent(Model model) {
        if (WorldBuilder.isInExtendedRegion(model.getParty().getPosition())) {
            return MyRandom.rollD10() < 5;
        }
        return MyRandom.rollD10() == 1;
    }

    private boolean checkForStormEvent(Model model) {
        int target = model.getSettings().getMiscFlags().get(VISITED_FAITH_ISLAND_KEY) == null ? 2 : 1;
        return MyRandom.rollD10() <= target;
    }

    private boolean checkForSeaMonsterEvent(Model model) {
        return WorldBuilder.isInExtendedRegion(model.getParty().getPosition()) && MyRandom.rollD10() < 4;
    }

    @Override
    protected String getTitleText() {
        return "TRAVEL BY SEA";
    }

    @Override
    protected String getUnderText() {
        return "You have chartered a boat and are traveling by sea.";
    }

    @Override
    protected String getTravelPrompt() {
        return "Please select a sea or coastal hex to travel to.";
    }

    @Override
    protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
        return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 5;
    }

    @Override
    protected boolean isValidDestination(Model model, Point selectedPos) {
        return model.getWorld().getHex(selectedPos) instanceof SeaHex || model.getWorld().getHex(selectedPos).getRivers() != Direction.NONE;
    }

    @Override
    protected boolean dontAllowSeaHexes() {
        return false;
    }

    private static class DriftingAtSeaState extends RunAwayState {
        public DriftingAtSeaState(Model model) {
            super(model);
        }

        @Override
        protected Point selectDirection(Model model, MapSubView mapSubView) {
            List<Point> result = new ArrayList<>();
            for (Point dir : mapSubView.getDirections(model)) {
                if (!movesOutsideMap(model.getParty().getPosition(), dir)) {
                    result.add(dir);
                }
            }
            return MyRandom.sample(result);
        }

        @Override
        protected boolean checkForRiding(Model model) {
            println("Your party is drifting at sea. Each party member suffers 1 damage from the cold waters.");
            print("Press enter to continue.");
            waitForReturn();
            model.getParty().getHorseHandler().someHorsesRunAway(model);
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getHP() > 1) {
                    gc.addToHP(-1);
                }
            }
            return false;
        }
    }
}

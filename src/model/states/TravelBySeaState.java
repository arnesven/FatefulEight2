package model.states;

import model.Model;
import model.map.TownLocation;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CollapsingTransition;
import view.subviews.MapSubView;

import java.awt.*;

public class TravelBySeaState extends GameState {
    private final MyPair<TownLocation, Integer> ship;
    private boolean didTravel = false;
    public static final Sprite SHIP_AVATAR = new Sprite32x32("shipavatar", "world.png", 0x26,
            MyColors.BLACK, MyColors.BEIGE, MyColors.BROWN);

    public TravelBySeaState(Model model) {
        super(model);
        this.ship = rollOnTable(model, (TownLocation)model.getCurrentHex().getLocation());
    }

    @Override
    public GameState run(Model model) {
        if (ship.first == null) {
            println("This ship will not be departing today. " +
                    "If you want to travel with it you will have to come back again tomorrow.");
        } else {
            int cost = ship.second * model.getParty().size();
            println("This ship is departing for the " + ship.first.getName() + " soon." +
                    " The captain will take your party for " + cost + " gold. The voyage takes 2 days.");
            print("Do you want to travel to " + ship.first.getTownName() + "? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-cost);
                travelTo(model, ship.first);
            } else {
                println("Ok. But come back soon if you change your mind. The ship will not wait for you.");
            }
        }
        return model.getCurrentHex().getDailyActionState(model);
    }

    private void travelTo(Model model, TownLocation first) {
        didTravel = true;
        travelBySea(model, ship, this);
    }

    public static void travelBySea(Model model, MyPair<TownLocation, Integer> ship, GameState state) {
        MapSubView mapSubView = new SeaTravelMapSubView(model, ship.first);
        CollapsingTransition.transition(model, mapSubView);
        Point newPosition = model.getWorld().getPositionForHex(ship.first.getHex());

        model.getWorld().dijkstrasBySea(ship.first.getHex());

        Point currentPos = model.getParty().getPosition();
        do {
            Point closest = model.getWorld().findClosestWaterPath(currentPos);
            if (closest.equals(currentPos)) {
                break;
            }
            mapSubView.addMovementAnimation(
                    SHIP_AVATAR,
                    model.getWorld().translateToScreen(currentPos, currentPos, MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                    model.getWorld().translateToScreen(closest, currentPos, MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
            mapSubView.waitForAnimation();
            mapSubView.removeMovementAnimation();
            model.getParty().setPosition(closest);
            currentPos = closest;
        } while (!(currentPos.x == newPosition.x && currentPos.y == newPosition.y));


        model.getCurrentHex().travelFrom(model);
        state.stepToNextDay(model);
        state.println("The party spends the day on the ship traveling to the " + ship.first.getName());
        state.stepToNextDay(model);
        System.out.println("TravelBySeaState: after step to next day.");
        model.getParty().setPosition(newPosition);
        System.out.println("TravelBySeaState: after set Position.");
        model.getCurrentHex().travelTo(model);
        System.out.println("TravelBySeaState: Done with travelTo");
    }

    public boolean didTravel() {
        return didTravel;
    }


    private static class SeaTravelMapSubView extends MapSubView {
        private final TownLocation destination;

        public SeaTravelMapSubView(Model model, TownLocation first) {
            super(model);
            this.destination = first;
        }

        @Override
        protected String getTitleText(Model model) {
            return "TRAVEL BY SEA";
        }

        @Override
        protected String getUnderText(Model model) {
            return "You are traveling by sea to " + destination.getTownName() + ".";
        }

        @Override
        public void specificDrawArea(Model model) {
            model.getWorld().drawYourself(model, model.getParty().getPosition(), model.getParty().getPosition(),
                    MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, model.getParty().getPosition(), false);
        }
    }

    private MyPair<TownLocation, Integer> rollOnTable(Model model, TownLocation currentLocation) {
        TownLocation durham = model.getWorld().getTownByName("East Durham");
        TownLocation cape = model.getWorld().getTownByName("Cape Paxton");
        TownLocation roukon = model.getWorld().getTownByName("Roukon");
        TownLocation ebonshire = model.getWorld().getTownByName("Ebonshire");
        TownLocation sutton = model.getWorld().getTownByName("Little Erinde");
        TownLocation ackerville = model.getWorld().getTownByName("Ackerville");
        TownLocation lower = model.getWorld().getTownByName("Lower Theln");
        TownLocation upper = model.getWorld().getTownByName("Upper Theln");
        TownLocation noShip = null;

        // OBS: If you update these tables, don't forget to update the corresponding BookItem.
        SeaTravelTable table = null;
        if (currentLocation == durham) {
            table = new SeaTravelTable(
                    noShip,    0, noShip,    0,
                    cape,      2,
                    cape,      1,
                    roukon,    3,
                    roukon,    2,
                    ebonshire, 3,
                    ebonshire, 2,
                    ebonshire, 1,
                    lower,     2);
        } else if (currentLocation == cape) {
            table = new SeaTravelTable(
                    noShip, 0,
                    durham, 4,
                    durham, 3,
                    durham, 2,
                    durham, 1,
                    lower, 2,
                    lower, 1,
                    ebonshire, 3,
                    ebonshire, 2,
                    roukon, 2);
        } else if (currentLocation == roukon) {
            table = new SeaTravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    ebonshire, 2, ebonshire, 1,
                    lower, 2, lower, 1,
                    durham, 3, cape, 3);
        } else if (currentLocation == ebonshire) {
            table = new SeaTravelTable(
                    ackerville, 3,
                    sutton, 2,
                    roukon, 2,
                    roukon, 1,
                    lower,  2,
                    lower,  1,
                    cape,   2,
                    cape,   1,
                    durham, 3,
                    durham, 2);
        } else if (currentLocation == sutton) {
            table = new SeaTravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    ebonshire, 2, ebonshire, 1,
                    ackerville, 2, ackerville, 1,
                    lower, 3
            );
        } else if (currentLocation == ackerville) {
            table = new SeaTravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    sutton, 2, sutton, 1,
                    ebonshire, 3,
                    ebonshire, 2
            );
        } else if (currentLocation == lower) {
            table = new SeaTravelTable(
                    noShip, 0,
                    durham, 3,
                    cape, 2,
                    roukon, 3,
                    ebonshire, 2,
                    ebonshire, 1,
                    upper, 3,
                    upper, 2,
                    upper, 1,
                    sutton, 3
            );
        } else if (currentLocation == upper) {
            table = new SeaTravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    lower, 3, lower, 2, lower, 1,
                    cape, 3
            );
        } else {
            throw new IllegalStateException("Cannot travel by sea from current location: " + currentLocation.getName());
        }

        int roll = MyRandom.rollD10();
        return table.get(roll);
    }

    private static class SeaTravelTable {
        private final TownLocation[] destinations;
        private final int[] costs;

        public SeaTravelTable(TownLocation d1, int c1, TownLocation d2, int c2, TownLocation d3, int c3,
                              TownLocation d4, int c4, TownLocation d5, int c5, TownLocation d6, int c6,
                              TownLocation d7, int c7, TownLocation d8, int c8, TownLocation d9, int c9,
                              TownLocation d10, int c10) {
            this.destinations = new TownLocation[]{d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
            this.costs = new int[]{c1, c2, c3, c4, c5, c6, c7, c8, c9, c10};
        }

        public MyPair<TownLocation, Integer> get(int roll) {
            return new MyPair<>(destinations[roll-1], costs[roll-1]);
        }
    }

}

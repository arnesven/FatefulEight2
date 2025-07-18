package model.states;

import model.Model;
import model.map.*;
import util.Arithmetics;
import util.MyLists;
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
        if (model.getCurrentHex().getLocation() instanceof TownLocation) {
            this.ship = rollOnTable(model, (TownLocation) model.getCurrentHex().getLocation());
        } else {
            this.ship = new MyPair<>(null, 0);
        }
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
            if (cost > model.getParty().getGold()) {
                println("Unfortunately you cannot afford to take the trip right now.");
            } else {
                print("Do you want to travel to " + ship.first.getTownName() + "? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().addToGold(-cost);
                    travelTo(model, ship.first);
                } else {
                    println("Ok. But come back soon if you change your mind. The ship will not wait for you.");
                }
            }
        }
        return model.getCurrentHex().getDailyActionState(model);
    }

    private void travelTo(Model model, TownLocation first) {
        didTravel = true;
        travelBySea(model, first.getHex(), this, SHIP_AVATAR, first.getTownName(), true, true);
    }

    public static void travelBySea(Model model, WorldHex hex, GameState state,
                                    Sprite shipAvatar, String destinationName,
                                    boolean voyageTakesTwoDays, boolean withViewTransition) {
        Point viewPoint = new Point(model.getParty().getPosition());
        MapSubView mapSubView = new SeaTravelMapSubView(model, destinationName, viewPoint);
        if (withViewTransition) {
            CollapsingTransition.transition(model, mapSubView);
        } else {
            model.setSubView(mapSubView);
        }
        Point newPosition = model.getWorld().getPositionForHex(hex);

        model.getWorld().dijkstrasBySea(hex);

        Point currentPos = model.getParty().getPosition();
        try {
            int innerPos = WaterTravelAnimations.findWaterDirection(model, currentPos);
            do {
                Point closest = model.getWorld().findClosestWaterPath(currentPos);
                if (closest.equals(currentPos)) {
                    break;
                }
                innerPos = addAnimation(model, mapSubView, currentPos, viewPoint, closest, innerPos, shipAvatar);
                model.getParty().setPosition(closest);
                currentPos = closest;
                if (viewPoint.distance(currentPos) > 3) {
                    viewPoint.x = currentPos.x;
                    viewPoint.y = currentPos.y;
                }
            } while (!(currentPos.x == newPosition.x && currentPos.y == newPosition.y));
            if (!currentPos.equals(newPosition)) {
                innerPos = addAnimation(model, mapSubView, currentPos, viewPoint, newPosition, innerPos, shipAvatar);
            }
            if (model.getWorld().getHex(newPosition) instanceof SeaHex) {
                WaterTravelAnimations.addAnimationToMiddle(model, mapSubView, currentPos, viewPoint, innerPos, shipAvatar);
            }
        } catch (WaterTravelAnimations.FaultyWaterTravelException fwte) {
            System.err.println(fwte.getMessage());
        }

        model.getCurrentHex().travelFrom(model);
        if (voyageTakesTwoDays) {
            state.stepToNextDay(model);
            state.println("The party spends the day on the ship traveling to " + destinationName);
            state.stepToNextDay(model);
        }
        System.out.println("TravelBySeaState: after step to next day.");
        model.getParty().setPosition(newPosition);
        System.out.println("TravelBySeaState: after set Position.");
        model.getCurrentHex().travelTo(model);
        System.out.println("TravelBySeaState: Done with travelTo");
    }

    private static int addAnimation(Model model, MapSubView mapSubView, Point currentPos,
                                     Point viewPoint, Point closest, int innerPos, Sprite shipAvatar) {
        if (model.getWorld().getHex(currentPos) instanceof SeaHex) {
            innerPos = WaterTravelAnimations.animateMovementOverSeaHex(model,
                    mapSubView, currentPos, viewPoint, closest, innerPos, shipAvatar);
        } else {
            innerPos = WaterTravelAnimations.animateMovementAlongWaterEdges(
                    model, mapSubView, currentPos, viewPoint, closest, innerPos, shipAvatar);
        }
        return innerPos;
    }

    public static void travelBySea(Model model, TownLocation town, GameState state,
                                   boolean voyageTakesTwoDays, boolean withViewTransition) {
        if (model.getCurrentHex().getLocation() != null) {
            DiscoveredRoute.uniqueAdd(model, model.getParty().getDiscoveredRoutes(),
                    model.getCurrentHex().getLocation(), town, DiscoveredRoute.SHIP);
        }
       travelBySea(model, town.getHex(), state, SHIP_AVATAR, town.getTownName(), voyageTakesTwoDays, withViewTransition);
    }

    public static void travelBySea(Model model, WorldHex hex, GameState state, Sprite avatar,
                                   boolean voyageTakesTwoDays, boolean withViewTransition) {
        Point p = model.getWorld().getPositionForHex(hex);
        travelBySea(model, hex, state, avatar, "(" + p.x + "," + p.y + ")",
                voyageTakesTwoDays, withViewTransition);
    }

    public boolean didTravel() {
        return didTravel;
    }


    private static class SeaTravelMapSubView extends MapSubView {
        private final String destination;
        private final Point viewPoint;

        public SeaTravelMapSubView(Model model, String destination, Point viewPoint) {
            super(model);
            this.destination = destination;
            this.viewPoint = viewPoint;
        }

        @Override
        protected String getTitleText(Model model) {
            return "TRAVEL BY SEA";
        }

        @Override
        protected String getUnderText(Model model) {
            return "You are traveling by sea to " + destination + ".";
        }

        @Override
        public void specificDrawArea(Model model) {
            model.getWorld().drawYourself(model, viewPoint, model.getParty().getPosition(),
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
        TravelTable table = null;
        if (currentLocation == durham) {
            table = new TravelTable(
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
            table = new TravelTable(
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
            table = new TravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    ebonshire, 2, ebonshire, 1,
                    lower, 2, lower, 1,
                    durham, 3, cape, 3);
        } else if (currentLocation == ebonshire) {
            table = new TravelTable(
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
            table = new TravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    ebonshire, 2, ebonshire, 1,
                    ackerville, 2, ackerville, 1,
                    lower, 3
            );
        } else if (currentLocation == ackerville) {
            table = new TravelTable(
                    noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0, noShip, 0,
                    sutton, 2, sutton, 1,
                    ebonshire, 3,
                    ebonshire, 2
            );
        } else if (currentLocation == lower) {
            table = new TravelTable(
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
            table = new TravelTable(
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

}

package model.states.events;

import model.Model;
import model.map.DiscoveredRoute;
import model.map.TownLocation;
import model.states.GameState;
import model.states.TravelTable;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CollapsingTransition;
import view.subviews.MapSubView;

import java.awt.*;
import java.util.List;

public class TravelByCarriageState extends GameState {

    private final MyPair<TownLocation, Integer> carriage;
    private final int limit;
    private boolean didTravel;
    public static final Sprite AVATAR = new Sprite32x32("carriageavatar", "world.png", 0x60,
            MyColors.BROWN, MyColors.BEIGE, MyColors.BROWN);

    public TravelByCarriageState(Model model) {
        super(model);
        this.carriage = rollOnTable(model, (TownLocation)model.getCurrentHex().getLocation());
        this.limit = MyRandom.randInt(5, 7);
    }

    @Override
    public GameState run(Model model) {
        if (carriage.first == null) {
            println("This carriage will not be departing today. " +
                    "If you want to travel with it you will have to come back again tomorrow.");
        } if (model.getParty().size() > limit) {
            println("The driver can't take your party, since there are too many of you.");
        } else {
            int cost = Math.max(1, carriage.second * model.getParty().size() / 2);
            println("This carriage is departing for the " + carriage.first.getName() + " soon." +
                    " The driver will take your party for " + cost + " gold. The trip takes 3 days.");
            print("Do you want to travel to " + carriage.first.getTownName() + "? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-cost);
                travelTo(model, carriage);
            } else {
                println("Ok. But come back soon if you change your mind. The carriage will not wait for you.");
            }
        }
        return model.getCurrentHex().getDailyActionState(model);
    }

    private void travelTo(Model model, MyPair<TownLocation, Integer> carriage) {
        this.didTravel = true;
        travelByCarriage(model, carriage, this);
    }

    private static void travelByCarriage(Model model, MyPair<TownLocation, Integer> carriage, TravelByCarriageState state) {
        MapSubView mapSubView = new CarriageTravelSubView(model, carriage.first);
        CollapsingTransition.transition(model, mapSubView);
        Point newPosition = model.getWorld().getPositionForHex(carriage.first.getHex());
        if (model.getCurrentHex().getLocation() != null) {
            DiscoveredRoute.uniqueAdd(model, model.getParty().getDiscoveredRoutes(), carriage.first,
                    model.getCurrentHex().getLocation(), DiscoveredRoute.CARRIAGE);
        }
        model.getWorld().dijkstrasByLand(model.getWorld().getPositionForHex(carriage.first.getHex()), false);

        Point currentPos = model.getParty().getPosition();
        List<Point> path = model.getWorld().shortestPathToPoint(currentPos);
        path.remove(path.size()-1);
        Point closest;
        do {
            closest = path.remove(path.size()-1);
            if (closest.equals(currentPos)) {
                break;
            }
            mapSubView.addMovementAnimation(
                    AVATAR,
                    model.getWorld().translateToScreen(currentPos, currentPos, MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                    model.getWorld().translateToScreen(closest, currentPos, MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
            mapSubView.waitForAnimation();
            mapSubView.removeMovementAnimation();
            model.getParty().setPosition(closest);
            currentPos = closest;
        } while (!(currentPos.x == newPosition.x && currentPos.y == newPosition.y));


        model.getCurrentHex().travelFrom(model);
        state.stepToNextDay(model);
        state.println("The party spends the day on the carriage traveling to the " + carriage.first.getName());
        state.stepToNextDay(model);
        state.println("The party spends the day on the carriage traveling to the " + carriage.first.getName());
        state.stepToNextDay(model);
        model.getParty().setPosition(newPosition);
        model.getCurrentHex().travelTo(model);
    }


    private MyPair<TownLocation, Integer> rollOnTable(Model model, TownLocation current) {
        TownLocation bullsville = model.getWorld().getTownByName("Bullsville");
        TownLocation sheffield = model.getWorld().getTownByName("Sheffield");
        TownLocation southMeadhome = model.getWorld().getTownByName("South Meadhome");
        TownLocation upperTheln = model.getWorld().getTownByName("Upper Theln");
        TownLocation saintQuellin = model.getWorld().getTownByName("Saint Quellin");
        TownLocation ashtonShire = model.getWorld().getTownByName("Ashtonshire");
        TownLocation urnTown = model.getWorld().getTownByName("Urntown");
        TownLocation noTrip = null;

        TravelTable table = null;
        if (current == bullsville) {
            table = new TravelTable(
                    sheffield, 1,
                    sheffield, 2,
                    saintQuellin, 1,
                    saintQuellin, 2,
                    upperTheln, 1,
                    upperTheln, 2,
                    upperTheln, 3,
                    noTrip, 0, noTrip, 0, noTrip, 0);
        } else if (current == sheffield) {
            table = new TravelTable(
                    southMeadhome, 1,
                    southMeadhome, 2,
                    upperTheln, 1,
                    upperTheln, 2,
                    saintQuellin, 1,
                    saintQuellin, 2,
                    bullsville, 1,
                    bullsville, 2,
                    noTrip, 0, noTrip, 0);
        } else if (current == southMeadhome) {
            table = new TravelTable(
                    sheffield, 1,
                    sheffield, 2,
                    sheffield, 3,
                    upperTheln, 1,
                    upperTheln, 2,
                    upperTheln, 3,
                    noTrip, 0, noTrip, 0, noTrip, 0, noTrip, 0);
        } else if (current == upperTheln) {
            table = new TravelTable(
                    southMeadhome, 2,
                    sheffield, 1,
                    sheffield, 2,
                    sheffield, 3,
                    saintQuellin, 1,
                    saintQuellin, 2,
                    ashtonShire, 1,
                    ashtonShire, 2,
                    urnTown, 3,
                    bullsville, 3);
        } else if (current == saintQuellin) {
            table = new TravelTable(
                    upperTheln, 1,
                    upperTheln, 2,
                    bullsville, 1,
                    bullsville, 2,
                    bullsville, 3,
                    sheffield, 1,
                    sheffield, 2,
                    sheffield, 3,
                    ashtonShire, 1,
                    ashtonShire, 2
            );
        } else if (current == ashtonShire) {
            table = new TravelTable(
                    urnTown, 1,
                    urnTown, 2,
                    upperTheln, 1,
                    upperTheln, 2,
                    upperTheln, 3,
                    saintQuellin, 2,
                    saintQuellin, 3,
                    noTrip, 0, noTrip, 0, noTrip, 0);
        } else if (current == urnTown) {
            table = new TravelTable(
                    ashtonShire, 1,
                    ashtonShire, 2,
                    ashtonShire, 3,
                    upperTheln, 3,
                    saintQuellin, 4,
                    noTrip, 0, noTrip, 0, noTrip, 0, noTrip, 0, noTrip, 0);
        }
        return table.get(MyRandom.rollD10());
    }

    public boolean didTravel() {
        return didTravel;
    }

    private static class CarriageTravelSubView extends MapSubView {
        private final TownLocation destination;

        public CarriageTravelSubView(Model model, TownLocation first) {
            super(model);
            this.destination = first;
        }

        @Override
        protected String getTitleText(Model model) {
            return "TRAVEL WITH CARRIAGE";
        }

        @Override
        protected String getUnderText(Model model) {
            return "You are traveling with a carriage to " + destination.getTownName() + ".";
        }

        @Override
        public void specificDrawArea(Model model) {
            model.getWorld().drawYourself(model, model.getParty().getPosition(), model.getParty().getPosition(),
                    MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, model.getParty().getPosition(), false);
        }
    }
}

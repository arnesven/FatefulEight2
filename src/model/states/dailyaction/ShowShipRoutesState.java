package model.states.dailyaction;

import model.Model;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.states.GameState;

import java.util.List;

public class ShowShipRoutesState extends GameState {
    public ShowShipRoutesState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("There's a sign by the docks. It says: ");
        TownLocation urb = (TownLocation) model.getCurrentHex().getLocation();
        List<String> routes = urb.getSeaTravelRoutes();
        if (routes.isEmpty()) {
            println("* No routes currently available *");
        } else {
            println("Outbound routes:");
            for (String s : routes) {
                println(s);
            }
        }
        return null; // Will not be used
    }
}

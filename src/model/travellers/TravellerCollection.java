package model.travellers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravellerCollection implements Serializable {

    public static final int MAX_ACTIVE = 2;
    private final List<Traveller> activeTravellers = new ArrayList<>();
    private final List<Traveller> completedTravellers = new ArrayList<>();
    private final List<Traveller> abandonedTravellers = new ArrayList<>();

    public void add(Traveller traveller) {
        activeTravellers.add(traveller);
    }

    public List<Traveller> getActiveTravellers() {
        return activeTravellers;
    }

    public void completeTraveller(Traveller traveller) {
        activeTravellers.remove(traveller);
        completedTravellers.add(traveller);
    }

    public List<Traveller> getCompletedTravellers() {
        return completedTravellers;
    }

    public void abandonTraveller(Traveller traveller) {
        activeTravellers.remove(traveller);
        abandonedTravellers.add(traveller);
    }
}

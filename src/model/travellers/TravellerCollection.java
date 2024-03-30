package model.travellers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravellerCollection implements Serializable {

    private final List<Traveller> activeTravellers = new ArrayList<>();
    private final List<Traveller> completedTravellers = new ArrayList<>();
    private final List<Traveller> abandonedTravellers = new ArrayList<>();

    public void add(Traveller traveller) {
        activeTravellers.add(traveller);
    }

    public List<Traveller> getActiveTravellers() {
        return activeTravellers;
    }
}

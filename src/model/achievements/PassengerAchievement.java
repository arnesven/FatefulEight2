package model.achievements;

import model.GameStatistics;
import model.Model;

public class PassengerAchievement extends PassiveAchievement {
    public PassengerAchievement() {
        super("Passenger", "You've taken 30 boat voyages or carriage trips.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getBoatVoyages() + GameStatistics.getCarriageTrips() >= 30;
    }
}

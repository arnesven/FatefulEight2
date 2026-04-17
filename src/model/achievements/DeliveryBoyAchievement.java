package model.achievements;

import model.GameStatistics;
import model.Model;

public class DeliveryBoyAchievement extends PassiveAchievement {
    private static final int COUNT_REQUIRED = 6;

    public DeliveryBoyAchievement() {
        super("Delivery Boy", "You have made " + COUNT_REQUIRED + " deliveries.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getDeliveries() >= COUNT_REQUIRED;
    }
}

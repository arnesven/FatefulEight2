package model.mainstory.thepast;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.map.locations.AncientCityLocation;
import model.tasks.DestinationTask;

import java.awt.*;

public class VisitAncientCityTask extends PastWorldDestinationTask {
    private final String cityName;
    private final Point destinationPoint;

    public VisitAncientCityTask(Point destinationPoint, AncientCityLocation cityName) {
        super(destinationPoint, "Visit the " + cityName.getName());
        this.cityName = cityName.getPlaceName();
        this.destinationPoint = destinationPoint;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new ThePastJournalEntry() {
            @Override
            public String getName() {
                return getDestinationDescription();
            }

            @Override
            public String getText() {
                return "You've heard about the " + cityName + ". It is supposedly not as dangerous as the capital of Recca. " +
                        "You should visit it.";
            }

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return destinationPoint;
            }
        };
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return null;
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return false;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}

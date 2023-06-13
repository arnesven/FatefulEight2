package model;

import model.journal.JournalEntry;
import model.journal.RuinsEntry;
import model.journal.VisitTask;
import model.map.UrbanLocation;
import model.states.EveningState;
import model.states.InitialLeadsEveningState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainStory implements Serializable {
    private boolean initialLeadsEventGiven = false;
    private boolean townVisited = false;
    private boolean castleVisited = false;
    private boolean templeVisited = false;

    public EveningState generateInitialLeadsEveningState(Model model) {
        if (initialLeadsEventGiven || model.getCurrentHex().getLocation() instanceof UrbanLocation || model.getParty().size() < 2) {
            return null;
        }
        return new InitialLeadsEveningState(model);
    }

    public List<JournalEntry> getMainStoryTasks(Model model) {
        if (initialLeadsEventGiven) {
            return List.of(new VisitTask("Town", townVisited),
                    new VisitTask("Castle", castleVisited),
                    new VisitTask("Temple", templeVisited),
                    new RuinsEntry(model)
            );
        }
        return new ArrayList<>();
    }

    public void setVisitedTown(boolean b) {
        townVisited = b;
    }

    public void setVisitedCastle(boolean b) {
        castleVisited = b;
    }

    public void setVisitedTemple(boolean b) {
        templeVisited = b;
    }

    public void setInitialLeadsGiven(boolean b) {
        initialLeadsEventGiven = b;
    }
}

package model.journal;

import model.Model;

import java.awt.*;

public class VisitTask extends MainStoryTask {
    private final boolean visited;
    private final String location;

    public VisitTask(String object, boolean visited) {
        super("Visit a " + object);
        this.location = object;
        this.visited = visited;
    }

    @Override
    public String getText() {
        return "Visit a " + location.toLowerCase() + "." + (visited ? "\n\nCompleted." : "");
    }

    @Override
    public boolean isComplete() {
        return visited;
    }

    @Override
    public Point getPosition(Model model) {
        return null;
    }
}

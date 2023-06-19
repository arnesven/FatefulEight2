package model.journal;

public class VisitWitchTask extends MainStoryTask {
    public VisitWitchTask() {
        super("Visit Witch");
    }

    @Override
    public String getText() {
        return "Visit the witch to ask about the crimson pearl.";
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}

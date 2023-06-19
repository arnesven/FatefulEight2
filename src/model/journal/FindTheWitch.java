package model.journal;

public class FindTheWitch extends MainStoryTask {
    public FindTheWitch() {
        super("Find the Witch");
    }

    @Override
    public String getText() {
        return "Find Everix's acquaintance, the witch, to ask about the crimson pearl.";
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}

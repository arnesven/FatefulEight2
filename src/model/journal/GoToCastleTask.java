package model.journal;

public class GoToCastleTask extends MainStoryTask {
    public GoToCastleTask(String castleName) {
        super("Reward at " + castleName + "");
    }

    @Override
    public String getText() {
        return "TODO";
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}

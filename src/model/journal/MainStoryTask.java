package model.journal;

public abstract class MainStoryTask implements JournalEntry {

    private final String name;

    public MainStoryTask(String name) {
        this.name = name;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTask() {
        return true;
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public String toString() {
        return "MainStoryTask{" +
                "name='" + name + '\'' +
                " text='" + getText() + '\'' +
                '}';
    }
}

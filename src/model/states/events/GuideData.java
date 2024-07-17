package model.states.events;

public class GuideData {
    private String name;
    private String description;

    public GuideData(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

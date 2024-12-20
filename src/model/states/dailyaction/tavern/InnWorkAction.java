package model.states.dailyaction.tavern;

import model.Model;

public abstract class InnWorkAction {

    private final String name;
    private final String description;

    public InnWorkAction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void doWork(Model model, TalkToBartenderState state);
}

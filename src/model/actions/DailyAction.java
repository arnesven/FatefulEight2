package model.actions;

import model.Model;
import model.states.GameState;

public class DailyAction {
    private final String name;
    private GameState state;

    public DailyAction(String name, GameState state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public char getShortKey() {
        return name.charAt(0);
    }

    public GameState getState() {
        return this.state;
    }

    public void runPreHook(Model model) {

    }
}

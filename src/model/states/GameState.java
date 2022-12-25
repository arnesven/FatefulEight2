package model.states;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.quests.QuestNode;
import view.subviews.CollapsingTransition;
import view.subviews.OnTheRoadSubView;
import view.subviews.SubView;

import java.util.List;

public abstract class GameState {

    private final Model model;

    public GameState(Model model) {
        this.model = model;
    }

    public abstract GameState run(Model model);

    protected Model getModel() { return model; }

    public void print(String s) {
        model.getLog().addAnimated(s);
    }

    public void println(String s) {
        model.getLog().addAnimated(s + "\n");
    }

    protected List<DailyAction> getDailyActions() {
        return model.getDailyActions();
    }

    public char singleCharInput() {
        model.getLog().acceptSingleCharInput();
        return internalInput().charAt(0);
    }

    public String lineInput() {
        model.getLog().acceptLineInput();
        return internalInput();
    }

    public void waitForReturn() {
        model.getLog().waitForReturn();
        internalInput();
    }

    public boolean yesNoInput() {
        while (true) {
            model.getLog().acceptLineInput();
            String response = internalInput().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            }
        }
    }

    private String internalInput() {
        while (!model.gameExited()) {
            if (model.getLog().inputReady()) {
                return model.getLog().getInput();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
        throw new IllegalStateException("Program failed to exit");
    }

    protected void setCurrentTerrainSubview(Model model) {
        SubView nextSubView;
        if (!model.getCurrentHex().hasLodging() && model.getParty().isOnRoad()) {
            nextSubView = OnTheRoadSubView.instance;
        } else {
            nextSubView = model.getCurrentHex().getImageSubView();
        }
        if (model.getSubView() != nextSubView) {
            CollapsingTransition.transition(model, nextSubView);
        }
    }
}

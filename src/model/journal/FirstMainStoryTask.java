package model.journal;

import model.characters.GameCharacter;

public class FirstMainStoryTask extends MainStoryTask {
    private final GameCharacter whosUncle;
    private final String town;
    private final int step;


    public FirstMainStoryTask(String startLocation, GameCharacter whosUncle, int storyStep) {
        super(whosUncle.getFirstName() + "'s Uncle");
        this.town = startLocation;
        this.whosUncle = whosUncle;
        this.step = storyStep;
    }

    @Override
    public String getText() {
        switch (step) {
            case 0:
                return "Visit " + whosUncle.getFirstName() + "'s uncle in the " + town +
                    ". He needs a capable group of adventurers to take care of a 'Frogmen Problem'.";
            case 1:
                return "Complete the 'Frogmen Problem' Quest.";
            case 2:
                return "Return to " + whosUncle.getFirstName() + "'s uncle to claim your reward.";
        }
        return "Completed";
    }

    @Override
    public boolean isComplete() {
        return step > 2;
    }
}

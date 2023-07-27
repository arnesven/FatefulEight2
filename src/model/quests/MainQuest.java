package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.journal.StoryPart;
import model.states.GameState;
import model.states.QuestState;

public abstract class MainQuest extends Quest {
    private CharacterAppearance portrait;
    private boolean completed = false; // TODO: This is not persistent in save... causes some quests to be labled as "in progress".
    private StoryPart storyPart;

    public MainQuest(String name, String provider, QuestDifficulty difficulty, int partyRep, int gold, int exp, String text, String endText) {
        super(name, provider, difficulty, partyRep, gold, exp, text, endText);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return portrait;
    }

    public void setPortrait(CharacterAppearance unclePortrait) {
        this.portrait = unclePortrait;
    }

    public void setStoryPart(StoryPart storyPart) {
        this.storyPart = storyPart;
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            storyPart.increaseStep(model);
            this.completed = true;
        } else if (model.getCurrentHex().getLocation() != null) {
            state.println("However, this quest can be accepted again.");
            model.getQuestDeck().unsetFailureIn(model.getCurrentHex().getLocation());
            resetQuest();
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    public boolean isCompleted() {
        return completed;
    }

    public abstract MainQuest copy();
}

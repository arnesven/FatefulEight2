package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.states.GameState;
import model.states.QuestState;

public abstract class MainQuest extends Quest {
    private CharacterAppearance portrait;

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

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            model.getMainStory().increaseStep(model, getStoryTrack());
        } else if (model.getCurrentHex().getLocation() != null) {
            state.println("However, this quest can be accepted again.");
            model.getQuestDeck().unsetFailureIn(model.getCurrentHex().getLocation());
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    protected abstract int getStoryTrack();

}

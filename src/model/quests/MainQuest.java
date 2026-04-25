package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.items.special.CrimsonPearl;
import model.journal.MainStorySpawnLocation;
import model.journal.StoryPart;
import model.states.GameState;
import model.states.QuestState;

public abstract class MainQuest extends Quest {
    private CharacterAppearance portrait;
    private StoryPart storyPart;

    public MainQuest(String name, String provider, QuestDifficulty difficulty,
                     Reward reward, int moveAfter, String text, String endText) {
        super(name, provider, difficulty, reward, moveAfter, text, endText);
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

    public void setSpawnData(MainStorySpawnLocation spawnData) {

    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            storyPart.increaseStep(model);
            model.getSettings().getMiscFlags().put(getSettingsKey(), true);
        } else {
            resetQuest();
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    private String getSettingsKey() {
        return getName() + "-quest-completed";
    }

    public boolean isCompleted(Model model) {
        return model.getParty().getQuestHandler().getOfferedQuest(getName()).isCompleted();
    }

    public abstract MainQuest copy();

    protected void getCrimsonPearl(Model model, QuestState state) {
        state.println("The party receives a Crimson Pearl.");
        model.getParty().addToInventory(new CrimsonPearl());
    }
}

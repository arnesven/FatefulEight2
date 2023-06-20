package model.quests;

import model.characters.appearance.CharacterAppearance;

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
}

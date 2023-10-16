package model.quests.scenes;

import model.Model;
import model.classes.Skill;
import model.items.Lockpick;
import model.states.QuestState;
import sound.SoundEffects;

public class SoloLockpickingSubScene extends SoloSkillCheckSubScene{
    public SoloLockpickingSubScene(int col, int row, int difficulty, String text) {
        super(col, row, Skill.Security, difficulty, text);
    }

    @Override
    public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
        int newDifficulty = Lockpick.askToUseLockpick(model, state, difficulty);
        boolean result = super.performSkillCheck(model, state, skill, newDifficulty);
        if (newDifficulty != difficulty) {
            Lockpick.checkForBreakage(model, state, result);
        }
        if (result) {
            SoundEffects.playUnlock();
        }
        return result;
    }
}

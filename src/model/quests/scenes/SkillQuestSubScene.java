package model.quests.scenes;

import model.Model;
import model.classes.Skill;
import model.quests.QuestEdge;
import model.quests.QuestSubScene;
import model.states.QuestState;
import model.states.SpellCastException;
import view.MyColors;

public abstract class SkillQuestSubScene extends QuestSubScene {

    private final String text;
    private final Skill skill;
    private final int difficulty;

    public SkillQuestSubScene(int col, int row, String leaderTalk, Skill skill, int difficulty) {
        super(col, row);
        this.text = leaderTalk;
        this.skill = skill;
        this.difficulty = difficulty;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.LIGHT_GREEN;
    }

    @Override
    public String getDescription() {
        return getDescriptionType() + " Skill Check " + skill.getName() + " " + difficulty;
    }

    protected abstract String getDescriptionType();

    @Override
    public final QuestEdge run(Model model, QuestState state) {
        state.setCursorEnabled(false);
        if (model.getParty().size() > 1) {
            if (!text.equals("") && !model.getParty().getBench().contains(model.getParty().getLeader())) {
                state.leaderSay(text);
            }
        }
        acceptAllSpells(model);
        boolean skillSuccess = false;
        do {
            try {
                skillSuccess = performSkillCheck(model, state, skill, difficulty);
                model.getLog().waitForAnimationToFinish();
                break;
            } catch (SpellCastException sce) {
                QuestEdge edge = tryCastSpell(model, state, sce);
                if (edge != null) {
                    return edge;
                }
            }
        } while (true);

        state.setCursorEnabled(true);
        unacceptAllSpells(model);
        return getEdgeToReturn(skillSuccess);
    }

    protected QuestEdge getEdgeToReturn(boolean skillCheckWasSuccessful) {
        if (skillCheckWasSuccessful) {
            return getSuccessEdge();
        }
        return getFailEdge();
    }

    protected abstract boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty);

    protected int getDifficulty() {
        return difficulty;
    }

    protected Skill getSkill() {
        return skill;
    }
}

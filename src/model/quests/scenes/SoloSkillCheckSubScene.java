package model.quests.scenes;

import model.Model;
import model.classes.Skill;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class SoloSkillCheckSubScene extends SkillQuestSubScene {
    private final Skill skill;
    private final int difficulty;
    private static final Sprite32x32 SPRITE = new Sprite32x32("soloskillscene", "quest.png", 0x10,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    private final String leaderTalk;

    public SoloSkillCheckSubScene(int col, int row, Skill skill, int difficulty, String leaderTalk) {
        super(col, row);
        this.skill = skill;
        this.difficulty = difficulty;
        this.leaderTalk = leaderTalk;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public String getDescription() {
        return "Solo Skill Check " + skill.getName() + " " + difficulty;
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.setCursorEnabled(false);
        if (model.getParty().size() > 1) {
            leaderSay(model, leaderTalk);
        }
        boolean success = model.getParty().doSoloSkillCheck(model, state, skill, difficulty);
        state.setCursorEnabled(true);
        if (success) {
            return getSuccessEdge();
        }
        return getFailEdge();
    }
}

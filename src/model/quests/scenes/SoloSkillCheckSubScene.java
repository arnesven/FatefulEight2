package model.quests.scenes;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.states.QuestState;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class SoloSkillCheckSubScene extends SkillQuestSubScene {
    private final Skill skill;
    private final int difficulty;
    public static final Sprite32x32 SPRITE = new Sprite32x32("soloskillscene", "quest.png", 0x10,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    private final String leaderTalk;
    private GameCharacter performer;

    public SoloSkillCheckSubScene(int col, int row, Skill skill, int difficulty, String leaderTalk) {
        super(col, row);
        this.skill = skill;
        this.difficulty = difficulty;
        this.leaderTalk = leaderTalk;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
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
        MyPair<Boolean, GameCharacter> success = model.getParty().doSoloSkillCheckWithPerformer(model, state, skill, difficulty);
        this.performer = success.second;
        state.setCursorEnabled(true);
        if (success.first) {
            return getSuccessEdge();
        }
        return getFailEdge();
    }

    protected GameCharacter getPerformer() {
        return performer;
    }

    @Override
    public String getDetailedDescription() {
        String diffStr = "E";
        if (difficulty > 9) {
            diffStr = "H";
        } else if (difficulty > 6) {
            diffStr = "M";
        }
        return skill.getName() + " " + diffStr;
    }
}

package model.quests.scenes;

import model.Model;
import model.classes.Skill;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class CollaborativeSkillCheckSubScene extends SkillQuestSubScene {
    private static final Sprite32x32 SPRITE = new Sprite32x32("colabskillscene", "quest.png", 0x11,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    public CollaborativeSkillCheckSubScene(int col, int row, Skill skill, int difficulty, String leaderTalk) {
        super(col, row, leaderTalk, skill, difficulty);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    protected String getDescriptionType() {
        return "Collaborative";
    }

    @Override
    public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
        return model.getParty().doCollaborativeSkillCheck(model, state, skill, difficulty);
    }

    @Override
    public String getDetailedDescription() {
        String diffStr = "E";
        if (getDifficulty() > 11) {
            diffStr = "H";
        } else if (getDifficulty() > 8) {
            diffStr = "M";
        }
        return getSkill().getName() + " " + diffStr;
    }
}

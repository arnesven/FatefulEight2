package model.quests.scenes;

import model.Model;
import model.classes.Skill;
import model.states.GameState;
import model.states.QuestState;
import model.states.events.RareBirdEvent;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class CollectiveSkillCheckSubScene extends SkillQuestSubScene {
    private static final Sprite32x32 SPRITE = new Sprite32x32("collectiveskill", "quest.png", 0x04,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);


    public CollectiveSkillCheckSubScene(int col,  int row, Skill skill, int difficulty, String leaderTalk) {
        super(col, row, leaderTalk, skill, difficulty);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    protected String getDescriptionType() {
        return "Collective";
    }

    @Override
    public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
        if (skill == Skill.Sneak && RareBirdEvent.checkForSquawk(model, state)) {
            difficulty += 6;
        }
        return model.getParty().doCollectiveSkillCheck(model, state, skill, difficulty);
    }

    @Override
    public String getDetailedDescription() {
        String diffStr = "E";
        if (getDifficulty() > 4) {
            diffStr = "H";
        } else if (getDifficulty() > 2) {
            diffStr = "M";
        }
        return getSkill().getName() + " " + diffStr;
    }

    public static String getDifficultyString(int difficulty) {
        if (difficulty > 4) {
            return "HARD";
        }
        if (difficulty > 2) {
            return "MEDIUM";
        }
        return "EASY";
    }
}

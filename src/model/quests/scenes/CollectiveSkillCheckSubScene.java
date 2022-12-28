package model.quests.scenes;

import model.Model;
import model.classes.Skill;
import model.quests.Quest;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.quests.QuestSubScene;
import model.states.GameState;
import model.states.QuestState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class CollectiveSkillCheckSubScene extends SkillQuestSubScene {
    private final Skill skill;
    private final int difficulty;

    private static final Sprite32x32 SPRITE = new Sprite32x32("collectiveskill", "quest.png", 0x04,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);


    public CollectiveSkillCheckSubScene(int col,  int row, Skill skill, int difficulty) {
        super(col, row);
        this.skill = skill;
        this.difficulty = difficulty;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public String getDescription() {
        return "Collective Skill Check " + skill.getName() + " " + difficulty;
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.setCursorEnabled(false);
        boolean success = model.getParty().doCollectiveSkillCheck(model, state, skill, difficulty);
        state.setCursorEnabled(true);
        if (success) {
            return getSuccessEdge();
        }
        return getFailEdge();
    }

}

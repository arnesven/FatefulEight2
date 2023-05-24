package model.quests.scenes;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.GameState;
import model.states.QuestState;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class SoloSkillCheckSubScene extends SkillQuestSubScene {
    public static final Sprite32x32 SPRITE = new Sprite32x32("soloskillscene", "quest.png", 0x10,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    private GameCharacter performer;

    public SoloSkillCheckSubScene(int col, int row, Skill skill, int difficulty, String leaderTalk) {
        super(col, row, leaderTalk, skill, difficulty);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    protected String getDescriptionType() {
        return "Solo";
    }

    @Override
    public boolean performSkillCheck(Model model, QuestState state, Skill skill, int difficulty) {
        MyPair<Boolean, GameCharacter> success = model.getParty().doSoloSkillCheckWithPerformer(model, state, skill, difficulty);
        this.performer = success.second;
        return success.first;
    }

    protected GameCharacter getPerformer() {
        return performer;
    }

    @Override
    public String getDetailedDescription() {
        String diffStr = "E";
        if (getDifficulty() > 9) {
            diffStr = "H";
        } else if (getDifficulty() > 6) {
            diffStr = "M";
        }
        return getSkill().getName() + " " + diffStr;
    }
}

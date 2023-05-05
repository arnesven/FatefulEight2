package model.quests;

import model.Model;
import model.map.HexLocation;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class QuestFailNode extends QuestNode {

    private static final Sprite32x32 SPRITE = new Sprite32x32("collectiveskill", "quest.png", 0x06,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }


    @Override
    public int getColumn() {
        return 1;
    }

    @Override
    public int getRow() {
        return QuestState.QUEST_MATRIX_ROWS-1;
    }

    @Override
    public String getDescription() {
        return "Quest Failed";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.println("You have failed the quest!");
        model.getQuestDeck().setFailureIn(model.getCurrentHex().getLocation());
        return new QuestEdge(this);
    }

}

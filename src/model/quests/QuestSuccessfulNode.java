package model.quests;

import model.Model;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class QuestSuccessfulNode extends QuestNode {

    private static final Sprite32x32 SPRITE = new Sprite32x32("collectiveskill", "quest.png", 0x05,
            MyColors.BLACK, MyColors.WHITE, MyColors.GREEN, MyColors.GREEN);
    private final Reward reward;
    private final String text;
    private int numberOfPartyMembers;

    public QuestSuccessfulNode(Reward reward, String text) {
        this.reward = reward;
        this.text = text;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public int getColumn() {
        return QuestState.QUEST_MATRIX_COLUMNS-2;
    }

    @Override
    public int getRow() {
        return QuestState.QUEST_MATRIX_ROWS-1;
    }

    @Override
    public String getDescription() {
        return "Quest Successful";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.println(text);
        state.print("Quest completed! You receive " + (reward.getGold() * numberOfPartyMembers) + " gold");
        if (reward.getReputation() > 0) {
            state.println(" and your reputation increases!");
        } else {
            state.println(".");
        }
        reward.giveYourself(model.getParty(), numberOfPartyMembers);
        return new QuestEdge(this);
    }

    public void setNumberOfStartingPartyMembers(int size) {
        this.numberOfPartyMembers = size;
    }
}

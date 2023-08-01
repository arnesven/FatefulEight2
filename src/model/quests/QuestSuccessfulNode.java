package model.quests;

import model.Model;
import model.map.HexLocation;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class QuestSuccessfulNode extends QuestNode {

    private static final Sprite32x32 SPRITE = new Sprite32x32("collectiveskill", "quest.png", 0x05,
            MyColors.BLACK, MyColors.WHITE, MyColors.GREEN, MyColors.GREEN);
    private final Reward reward;
    private final String text;
    private final Point position;
    private int numberOfPartyMembers;

    public QuestSuccessfulNode(Reward reward, String text) {
        this.reward = reward;
        this.text = text;
        this.position = new Point(QuestState.QUEST_MATRIX_COLUMNS-2,
                QuestState.QUEST_MATRIX_ROWS-1);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public int getColumn() {
        return position.x;
    }

    @Override
    public int getRow() {
        return position.y;
    }

    @Override
    public String getDescription() {
        return "Quest Successful";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.println(text);
        int gold = (reward.getGold() * numberOfPartyMembers);
        String goldPart = " You receive " + gold + " gold,";
        if (gold == 0) {
            goldPart = "";
        }
        if (reward.getExp() > 0) {
            goldPart += " each party member receives " + reward.getExp() + " XP,";
        }
        state.print("Quest completed!" + goldPart);
        if (reward.getReputation() > 0) {
            state.println(" and your reputation increases!");
        } else if (reward.getReputation() < 0) {
            state.println(" but your reputation has decreased!");
        } else {
            state.println(".");
        }
        model.getQuestDeck().setSuccessfulIn(model.getCurrentHex().getLocation());
        reward.giveYourself(model, numberOfPartyMembers);
        return new QuestEdge(this);
    }

    public void setNumberOfStartingPartyMembers(int size) {
        this.numberOfPartyMembers = size;
    }

    public void move(int col, int row) {
        this.position.x = col;
        this.position.y = row;
    }
}

package model.quests.scenes;

import model.Model;
import model.quests.QuestDecisionPoint;
import model.quests.QuestEdge;
import model.quests.QuestJunction;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class ChooseNode extends QuestJunction {
    private static final Sprite32x32 SPRITE = new Sprite32x32("simplejunc", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);

    public ChooseNode(int col, int row, List<QuestEdge> questEdges) {
        super(col, row);
        for (QuestEdge edge : questEdges) {
            connectTo(edge);
        }
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        QuestEdge finalEdge = QuestDecisionPoint.questNodeInput(model, state, getConnections());
        return finalEdge;
    }
}

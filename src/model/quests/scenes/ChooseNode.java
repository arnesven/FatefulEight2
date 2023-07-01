package model.quests.scenes;

import model.Model;
import model.quests.MovingEnemyGroup;
import model.quests.QuestDecisionPoint;
import model.quests.QuestEdge;
import model.quests.QuestJunction;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class ChooseNode extends QuestJunction {
    private static final Sprite32x32 SPRITE = new Sprite32x32("choosenode", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);
    private MovingEnemyGroup enemyGroup;

    public ChooseNode(int col, int row, List<QuestEdge> questEdges) {
        super(col, row);
        for (QuestEdge edge : questEdges) {
            connectTo(edge);
        }
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        if (enemyGroup != null) {
            enemyGroup.drawYourself(model.getScreenHandler(), new Point(xPos, yPos));
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        preRunHook(model, state);
        QuestEdge finalEdge = QuestDecisionPoint.questNodeInput(model, state, getConnections());
        return finalEdge;
    }

    protected void preRunHook(Model model, QuestState state) {
    }

    public void setEnemyGroup(MovingEnemyGroup movingEnemyGroup) {
        this.enemyGroup = movingEnemyGroup;
    }

    public MovingEnemyGroup getEnemyGroup() {
        return enemyGroup;
    }
}

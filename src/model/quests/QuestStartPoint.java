package model.quests;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class QuestStartPoint extends QuestDecisionPoint {
    private final Sprite32x32 SPRITE = new Sprite32x32("startpoint", "quest.png", 0x02, MyColors.BLACK, MyColors.WHITE, MyColors.RED);

    public QuestStartPoint(List<QuestEdge> connections, String leaderTalk) {
        super(0, 0, connections, leaderTalk);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }
}

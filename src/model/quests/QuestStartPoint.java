package model.quests;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class QuestStartPoint extends QuestDecisionPoint {
    private final Sprite32x32 SPRITE = new Sprite32x32("startpoint", "quest.png", 0x02, MyColors.BLACK, MyColors.WHITE, MyColors.GOLD);

    public QuestStartPoint(List<QuestNode> connections) {
        super(0, 0);
        for (QuestNode q : connections) {
            connectTo(q);
        }
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }
}

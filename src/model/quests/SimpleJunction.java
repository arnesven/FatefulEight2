package model.quests;

import model.Model;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class SimpleJunction extends QuestJunction {
    private static final Sprite32x32 SPRITE = new Sprite32x32("simplejunc", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);

    public SimpleJunction(int col, int row, QuestEdge edge) {
        super(col, row);
        getConnections().add(edge);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public String getDescription() {
        return "Simple Junction";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        return getConnections().get(0);
    }
}

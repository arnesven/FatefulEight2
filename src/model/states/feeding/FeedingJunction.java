package model.states.feeding;

import model.Model;
import model.quests.QuestEdge;
import model.quests.QuestJunction;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

abstract class FeedingJunction extends QuestJunction {
    private final Sprite32x32 SPRITE = new Sprite32x32("simplejunc", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);

    public FeedingJunction(int col, int row, List<QuestEdge> questEdges) {
        super(col, row);
        for (QuestEdge edge : questEdges) {
            connectTo(edge);
        }
    }

    protected abstract QuestEdge specificDoAction(Model model, GameState state);

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public String getDescription() {
        return "Unused";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        return null; // unused
    }

    @Override
    public QuestEdge doAction(Model model, GameState state) {
        return specificDoAction(model, state);
    }
}

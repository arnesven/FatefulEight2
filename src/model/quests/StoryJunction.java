package model.quests;

import model.Model;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public abstract class StoryJunction extends SimpleJunction {
    private static final Sprite32x32 SPRITE = new Sprite32x32("storyjunc", "quest.png", 0x0C,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BROWN);

    public StoryJunction(int col, int row, QuestEdge edge) {
        super(col, row, edge);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        doAction(model, state);
        return super.run(model, state);
    }

    protected abstract void doAction(Model model, QuestState state);
}

package model.quests;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public abstract class ConditionSubScene extends QuestSubScene {

    private static final Sprite32x32 SPRITE = new Sprite32x32("conditionsubscene", "quest.png", 0x25,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    public ConditionSubScene(int col, int row) {
        super(col, row);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.LIGHT_GREEN;
    }

    @Override
    public String getDetailedDescription() {
        return "???";
    }
}

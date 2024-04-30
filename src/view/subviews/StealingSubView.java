package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import view.BorderFrame;
import view.MyColors;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class StealingSubView extends TopMenuSubView {
    private final SteppingMatrix<Item> matrix;
    private int bounty;

    public StealingSubView(List<Item> shopInventory) {
        super(5, new int[]{X_OFFSET + 12});
        this.matrix = new SteppingMatrix<Item>(8, 8);
        matrix.addElements(shopInventory);
    }

    @Override
    protected String getUnderText(Model model) {
        Item it = getSelectedItem();
        return it.getName() + " " + it.getCost() + " gold, " +
                it.getWeight() / 1000.0 + " kg" +
                it.getShoppingDetails();
    }

    public Item getSelectedItem() {
        return matrix.getSelectedElement();
    }

    @Override
    protected String getTitleText(Model model) {
        return "STEALING";
    }

    @Override
    protected void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = new Point(matrix.getSelectedPoint());
            p.x = X_OFFSET + p.x * 4;
            p.y = Y_OFFSET + p.y * 4 - 2;
            model.getScreenHandler().register("recruitcursor", p, cursor, 2);
        }
    }

    @Override
    protected void drawInnerArea(Model model) {
        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                Item it = matrix.getElementAt(col, row);
                int xPos = X_OFFSET + col * 4;
                int yPos = Y_OFFSET + row * 4 + 2;
                if (it != null) {
                    it.drawYourself(model.getScreenHandler(), xPos, yPos);
                } else {
                    model.getScreenHandler().put(xPos, yPos, Item.EMPTY_ITEM_SPRITE);
                }
            }
        }

        String difficulty = CollectiveSkillCheckSubScene.getDifficultyString(bounty / 2);
        MyColors color = MyColors.GREEN;
        if (difficulty.equals("HARD")) {
            color = MyColors.RED;
        } else if (difficulty.equals("MEDIUM")) {
            color = MyColors.YELLOW;
        }
        BorderFrame.drawString(model.getScreenHandler(), "Escape - Collective Sneak", X_OFFSET,
                Y_OFFSET+1, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), difficulty, X_OFFSET+26,
                Y_OFFSET+1, color, MyColors.BLACK);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        return "DONE";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    public void removeItem(Item it) {
        matrix.remove(it);
    }

    public void setBounty(int bounty) {
        this.bounty = bounty;
    }
}

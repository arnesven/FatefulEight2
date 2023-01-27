package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.List;

public class ShopSubView extends SubView {
    private final String seller;
    private final Map<Item, Integer> priceMap;
    private SteppingMatrix<Item> matrix;
    private String title;
    private Sprite crossSprite = new Sprite32x32("crosssprite", "combat.png", 0x00, MyColors.BLACK, MyColors.CYAN, MyColors.RED);

    public ShopSubView(SteppingMatrix<Item> items, String title, String seller,
                       Map<Item, Integer> prices) {
        super(3);
        this.matrix = items;
        this.title = title;
        this.seller = seller;
        this.priceMap = prices;
    }

    @Override
    protected void drawArea(Model model) {
        BorderFrame.drawCentered(model.getScreenHandler(), title, 4,
                title.equals("BUYING") ? MyColors.YELLOW: MyColors.LIGHT_BLUE);
        List<Item> inventory = model.getParty().getInventory().getAllItems();
        for (int row = 0; row < matrix.getRows(); row++) {
            for (int col = 0; col < matrix.getColumns(); col++) {
                Item it = matrix.getElementAt(col, row);
                int xPos = X_OFFSET + col * 4;
                int yPos = Y_OFFSET + row * 4 + 2;
                if (it != null) {
                    it.drawYourself(model.getScreenHandler(), xPos, yPos);
                    if ((title.equals("BUYING") && priceMap.get(it) > model.getParty().getGold()) ||
                            (title.equals("SELLING") && !inventory.contains(it))) {
                        model.getScreenHandler().register("crossedout", new Point(xPos, yPos), crossSprite);
                    }
                } else {
                    model.getScreenHandler().put(xPos, yPos, Item.EMPTY_ITEM_SPRITE);
                }
            }
        }

        drawCursor(model);
    }


    private void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = new Point(matrix.getSelectedPoint());
            p.x = X_OFFSET + p.x * 4;
            p.y = Y_OFFSET + p.y * 4 - 2;
            model.getScreenHandler().register("recruitcursor", p, cursor, 2);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        Item it = matrix.getSelectedElement();
        if (it != null) {
            int cost = it.getCost();
            if (title.equals("BUYING")) {
                cost = priceMap.get(it);
            }
            return it.getName() + " " + cost + " gold" + it.getShoppingDetails();
        }
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "SHOP - " + seller.toUpperCase();
    }

    public void setText(String text) {
        this.title = text;
    }

    public void setContent(SteppingMatrix<Item> content) {
        this.matrix = content;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }
}

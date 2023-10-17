package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import model.states.ShopState;
import view.sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class ShopSubView extends TopMenuSubView {
    private final String seller;
    private final Map<Item, Integer> priceMap;
    private final ShopState state;
    private SteppingMatrix<Item> matrix;
    private boolean isBuying;
    private Sprite crossSprite = new Sprite32x32("crosssprite", "combat.png", 0x00, MyColors.BLACK, MyColors.CYAN, MyColors.RED);
    private boolean overflow = false;

    public ShopSubView(SteppingMatrix<Item> items, boolean isBuying, String seller,
                       Map<Item, Integer> prices, ShopState state) {
        super(5, new int[]{X_OFFSET + 3, X_OFFSET+13, X_OFFSET+24});
        this.matrix = items;
        this.isBuying = isBuying;
        this.seller = seller;
        this.priceMap = prices;
        this.state = state;
        setTopCursorIndex(getDefaultIndex());
    }

    protected int getDefaultIndex() {
        return isBuying ? 0:1;
    }

    @Override
    protected void drawInnerArea(Model model) {
        if (!isBuying) {
            checkSellIntegrity(model);
        }

        for (int row = 0; row < matrix.getRows(); row++) {
            for (int col = 0; col < matrix.getColumns(); col++) {
                Item it = matrix.getElementAt(col, row);
                int xPos = X_OFFSET + col * 4;
                int yPos = Y_OFFSET + row * 4 + 2;
                if (it != null) {
                    it.drawYourself(model.getScreenHandler(), xPos, yPos);
                    if ((isBuying && priceMap.get(it) > model.getParty().getGold())) {
                        model.getScreenHandler().register("crossedout", new Point(xPos, yPos), crossSprite);
                    }
                } else {
                    model.getScreenHandler().put(xPos, yPos, Item.EMPTY_ITEM_SPRITE);
                }
            }
        }

        if (overflow) {
            int xPos = X_MAX - 19;
            int yPos = Y_OFFSET + (matrix.getRows()) * 4 + 3;
            BorderFrame.drawString(model.getScreenHandler(),"All items not shown",  xPos, yPos,
                    MyColors.RED, MyColors.BLACK);
        }
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        if (i == 0) {
            return MyColors.YELLOW;
        }
        if (i == 2) {
            return MyColors.LIGHT_RED;
        }
        return state.maySell(model)?MyColors.LIGHT_BLUE:MyColors.GRAY;
    }

    @Override
    protected String getTitle(int i) {
        switch (i) {
            case 0 : return "BUY";
            case 1 : return "SELL";
            default : return "EXIT";
        }
    }

    private void checkSellIntegrity(Model model) {
        boolean integrityOk = true;
        overflow = false;
        List<Item> sellableItems = model.getParty().getInventory().getAllItems();
        if (sellableItems.size() > matrix.getColumns()*matrix.getRows()) {
            sellableItems = sellableItems.subList(0, matrix.getColumns()*matrix.getRows());
            overflow = true;
        }
        for (Item it : sellableItems) {
            if (!matrix.getElementList().contains(it)) {
                integrityOk = false;
            }
        }
        if (integrityOk) {
            for (Item it : matrix.getElementList()) {
                if (!sellableItems.contains(it)) {
                    integrityOk = false;
                }
            }
        }
        if (!integrityOk) {
            List<Item> items = new ArrayList<>();
            items.addAll(matrix.getElementList());
            for (Item it : items) {
                matrix.remove(it);
            }
            matrix.addElements(sellableItems);
        }
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
    protected String getUnderText(Model model) {
        Item it = matrix.getSelectedElement();
        if (it != null) {
            int cost = it.getCost();
            if (isBuying) {
                cost = priceMap.get(it);
            } else {
                cost /= 2;
            }
            return it.getName() + " " + cost + " gold, " +
                    it.getWeight() / 1000.0 + " kg" +
                    it.getShoppingDetails();
        }
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "SHOP - " + seller.toUpperCase();
    }

    public void setIsBuying(boolean isBuying) {
        this.isBuying = isBuying;
        setTopCursorIndex(getDefaultIndex());
    }

    public void setContent(SteppingMatrix<Item> content) {
        this.matrix = content;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    public void setOverflowWarning(boolean b) {
        this.overflow = b;
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }
}

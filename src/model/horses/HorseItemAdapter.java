package model.horses;

import model.items.Inventory;
import model.items.Item;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;

public class HorseItemAdapter extends Item {
    private final Horse horse;

    public HorseItemAdapter(Horse horse) {
        super(horse.getName(), horse.getCost());
        this.horse = horse;
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        Point horsePos = new Point(col - 2, row);

        screenHandler.clearSpace(horsePos.x, horsePos.x + 8, horsePos.y, horsePos.y + 8);
        screenHandler.put(horsePos.x, horsePos.y, Horse.getBackgroundSprite());
        screenHandler.register(getSprite().getName(), horsePos, getSprite());
    }

    @Override
    protected Sprite getSprite() {
        return horse.getSprite();
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        throw new IllegalStateException("Should not add horse to inventory.");
    }

    @Override
    public String getShoppingDetails() {
        return ", Type: " + horse.getType() + ", " + horse.getInfo();
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Should not copy horse item adapter");
    }

    @Override
    public String getSound() {
        throw new IllegalStateException("Should not call getSound on horse item adapter.");
    }

    @Override
    public int getSpriteSize() {
        return 8;
    }
}

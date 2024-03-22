package model.items;

import model.characters.TamedDragonCharacter;
import model.horses.Horse;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;

public class TamedDragonItemAdapter extends Item {

    private final TamedDragonCharacter dragon;

    public TamedDragonItemAdapter(TamedDragonCharacter dragon) {
        super(dragon.getName(), 0);
        this.dragon = dragon;
    }

    @Override
    protected Sprite getSprite() {
        return dragon.getInventorySprite();
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        Point dragonPos = new Point(col - 2, row);

        screenHandler.clearSpace(dragonPos.x, dragonPos.x + 8, dragonPos.y, dragonPos.y + 8);
        screenHandler.put(dragonPos.x, dragonPos.y, Horse.getBackgroundSprite());
        screenHandler.register(getSprite().getName(), dragonPos, getSprite());
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        throw new IllegalStateException("Should not be added to inventory");
    }

    @Override
    public String getShoppingDetails() {
        return "TODO";
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Should not be copied!");
    }

    @Override
    public String getSound() {
        throw new IllegalStateException("Should not get sound for this class!");
    }

    @Override
    public int getSpriteSize() {
        return 8;
    }
}

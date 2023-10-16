package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ZeppelinFuel extends Potion {

    private static final Sprite SPRITE = new ItemSprite(13, 6, MyColors.WHITE, MyColors.ORC_GREEN);

    public ZeppelinFuel() {
        super("Zeppelin Fuel", 20);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", enough for one trip with the zeppelin.";
    }

    @Override
    public Item copy() {
        return new ZeppelinFuel();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        return "The zeppelin fuel can not be used right now.";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return false;
    }

    @Override
    public int getWeight() {
        return 500;
    }
}

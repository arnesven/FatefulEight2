package model.items.special;

import model.items.Item;
import model.items.weapons.Weapon;
import model.ruins.objects.FatueKeyObject;
import util.MyStrings;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class FatueKeyItem extends StoryItem {
    private final MyColors color;
    private final ItemSprite sprite;

    public FatueKeyItem(MyColors color) {
        super(MyStrings.capitalize(color.name()).replace("_", " ") + " Key", 0);
        this.color = color;
        this.sprite = new ItemSprite(11, 13, FatueKeyObject.getHighlightColor(color), color, MyColors.DARK_GRAY);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return 50;
    }

    @Override
    public String getShoppingDetails() {
        return ", a key you found in the Fortress at the Ultimate Edge.";
    }

    @Override
    public Item copy() {
        return new FatueKeyItem(color);
    }

    public MyColors getColor() {
        return color;
    }
}

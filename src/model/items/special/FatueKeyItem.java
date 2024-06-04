package model.items.special;

import model.Model;
import model.items.Item;
import model.ruins.objects.FatueKeyObject;
import util.MyLists;
import util.MyStrings;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class FatueKeyItem extends StoryItem {
    private final MyColors color;
    private final ItemSprite sprite;

    public FatueKeyItem(MyColors color) {
        super(toName(color) + " Key", 0);
        this.color = color;
        this.sprite = new ItemSprite(11, 13, FatueKeyObject.getHighlightColor(color), color, MyColors.DARK_GRAY);
    }

    private static String toName(MyColors color) {
        if (color == MyColors.GOLD) {
            return "Gold";
        }
        if (color == MyColors.DARK_RED) {
            return "Red";
        }
        if (color == MyColors.DARK_GREEN) {
            return "Jade";
        }
        if (color == MyColors.GRAY) {
            return "Silver";
        }
        if (color == MyColors.BROWN) {
            return "Bronze";
        }
        return "Azure";
    }

    public static boolean hasKey(Model model, MyColors color) {
        return !MyLists.filter(model.getParty().getInventory().getStoryItems(),
                (StoryItem si) -> si instanceof FatueKeyItem && ((FatueKeyItem) si).getColor() == color).isEmpty();
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

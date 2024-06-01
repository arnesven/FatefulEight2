package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class PieceOfStaffItem extends StoryItem {

    private static final Sprite SPRITE = new ItemSprite(10, 13, MyColors.DARK_RED, MyColors.GRAY, MyColors.DARK_GRAY);

    public PieceOfStaffItem() {
        super("Piece of Staff", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 200;
    }

    @Override
    public String getShoppingDetails() {
        return ", a strange piece of staff found in the Fortress at the Ultimate Edge. " +
                "You think it may be part of a key to unlock a door in the fortress.";
    }

    @Override
    public Item copy() {
        return new PieceOfStaffItem();
    }
}

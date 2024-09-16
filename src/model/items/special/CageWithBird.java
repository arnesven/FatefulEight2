package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CageWithBird extends StoryItem implements CollectorItem {

    private static final Sprite SPRITE = new ItemSprite(15, 14, MyColors.LIGHT_YELLOW, MyColors.ORANGE, MyColors.RED);

    public CageWithBird() {
        super("Cage with Bird", 500);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 3500;
    }

    @Override
    public String getShoppingDetails() {
        return "A cage with a rare bird in it.";
    }

    @Override
    public Item copy() {
        return new CageWithBird();
    }
}

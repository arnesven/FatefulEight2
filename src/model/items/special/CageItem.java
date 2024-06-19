package model.items.special;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CageItem extends StoryItem {
    private static final Sprite SPRITE = new ItemSprite(15, 13);

    public CageItem() {
        super("Birdcage", 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 3000;
    }

    @Override
    public String getShoppingDetails() {
        return "Useful for keeping a bird from flying away";
    }

    @Override
    public Item copy() {
        return new CageItem();
    }
}

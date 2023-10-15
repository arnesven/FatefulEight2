package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class IngredientsDummyItem extends InventoryDummyItem {

    private static final Sprite SPRITE = new ItemSprite(14, 7, MyColors.WHITE,
            MyColors.LIGHT_GREEN, MyColors.CYAN);

    public IngredientsDummyItem(int ingredients) {
        super("Ingredients (" + ingredients + ")", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Components which you can make alchemical drafts. This includes spices herbs " +
                "tonics minerals and animal parts.";
    }
}

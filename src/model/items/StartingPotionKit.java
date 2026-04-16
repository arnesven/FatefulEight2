package model.items;

import model.items.potions.RejuvenationPotion;
import model.items.potions.StaminaPotion;
import view.sprites.Sprite;

public class StartingPotionKit extends InventoryDummyItem {
    private final Sprite sprite;

    public StartingPotionKit() {
        super("Potions Kit", 16);
        this.sprite = RejuvenationPotion.SPRITE;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        new RejuvenationPotion().addYourself(inventory);
        new StaminaPotion().addYourself(inventory);
    }

    @Override
    public String getShoppingDetails() {
        return ", " + getDescription();
    }


    @Override
    public String getDescription() {
        return "Rejuvenation and Stamina Potion.";
    }


    @Override
    public Item copy() {
        return new StartingPotionKit();
    }
}

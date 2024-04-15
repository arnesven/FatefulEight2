package model.items;

import model.items.potions.Potion;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class PotionRecipe extends WeightlessItem {
    private static final Sprite SPRITE = new ItemSprite(9, 5, MyColors.WHITE, MyColors.BEIGE);
    private final Potion brewable;

    public PotionRecipe(Potion brewable) {
        super("Recipe '" + makeAbbreviation(brewable.getName()) + "'", 10);
        this.brewable = brewable;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getShoppingDetails() {
        return ", a recipe for '" + brewable.getName() + "'.";
    }

    @Override
    public Item copy() {
        return new PotionRecipe(brewable);
    }

    @Override
    public String getSound() {
        return "paper";
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    public Potion getBrewable() {
        return brewable;
    }
}

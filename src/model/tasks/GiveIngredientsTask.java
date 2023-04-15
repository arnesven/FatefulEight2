package model.tasks;

import model.Model;
import model.Summon;
import model.map.UrbanLocation;

public class GiveIngredientsTask extends GiveResourceTask {
    private static final String FIRST_PART = "Our druid is currently indisposed and we badly need to replenish our stores of magical potions, ";

    public GiveIngredientsTask(Summon summon, Model model, UrbanLocation location) {
        super(model, summon, location, "ingredients", 25, FIRST_PART);
    }

    @Override
    protected int getResource(Model model) {
        return model.getParty().getInventory().getIngredients();
    }

    @Override
    protected void reduceResource(Model model, int amount) {
        model.getParty().getInventory().addToIngredients(-amount);
    }
}

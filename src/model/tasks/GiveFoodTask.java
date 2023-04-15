package model.tasks;

import model.Model;
import model.Summon;
import model.map.UrbanLocation;

public class GiveFoodTask extends GiveResourceTask {
    private static final String FIRST_PART = "We've had a terrible winter and our food stocks are almost depleted. We desperately need to replenish them, ";

    public GiveFoodTask(Summon summon, Model model, UrbanLocation location) {
        super(model, summon, location, "rations", 150, FIRST_PART);
    }

    @Override
    protected int getResource(Model model) {
        return model.getParty().getFood();
    }

    @Override
    protected void reduceResource(Model model, int amount) {
        model.getParty().addToFood(-amount);
    }
}

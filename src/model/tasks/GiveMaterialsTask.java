package model.tasks;

import model.Model;
import model.Summon;
import model.map.UrbanLocation;

public class GiveMaterialsTask extends GiveResourceTask {
    private static final String FIRST_PART = "We are currently in the process of restoring some of our oldest buildings, ";

    public GiveMaterialsTask(Summon summon, Model model, UrbanLocation location) {
        super(model, summon, location, "materials", 25, FIRST_PART);
    }

    @Override
    protected int getResource(Model model) {
        return model.getParty().getInventory().getMaterials();
    }

    @Override
    protected void reduceResource(Model model, int amount) {
        model.getParty().getInventory().addToMaterials(-amount);
    }
}

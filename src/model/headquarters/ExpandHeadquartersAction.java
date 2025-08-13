package model.headquarters;

import model.Model;
import model.states.GameState;

public class ExpandHeadquartersAction extends HeadquartersAction {
    public static final int EXPAND_COST_MATERIALS = 50;
    public static final int EXPAND_COST_GOLD = 20;

    public ExpandHeadquartersAction(Model model) {
        super(model, "Expand");
    }

    @Override
    public GameState run(Model model) {
        expand(model);
        return null;
    }

    private void expand(Model model) {
        println("The cost to expand your current headquarters is " + EXPAND_COST_MATERIALS +
                " materials and " + EXPAND_COST_GOLD + " gold." );
        if (model.getParty().getInventory().getMaterials() +
                model.getParty().getHeadquarters().getMaterials() < EXPAND_COST_MATERIALS) {
            println("You lack the building materials.");
            return;
        }
        if (model.getParty().getGold() + model.getParty().getHeadquarters().getGold() < EXPAND_COST_GOLD) {
            println("You lack the gold.");
            return;
        }

        print("Do you want to spend the resources to raise the size of your headquarters by one? (Y/N)");
        if (!yesNoInput()) {
            return;
        }

        int hqMatLoss = Math.min(EXPAND_COST_MATERIALS, model.getParty().getHeadquarters().getMaterials());
        model.getParty().getHeadquarters().addToMaterials(-hqMatLoss);
        model.getParty().getInventory().addToMaterials(hqMatLoss - EXPAND_COST_MATERIALS);

        int hqGoldLoss = Math.min(EXPAND_COST_GOLD, model.getParty().getHeadquarters().getGold());
        model.getParty().getHeadquarters().addToGold(-hqGoldLoss);
        model.getParty().spendGold(EXPAND_COST_GOLD - hqGoldLoss);

        model.getParty().getHeadquarters().incrementSize();
        println("You have expanded your headquarters! " + model.getParty().getHeadquarters().presentYourself());
    }
}

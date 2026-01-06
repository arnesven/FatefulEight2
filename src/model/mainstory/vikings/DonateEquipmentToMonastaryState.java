package model.mainstory.vikings;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import model.states.TransferItemState;

import java.util.HashMap;
import java.util.List;

public class DonateEquipmentToMonastaryState extends TransferItemState {
    public DonateEquipmentToMonastaryState(Model model, List<Item> donatedItems) {
        super(model, "SIXTH MONK", donatedItems);
    }

    @Override
    protected void itemJustSold(Model model, Item it, SteppingMatrix<Item> buyItems, HashMap<Item, Integer> prices) {
        if (GainSupportOfVikingsTask.canItemBeDonated(it)) {
            super.itemJustSold(model, it, buyItems, prices);
        } else {
            println("The Sixth Monk does not want " + it.getName() + ".");
            it.addYourself(model.getParty().getInventory());
        }
    }
}

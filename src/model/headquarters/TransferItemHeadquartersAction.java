package model.headquarters;

import model.Model;
import model.items.Item;
import model.states.GameState;
import model.states.TransferItemState;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

import java.util.List;

public class TransferItemHeadquartersAction extends HeadquartersAction {
    private final SubView previous;

    public TransferItemHeadquartersAction(Model model, SubView previous) {
        super(model, "Transfer items");
        this.previous = previous;
    }

    @Override
    public GameState run(Model model) {
        transferItems(model, this, previous);
        return null;
    }

    private void transferItems(Model model, GameState state, SubView previous) {
        List<Item> stashItems = model.getParty().getHeadquarters().getItems();
        TransferItemState shop = new TransferItemState(model, "HEADQUARTERS", stashItems);
        if (stashItems.isEmpty() && shop.getSellableItems(model).isEmpty()) {
            state.println("You have no items to stash or retrieve from headquarters.");
            return;
        }
        if (stashItems.isEmpty()) {
            shop.setSellingMode(model);
        }
        shop.run(model);
        CollapsingTransition.transition(model, previous);
    }
}

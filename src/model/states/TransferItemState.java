package model.states;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import view.subviews.ShopSubView;
import view.subviews.TransferItemSubView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransferItemState extends ShopState {
    private final List<Item> stashItems;

    public TransferItemState(Model model, String name, List<Item> stashItems) {
        super(model, name, stashItems, new int[stashItems.size()], new boolean[]{false});
        this.stashItems = stashItems;
    }

    protected ShopSubView makeSubView(SteppingMatrix<Item> buyItems, boolean isBuying,
                                      String seller, HashMap<Item, Integer> prices, ShopState shopState) {
        return new TransferItemSubView(buyItems, true, seller, prices, this);
    }

    @Override
    protected String getAcquireVerb() {
        return "Take";
    }

    @Override
    protected String getRelinquishVerb() {
        return "Give";
    }

    protected void itemJustSold(Model model, Item it, SteppingMatrix<Item> buyItems, HashMap<Item, Integer> prices) {
        println("You gave away " + it.getName() + ".");
        stashItems.add(it);
        if (!buyItems.isFull()) {
            buyItems.addElementLast(it);
            prices.put(it, 0);
        } else {
            ((ShopSubView)model.getSubView()).setOverflowWarning(true);
        }
    }
}

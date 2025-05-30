package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import model.states.TransferItemState;

import java.util.HashMap;

public class TransferItemSubView extends ShopSubView {
    private final String seller;

    public TransferItemSubView(SteppingMatrix<Item> buyItems, boolean isBuying, String seller,
                               HashMap<Item, Integer> prices, TransferItemState transferItemState) {
        super(buyItems, isBuying, seller, prices, transferItemState, 0);
        this.seller = seller;
    }

    @Override
    protected String getTitle(int i) {
        switch (i) {
            case 0 : return "TAKE";
            case 1 : return "GIVE";
            default : return "EXIT";
        }
    }

    @Override
    protected String getTitleText(Model model) {
        return "TRANSFER - " + seller.toUpperCase();
    }

    protected String getItemInfo(Item it) {
        return it.getName() + " " +
                it.getWeight() / 1000.0 + " kg" +
                it.getShoppingDetails();
    }
}

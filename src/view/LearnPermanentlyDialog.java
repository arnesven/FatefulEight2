package view;

import model.Model;
import model.items.Item;

public class LearnPermanentlyDialog extends YesNoMessageView {
    private final Item item;
    private final GameView outerView;
    private final boolean disposeOuter;

    public LearnPermanentlyDialog(GameView outerView, Item itemToLearn, String verb, boolean disposeOuter) {
        super(outerView, "Are you sure you want to learn " + itemToLearn.getName() + " permanently? " +
                "This will remove it from your inventory but your party " +
                "members will still be able to " + verb + " it.");
        this.item = itemToLearn;
        this.outerView = outerView;
        this.disposeOuter = disposeOuter;
    }

    @Override
    protected void doAction(Model model) {
        model.getParty().getInventory().remove(item);
        model.getParty().permanentlyLearn(item);
        if (disposeOuter) {
            outerView.setTimeToTransition(true);
        }
    }
}

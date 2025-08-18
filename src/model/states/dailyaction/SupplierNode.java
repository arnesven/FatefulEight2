package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.dailyaction.shops.ShopSupplier;
import util.MyRandom;
import util.MyStrings;
import view.sprites.Sprite;
import view.subviews.ShopInteriorSubView;

import java.awt.*;
import java.util.List;

public class SupplierNode extends DailyActionNode {
    private static final Point CURSOR_SHIFT = new Point(+4, -4);
    private final ShopSupplier supplier;

    public SupplierNode(ShopSupplier supplier) {
        super("Supplier");
        this.supplier = supplier;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new InteractWIthSupplierState(model, supplier);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        supplier.drawYourself(model, p);
    }

    @Override
    public Point getCursorShift() {
        return CURSOR_SHIFT;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private static class InteractWIthSupplierState extends GameState {
        private final ShopSupplier supplier;

        public InteractWIthSupplierState(Model model, ShopSupplier supplier) {
            super(model);
            this.supplier = supplier;
        }

        @Override
        public GameState run(Model model) {
            String itemName = supplier.getItem().getName();
            if (!itemName.endsWith("s")) {
                itemName += "s";
            }
            if (!supplier.isActivated()) {
                supplierSay("I'm a supplier for this shop. Just dropped off a shipment of " + itemName + ".");
                leaderSay(MyRandom.sample(List.of("How nice for you.", "Good for you.", "Okay.", "...")));
                return null;
            }

            if (supplier.isDealMade()) {
                supplierSay("Thanks again for taking those " + itemName + " off my hands!");
                return null;
            }

            println("The supplier looks upset.");
            leaderSay("What's the matter. Don't like the prices?");
            supplierSay("What? Oh, no I'm not a customer. I supply goods to this shop. " +
                    "I'm a little annoyed at the moment. The shopkeeper just backed out of a deal on me.");
            leaderSay("What's the deal?");
            boolean shopKeeperGender = MyRandom.flipCoin();
            supplierSay("I was contracted to procure " + itemName + " for this shop. " +
                    "I seem to remember the agreement was " + MyStrings.numberWord(supplier.getNumber()) +
                    " of them for " + supplier.getGoldAmount() + " gold. I've been through some trouble to get them actually. " +
                    "Now that I've delivered them here, the shopkeeper doesn't want to accept them! " +
                    heOrSheCap(shopKeeperGender) + " says " + heOrShe(shopKeeperGender) + " won't be able to sell them, " +
                    "that there's too little demand.");
            if (model.getParty().getGold() >= supplier.getGoldAmount()) {
                print("Do you want to buy the supplier's shipment? (Y/N) ");
                if (yesNoInput()) {
                    supplier.setDealMade(true);
                    ShopSupplier.setDealOnDay(model);
                    completeAchievement(ShopSupplier.ACHIEVEMENT_KEY);
                    leaderSay("I'll buy those " + itemName + " from you.");
                    supplierSay("You will? That's fantastic! You're really helping me out of a tight " +
                            "spot here. Here you go.");
                    println("You received " + supplier.getNumber() + " " + itemName + ".");
                    for (int i = 0; i < supplier.getNumber(); i++) {
                        supplier.getItem().copy().addYourself(model.getParty().getInventory());
                    }
                    println("You give the supplier " + supplier.getGoldAmount() + " gold.");
                    model.getParty().spendGold(supplier.getGoldAmount());
                    return null;
                }
            }
            leaderSay(MyRandom.sample(List.of("Sorry. I'm afraid I can't help you.",
                    "Maybe you can try another shop?", "Merchants are fickle folk.",
                    "Sounds like a rough business.", "Make your agreements in ink in the future.",
                    "How annoying!")));
            println("The supplier grumbles.");
            return null;
        }

        private void supplierSay(String line) {
            if (getModel().getSubView() instanceof ShopInteriorSubView) {
                ((ShopInteriorSubView)getModel().getSubView()).addCalloutAtSupplier(line.length());
            }
            printQuote("Customer", line);
        }
    }
}

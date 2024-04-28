package model.states;

import model.Model;
import model.items.FoodDummyItem;
import model.items.Item;
import model.items.ObolsDummyItem;

import java.util.ArrayList;
import java.util.List;

public class TradeWithBartenderState extends ShopState {
    private ObolsDummyItem sellableObols = null;

    public TradeWithBartenderState(Model model) {
        super(model, "Bartender", makeStock(), makeStockPrices());
        refreshObols();
    }

    private void refreshObols() {
        this.sellableObols = new ObolsDummyItem(getModel().getParty().getInventory().getObols());
    }

    private static int[] makeStockPrices() {
        return new int[]{1, 1};
    }

    private static List<Item> makeStock() {
        List<Item> stock = new ArrayList<>(List.of(new ObolsDummyItem(10),
                new FoodDummyItem(5)));
        return stock;
    }

    @Override
    public List<Item> getSellableItems(Model model) {
        List<Item> items = super.getSellableItems(model);
        if (model.getParty().getInventory().getObols() > 0 && sellableObols != null) {
            items.add(sellableObols);
        }
        return items;
    }

    @Override
    protected boolean sellThisItem(Model model, Item it) {
        if (!(it instanceof ObolsDummyItem)) {
            return super.sellThisItem(model, it);
        }
        sellObols(model);
        refreshObols();
        return false;
    }

    @Override
    protected boolean purchaseItem(Model model, Item it, int xPos, int yPos) {
        if (it instanceof ObolsDummyItem) {
            buyObols(model);
            refreshObols();
            return false;
        }
        return super.purchaseItem(model, it, xPos, yPos);
    }

    private void sellObols(Model model) {
        int take = model.getParty().getObols() / 10;
        if (take == 0) {
            printQuote("Bartender", "Sorry, I only take 10 obols at a time.");
            return;
        }
        printQuote("Bartender", "Want to cash in some obols? I can take " + take*10 +
                " of them off your hands and give you " + take + " gold.");
        print("Do you accept? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().addToObols(-take*10);
            model.getParty().addToGold(take);
            println("You now have " + model.getParty().getObols() + " obols.");
        }
    }

    private void buyObols(Model model) {
        model.getTutorial().obols(model);
        int spend = 0;
        do {
            printQuote("Bartender", "Wanna buy Obols? You get 10 obols for each gold. How much would you like to spend? ");
            try {
                spend = Integer.parseInt(lineInput());
                if (spend < 0) {
                    println("You cannot spend negative gold.");
                } else if (spend > model.getParty().getGold()) {
                    println("You cannot afford that.");
                } else {
                    break;
                }
            } catch (NumberFormatException nfe) {
                println("Please enter an integer.");
            }
        } while (true);
        print("You bought " + spend*10 + " obols.");
        model.getParty().addToObols(spend*10);
        println(" You now have " + model.getParty().getObols() + " obols.");
        model.getParty().addToGold(-spend);
    }
}

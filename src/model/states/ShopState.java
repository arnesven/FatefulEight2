package model.states;

import model.Model;
import model.SteppingMatrix;
import model.items.*;
import view.subviews.CollapsingTransition;
import view.subviews.ShopSubView;

import java.util.*;

public class ShopState extends GameState {

    private final ShopSubView subView;
    private final String seller;
    private HashMap<Item, Integer> prices;
    private SteppingMatrix<Item> buyItems;
    private SteppingMatrix<Item> sellItems;
    private boolean showingBuyItems = true;
    private boolean sellingEnabled = true;

    public ShopState(Model model, String seller, List<Item> itemsForSale, int[] specialPrices) {
        super(model);
        buyItems = new SteppingMatrix<>(8, 8);
        sellItems = new SteppingMatrix<>(8, 8);
        this.seller = seller;
        buyItems.addElements(itemsForSale);
        sellItems.addElements(model.getParty().getInventory().getAllItems());
        makePricesMap(itemsForSale, specialPrices);
        this.subView = new ShopSubView(buyItems, "BUYING", seller, prices);
    }

    private void makePricesMap(List<Item> itemsForSale, int[] specialPrices) {
        prices = new HashMap<>();
        int i = 0;
        for (Item it : itemsForSale) {
            if (specialPrices != null) {
                prices.put(it, specialPrices[i]);
                i++;
            } else {
                prices.put(it, it.getCost());
            }
        }
    }

    @Override
    public GameState run(Model model) {
        CollapsingTransition.transition(model, subView);
        while (true) {
            if (sellingEnabled && model.getParty().getInventory().noOfsellableItems() > 0) {
                print("Do you want to Buy (B), Sell (S) or are you Done (Q)? ");
            } else {
                print("Do you want to Buy (B) or are you Done (Q)? ");
            }
            char selectedAction = lineInput().toUpperCase().charAt(0);
            if (selectedAction == 'Q') {
                break;
            }
            if (selectedAction == 'B') {
                if (!showingBuyItems) {
                    toggleBuySell();
                } else {
                    Item it = buyItems.getSelectedElement();
                    int cost = prices.get(it);
                    if (cost > model.getParty().getGold()) {
                        println("You cannot afford that.");
                    } else {
                        buyItems.remove(it);
                        model.getParty().getInventory().addItem(it);
                        model.getParty().addToGold(-1 * cost);
                        println("You bought " + it.getName() + " for " + cost + " gold.");
                        sellItems.addElementLast(it);
                        if (buyItems.getElementList().isEmpty() && (!sellingEnabled || sellItems.getElementList().isEmpty())) {
                            break;
                        }
                    }
                }
            } else if (selectedAction == 'S' && sellingEnabled && model.getParty().getInventory().noOfsellableItems() > 0) {
                if (showingBuyItems) {
                    toggleBuySell();
                } else {
                    Item it = sellItems.getSelectedElement();
                    sellItems.remove(it);
                    int money = it.getCost() / 2;
                    model.getParty().addToGold(money);
                    println("You sold " + it.getName() + " for " + money + " gold.");
                }
            }
        }
        return new EveningState(model);
    }

    private void toggleBuySell() {
        showingBuyItems = !showingBuyItems;
        if (showingBuyItems) {
            subView.setContent(buyItems);
            subView.setText("BUYING");
        } else {
            subView.setContent(sellItems);
            subView.setText("SELLING");
        }
    }

    public void setSellingEnabled(boolean b) {
        this.sellingEnabled = b;
    }


    public static List<Item> makeGeneralShopInventory(Model model, int commons, int uncommons, int rares) {
        List<Item> shopInventory = new ArrayList<>();
        shopInventory.addAll(model.getItemDeck().draw(commons, Prevalence.common));
        shopInventory.addAll(model.getItemDeck().draw(uncommons, Prevalence.uncommon));
        shopInventory.addAll(model.getItemDeck().draw(rares, Prevalence.rare));
        Collections.sort(shopInventory);
        return shopInventory;
    }
}

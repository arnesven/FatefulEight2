package model.states;

import model.Model;
import model.SteppingMatrix;
import model.items.*;
import sound.SoundEffects;
import view.subviews.ArrowMenuSubView;
import view.subviews.CollapsingTransition;
import view.subviews.ShopSubView;
import view.subviews.SubView;

import java.util.*;

public class ShopState extends GameState {

    private final ShopSubView subView;
    private final String seller;
    private HashMap<Item, Integer> prices;
    private SteppingMatrix<Item> buyItems;
    private SteppingMatrix<Item> sellItems;
    private List<Item> itemsForSale;
    private boolean showingBuyItems = true;
    private boolean sellingEnabled = true;

    public ShopState(Model model, String seller, List<Item> itemsForSale, int[] specialPrices) {
        super(model);
        this.itemsForSale = itemsForSale;
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
            List<String> buySellActions = new ArrayList<>();
            buySellActions.add("Buy");
            if (maySell(model)) {
                buySellActions.add("Sell");
            }
            buySellActions.add("Back");
            buySellActions.add("Done");
            
            model.getTutorial().shopping(model);
            waitForReturnSilently();

            SteppingMatrix<Item> matrixToUse = sellItems;
            if (showingBuyItems) {
                matrixToUse = buyItems;
            }

            int xPos = matrixToUse.getSelectedPoint().x*4 + SubView.X_OFFSET;
            int yPos = matrixToUse.getSelectedPoint().y*4 + 10;
            final char[] selectedAction = new char[]{'x'};
            model.setSubView(new ArrowMenuSubView(model.getSubView(), buySellActions, xPos, yPos, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    if (buySellActions.get(cursorPos).equals("Buy")) {
                        selectedAction[0] = 'B';
                    } else if (buySellActions.get(cursorPos).equals("Sell")) {
                        selectedAction[0] = 'S';
                    } else if (buySellActions.get(cursorPos).equals("Done")) {
                        selectedAction[0] = 'Q';
                    }
                    model.setSubView(getPrevious());
                }
            });
            waitForReturnSilently();
            if (selectedAction[0] == 'Q') {
                break;
            }
            if (selectedAction[0] == 'B') {
                if (!showingBuyItems) {
                    toggleBuySell();
                } else {
                    Item it = buyItems.getSelectedElement();
                    int cost = prices.get(it);
                    if (cost > model.getParty().getGold()) {
                        println("You cannot afford that.");
                    } else {
                        buyItems.remove(it);
                        itemsForSale.remove(it);
                        model.getParty().getInventory().addItem(it);
                        model.getParty().addToGold(-1 * cost);
                        println("You bought " + it.getName() + " for " + cost + " gold.");
                        SoundEffects.playSound(it.getSound());
                        model.getTutorial().equipment(model);
                        sellItems.addElementLast(it);
                        if (buyItems.getElementList().isEmpty()) {
                            if (!sellingEnabled || sellItems.getElementList().isEmpty()) {
                                break;
                            } else {
                                toggleBuySell();
                            }
                        }
                    }
                }
            } else if (selectedAction[0] == 'S' && sellingEnabled && model.getParty().getInventory().noOfsellableItems() > 0) {
                if (showingBuyItems) {
                    toggleBuySell();
                } else {
                    Item it = sellItems.getSelectedElement();
                    if (!isCurrentlyEquipped(model, it)) {
                        sellItems.remove(it);
                        int money = it.getCost() / 2;
                        model.getParty().addToGold(money);
                        model.getParty().getInventory().remove(it);
                        println("You sold " + it.getName() + " for " + money + " gold.");
                        SoundEffects.sellItem();
                        if (sellItems.getElementList().isEmpty()) {
                            if (buyItems.getElementList().isEmpty()) {
                                break;
                            } else {
                                toggleBuySell();
                            }
                        }
                    } else {
                        println("You cannot sell an item that is currently equipped.");
                    }
                }
            }
        }
        return new EveningState(model);
    }

    private boolean maySell(Model model) {
        return sellingEnabled && model.getParty().getInventory().noOfsellableItems() > 0;
    }

    private boolean isCurrentlyEquipped(Model model, Item it) {
        return !model.getParty().getInventory().getAllItems().contains(it);
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

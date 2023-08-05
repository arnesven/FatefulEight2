package model.states;

import model.Model;
import model.SteppingMatrix;
import model.items.*;
import sound.SoundEffects;
import view.SimpleMessageView;
import view.subviews.ArrowMenuSubView;
import view.subviews.CollapsingTransition;
import view.subviews.ShopSubView;
import view.subviews.SubView;

import java.util.*;

public class ShopState extends GameState {

    private final ShopSubView subView;
    private final String seller;
    private boolean warnAboutManyItems = false;
    private HashMap<Item, Integer> prices;
    private SteppingMatrix<Item> buyItems;
    private SteppingMatrix<Item> sellItems;
    private List<Item> itemsForSale;
    private boolean showingBuyItems = true;
    private boolean sellingEnabled = true;
    private boolean mayOnlyBuyOne = false;

    public ShopState(Model model, String seller, List<Item> itemsForSale, int[] specialPrices) {
        super(model);
        this.itemsForSale = itemsForSale;
        buyItems = new SteppingMatrix<>(8, 8);
        sellItems = new SteppingMatrix<>(8, 8);
        this.seller = seller;
        buyItems.addElements(itemsForSale);
        List<Item> itemsToSell = new ArrayList<>();
        itemsToSell.addAll(model.getParty().getInventory().getAllItems());
        if (itemsToSell.size() > sellItems.getColumns() * sellItems.getRows()) {
            itemsToSell = itemsToSell.subList(0, sellItems.getColumns() * sellItems.getRows());
            warnAboutManyItems = true;
        }
        sellItems.addElements(itemsToSell);
        makePricesMap(itemsForSale, specialPrices);
        this.subView = new ShopSubView(buyItems, true, seller, prices, this);
    }

    public static void pressToEnterShop(DailyEventState state) {
        state.print("Press enter to continue.");
        state.waitForReturn();
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
            SteppingMatrix<Item> matrixToUse = sellItems;
            if (showingBuyItems) {
                matrixToUse = buyItems;
            }

            model.getTutorial().shopping(model);
            waitForReturnSilently();
            int topCommand = subView.getTopIndex();
            if (topCommand == 2) {
                break;
            } else if ((topCommand == 1 && showingBuyItems) || topCommand == 0 && !showingBuyItems) {
                toggleBuySell(model);
                continue;
            }

            List<String> buySellActions = new ArrayList<>();
            if (showingBuyItems) {
                buySellActions.add("Buy");
            } else {
                buySellActions.add("Sell");
            }
            if (matrixToUse.getSelectedElement().isAnalyzable()) {
                buySellActions.add("Analyze");
            }
            buySellActions.add("Back");

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
                    } else if (buySellActions.get(cursorPos).equals("Analyze")) {
                        selectedAction[0] = 'A';
                    }
                    model.setSubView(getPrevious());
                }
            });
            waitForReturnSilently();
            if (selectedAction[0] == 'B') {
                Item it = buyItems.getSelectedElement();
                int cost = prices.get(it);
                if (cost > model.getParty().getGold()) {
                    println("You cannot afford that.");
                } else {
                    buyItems.remove(it);
                    itemsForSale.remove(it);
                    model.getParty().getInventory().addItem(it);
                    model.getParty().addToGold(-1 * cost);
                    if (cost > 0) {
                        println("You bought " + it.getName() + " for " + cost + " gold.");
                    } else {
                        println("You received " + it.getName() + ".");
                    }
                    SoundEffects.playSound(it.getSound());
                    model.getTutorial().equipment(model);
                    sellItems.addElementLast(it);
                    if (buyItems.getElementList().isEmpty()) {
                        if (!sellingEnabled || sellItems.getElementList().isEmpty()) {
                            break;
                        } else {
                            toggleBuySell(model);
                        }
                    }
                    if (mayOnlyBuyOne) {
                        break;
                    }
                }
            } else if (selectedAction[0] == 'S' && sellingEnabled && model.getParty().getInventory().noOfsellableItems() > 0) {
                Item it = sellItems.getSelectedElement();
                if (!isCurrentlyEquipped(model, it)) {
                    sellItems.remove(it);
                    int money = it.getCost() / 2;
                    model.getParty().addToGold(money);
                    model.getParty().getInventory().remove(it);
                    println("You sold " + it.getName() + " for " + money + " gold.");
                    SoundEffects.sellItem();
                    if (model.getParty().getInventory().noOfsellableItems() == 0) {
                        if (buyItems.getElementList().isEmpty()) {
                            break;
                        } else {
                            toggleBuySell(model);
                        }
                    }
                } else {
                    println("You cannot sell an item that is currently equipped.");
                }
            } else if (selectedAction[0] == 'A') {
                model.transitionToDialog(matrixToUse.getSelectedElement().getAnalysisDialog(model));
            }
        }
        return new EveningState(model);
    }

    public boolean maySell(Model model) {
        return sellingEnabled && model.getParty().getInventory().noOfsellableItems() > 0;
    }

    private boolean isCurrentlyEquipped(Model model, Item it) {
        return !model.getParty().getInventory().getAllItems().contains(it);
    }

    private void toggleBuySell(Model model) {
        showingBuyItems = !showingBuyItems;
        if (showingBuyItems) {
            subView.setContent(buyItems);
            subView.setIsBuying(true);
        } else {
            subView.setContent(sellItems);
            subView.setIsBuying(false);
            if (warnAboutManyItems) {
                model.transitionToDialog(new SimpleMessageView(model.getView(),
                        "Please note that you currently have more items than can be displayed in this view."));
                warnAboutManyItems = false;
                subView.setOverflowWarning(true);
            }
        }
        subView.setOverflowWarning(false);
    }

    public void setSellingEnabled(boolean b) {
        this.sellingEnabled = b;
    }


    public static List<Item> makeGeneralShopInventory(Model model, int commons, int uncommons, int rares) {
        List<Item> shopInventory = new ArrayList<>();
        shopInventory.addAll(model.getItemDeck().draw(commons, Prevalence.common, 0.0));
        shopInventory.addAll(model.getItemDeck().draw(uncommons, Prevalence.uncommon, 0.0));
        shopInventory.addAll(model.getItemDeck().draw(rares, Prevalence.rare, 0.0));
        Collections.sort(shopInventory);
        return shopInventory;
    }

    public void setMayOnlyBuyOne(boolean b) {
        this.mayOnlyBuyOne = b;
    }
}

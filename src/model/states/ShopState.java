package model.states;

import model.GameStatistics;
import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.items.*;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import sound.SoundEffects;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.SimpleMessageView;
import view.subviews.ArrowMenuSubView;
import view.subviews.CollapsingTransition;
import view.subviews.ShopSubView;
import view.subviews.SubView;

import java.util.*;

public class ShopState extends GameState {

    public static final int HAGGLE_LIMIT = 40;
    public static final int HAGGLE_DIFFICULTY = 7;
    private final ShopSubView subView;
    private final int partyMaxMercantile;
    private final String seller;
    private boolean warnAboutManyItems = false;
    private HashMap<Item, Integer> prices;
    private SteppingMatrix<Item> buyItems;
    private SteppingMatrix<Item> sellItems;
    private List<Item> itemsForSale;
    private boolean showingBuyItems = true;
    private boolean sellingEnabled = true;
    private boolean mayOnlyBuyOne = false;
    private boolean[] haggleFlag;

    public ShopState(Model model, String seller, List<Item> itemsForSale, int[] specialPrices, boolean[] haggleFlag) {
        super(model);
        this.seller = seller;
        this.itemsForSale = itemsForSale;
        buyItems = new SteppingMatrix<>(8, 8);
        sellItems = new SteppingMatrix<>(8, 8);
        boolean overflow = false;
        if (itemsForSale.size() > buyItems.getRows()*buyItems.getColumns()) {
            buyItems.addElements(itemsForSale.subList(0, buyItems.getRows()*buyItems.getColumns()));
            overflow = true;
        } else {
            buyItems.addElements(itemsForSale);
        }
        this.haggleFlag = haggleFlag;
        List<Item> itemsToSell = getSellableItems(model);
        if (itemsToSell.size() > sellItems.getColumns() * sellItems.getRows()) {
            itemsToSell = itemsToSell.subList(0, sellItems.getColumns() * sellItems.getRows());
            warnAboutManyItems = true;
            itemsToSell.sort(Comparator.comparing(Item::getName));
        }
        sellItems.addElements(itemsToSell);
        makePricesMap(itemsForSale, specialPrices);
        partyMaxMercantile = MyLists.maximum(model.getParty().getPartyMembers(), gc -> gc.getRankForSkill(Skill.Mercantile));
        this.subView = makeSubView(buyItems, true, seller, prices, this, partyMaxMercantile);
        subView.setOverflowWarning(overflow);
    }

    public ShopState(Model model, String seller, List<Item> itemsForSale, boolean[] haggleFlag) {
        this(model, seller, itemsForSale, null, haggleFlag);
    }

    protected ShopSubView makeSubView(SteppingMatrix<Item> buyItems, boolean isBuying, String seller,
                                      HashMap<Item, Integer> prices, ShopState shopState, int partyMaxMercantile) {
        return new ShopSubView(buyItems, true, seller, prices, this, partyMaxMercantile);
    }

    public List<Item> getSellableItems(Model model) {
        List<Item> itemsToSell = new ArrayList<>();
        itemsToSell.addAll(model.getParty().getInventory().getAllItems());
        itemsToSell.removeIf((Item it) -> !it.isSellable());
        groupStackables(itemsToSell);
        return itemsToSell;
    }

    private void groupStackables(List<Item> itemsToSell) {
        Map<String, Integer> counts = new HashMap<>();
        for (Item it : itemsToSell) {
            if (it.isStackable()) {
                if (counts.containsKey(it.getName())) {
                    counts.put(it.getName(), counts.get(it.getName()) + 1);
                } else {
                    counts.put(it.getName(), 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > 1) {
                Item inner = MyLists.find(itemsToSell, it -> it.getName().equals(entry.getKey()));
                assert inner != null;
                itemsToSell.removeIf(it -> it.getName().equals(entry.getKey()));
                itemsToSell.add(new StackableDummyItem(inner, entry.getValue()));
            }
        }

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
            } else if (topCommand == 1 || topCommand == 0) {
                if ((topCommand == 1 && showingBuyItems && maySell(model)) ||
                        (topCommand == 0 && !showingBuyItems && !buyItems.getElementList().isEmpty())) {
                    toggleBuySell(model);
                }
                continue;
            }

            List<String> buySellActions = new ArrayList<>();
            if (showingBuyItems) {
                buySellActions.add(getAcquireVerb());
                addHaggleAction(model, matrixToUse.getSelectedElement(), buySellActions);
            } else {
                buySellActions.add(getRelinquishVerb());
            }
            if (matrixToUse.getSelectedElement().isAnalyzable()) {
                buySellActions.add("Analyze");
            }
            buySellActions.add("Back");

            int xPos = Math.min(matrixToUse.getColumns()-3, matrixToUse.getSelectedPoint().x)*4 + SubView.X_OFFSET;
            int yPos = matrixToUse.getSelectedPoint().y*4 + 10;
            final char[] selectedAction = new char[]{'x'};
            model.setSubView(new ArrowMenuSubView(model.getSubView(), buySellActions, xPos, yPos, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    if (buySellActions.get(cursorPos).equals(getAcquireVerb())) {
                        selectedAction[0] = 'B';
                    } else if (buySellActions.get(cursorPos).equals(getRelinquishVerb())) {
                        selectedAction[0] = 'S';
                    } else if (buySellActions.get(cursorPos).equals("Haggle")) {
                        selectedAction[0] = 'H';
                    } else if (buySellActions.get(cursorPos).equals("Analyze")) {
                        selectedAction[0] = 'A';
                    }
                    model.setSubView(getPrevious());
                }
            });
            waitForReturnSilently();
            if (selectedAction[0] == 'B') {
                Item it = buyItems.getSelectedElement();
                if (purchaseItem(model, it, xPos, yPos, prices.get(it))) {
                    break;
                }
            } else if (selectedAction[0] == 'H') {
                Item it = buyItems.getSelectedElement();
                if (haggleForItem(model, it, xPos, yPos)) {
                    break;
                }
            } else if (selectedAction[0] == 'S' && maySell(model)) {
                Item it = sellItems.getSelectedElement();
                if (sellThisItem(model, it)){
                    break;
                }
            } else if (selectedAction[0] == 'A') {
                model.transitionToDialog(matrixToUse.getSelectedElement().getAnalysisDialog(model));
            }
        }
        return new EveningState(model);
    }

    private void addHaggleAction(Model model, Item it, List<String> buySellActions) {
        if (haggleFlag[0] && it.getCost() >= HAGGLE_LIMIT) {
            buySellActions.add("Haggle");
        }
    }

    protected String getRelinquishVerb() {
        return "Sell";
    }

    protected String getAcquireVerb() {
        return "Buy";
    }

    protected boolean purchaseItem(Model model, Item it, int xPos, int yPos, int cost) {
        if (cost > model.getParty().getGold()) {
            println("You cannot afford that.");
        } else {
            if (it instanceof InventoryDummyItem && ((InventoryDummyItem) it).keepInStock()) {
                model.getParty().getInventory().addItem(it.copy());
            } else {
                buyItems.remove(it);
                itemsForSale.remove(it);
                model.getParty().getInventory().addItem(it);
            }

            model.getParty().spendGold(cost);
            if (cost > 0) {
                println("You bought " + it.getName() + " for " + cost + " gold.");
                GameStatistics.incrementItemsBought(1);
            } else {
                println("You received " + it.getName() + ".");
            }
            SoundEffects.playSound(it.getSound());
            model.getTutorial().equipment(model);
            if (!checkForImmediateEquip(model, it, xPos, yPos) && !(it instanceof InventoryDummyItem)) {
                sellItems.addElementLast(it);
            }
            if (buyItems.getElementList().isEmpty()) {
                if (!sellingEnabled || sellItems.getElementList().isEmpty()) {
                    return true;
                } else {
                    toggleBuySell(model);
                }
            }
            if (mayOnlyBuyOne) {
                return true;
            }
        }
        return false;
    }

    protected boolean haggleForItem(Model model, Item it, int xPos, int yPos) {
        model.getTutorial().haggling(model);
        GameCharacter haggler;
        if (model.getParty().size() > 1) {
            print("Who would you like to haggle for " + it.getName() + "? ");
            haggler = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        } else {
            haggler = model.getParty().getPartyMember(0);
        }
        SkillCheckResult result = SkillChecks.doSkillCheckWithReRoll(model, this, haggler, Skill.Mercantile, HAGGLE_DIFFICULTY,
                5, haggler.getRankForSkill(Skill.Persuade));
        if (result.isFailure()) {
            printQuote(seller, "Hmph. My prices are final. Accept them or walk away.");
            preventHaggling();
            return false;
        }
        printQuote(seller, "I guess I can let it go for a little less.");
        int newPrice = (int) Math.round((1.0 - getHaggleDiscount(result.getModifiedRoll())) * prices.get(it));
        printQuote(seller, "How about " + newPrice + " gold?");
        if (newPrice > model.getParty().getGold()) {
            leaderSay("Naw... can't afford that.");
            printQuote(seller, "Well, I just can't go any lower.");
            preventHaggling();
            return false;
        }

        print("Do you accept the haggled price? (Y/N) ");
        if (yesNoInput()) {
            leaderSay(MyRandom.sample(List.of("You've got yourself a deal.", "Done!",
                    "Nice doing business with you.", "The price is acceptable.", "I accept.", "I agree.")));
            prices.put(it, newPrice);
            return purchaseItem(model, it, xPos, yPos, newPrice);
        }
        printQuote(seller, "Just wasting my time, huh? Just buy something already.");
        preventHaggling();
        return false;
    }

    public static double getHaggleDiscount(int modifiedRoll) {
        double[] discounts = new double[]{
            //   0     1     2     3     4     5     6     7     8     9
                0.05, 0.07, 0.09, 0.12, 0.15, 0.19, 0.23, 0.28, 0.33, 0.38,
            //   10    11    12    13    14    15    16    17    18    19
                0.42, 0.46, 0.50, 0.54, 0.58, 0.62, 0.66, 0.69, 0.72, 0.75
        };
        int index = modifiedRoll - HAGGLE_DIFFICULTY;
        if (index >= discounts.length) {
            return discounts[discounts.length - 1];
        }
        return discounts[index];
    }

    private void preventHaggling() {
        haggleFlag[0] = false;
    }

    protected boolean sellThisItem(Model model, Item it) {
        if (!isCurrentlyEquipped(model, it)) {
            sellItems.remove(it);
            itemJustSold(model, it, buyItems, prices);
            model.getParty().getInventory().remove(it);
            if (getSellableItems(model).isEmpty()) {
                if (buyItems.getElementList().isEmpty()) {
                    return true;
                } else {
                    toggleBuySell(model);
                }
            }
        } else if (it instanceof StackableDummyItem) {
            StackableDummyItem dummy = ((StackableDummyItem)it);
            Item inner = dummy.getInnerItem();
            itemJustSold(model, inner, buyItems, prices);
            model.getParty().getInventory().remove(inner);
        } else {
            println("You cannot sell an item that is currently equipped.");
        }
        return false;
    }

    protected void itemJustSold(Model model, Item it, SteppingMatrix<Item> buyItems, HashMap<Item, Integer> prices) {
        int money;
        if (prices.containsKey(it)) {
            money = prices.get(it);
        } else {
            money = getSellValue(model, it);
        }
        model.getParty().earnGold(money);
        println("You sold " + it.getName() + " for " + money + " gold.");
        GameStatistics.incrementItemsSold(1);
        SoundEffects.sellItem();
    }

    private int getSellValue(Model model, Item it) {
        return it.getSellValue(partyMaxMercantile);
    }

    public static double getSellRateForMercantile(int maxMercantile) {
        if (maxMercantile == 0) {
            return 0.4;
        }
        if (maxMercantile <= 3) {   // 0.5   0.6   0.7
            return getSellRateForMercantile(0) + maxMercantile / 10.0;
        }
        if (maxMercantile <= 6) {   // 0.75  0.80  0.85
            return getSellRateForMercantile(3) + (maxMercantile - 3) / 20.0;
        }
        // 0.86 0.87 0.88 0.89 ...
        return getSellRateForMercantile(6) + (maxMercantile - 6) / 100.0;

    }

    private boolean checkForImmediateEquip(Model model, Item it, int xPos, int yPos) {
        if (!(it instanceof EquipableItem)) {
            return false;
        }
        List<GameCharacter> candidates = MyLists.filter(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                        ((it instanceof Weapon) || (it instanceof Clothing) || (it instanceof Accessory)) &&
                        (Equipment.canEquip(it, gc).equals("")));

        if (candidates.isEmpty()) {
            return false;
        }
        print("Equip immediately? ");
        List<String> options = MyLists.transform(candidates, GameCharacter::getFirstName);
        options.add("Cancel");

        final GameCharacter[] didAction = new GameCharacter[]{null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, xPos, yPos, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                if (cursorPos < candidates.size()) {
                    ((EquipableItem) it).equipYourself(candidates.get(cursorPos));
                    didAction[0] = candidates.get(cursorPos);
                }
                model.setSubView(getPrevious());
            }
        });
        waitForReturnSilently();
        if (didAction[0] != null) {
            if (MyRandom.flipCoin()) {
                println(it.getName() + " was equipped by " + didAction[0].getName() + ".");
            } else {
                println("");
                partyMemberCommentOnEquip(model, didAction[0], it);
            }
            for (Item it2 : model.getParty().getInventory().getAllItems()) {
                if (!sellItems.getElementList().contains(it2)) {
                    sellItems.addElementLast(it2);
                    break;
                }
            }
            return true;
        }

        println(it.getName() + " was put into inventory.");
        return false;
    }

    private void partyMemberCommentOnEquip(Model model, GameCharacter who, Item what) {
        if (who.hasPersonality(PersonalityTrait.prudish) && what instanceof Clothing) {
            partyMemberSay(who, MyRandom.sample(List.of("Eeek! Look away while I'm changing.",
                    "Okay I'll put it on. Don't look while I'm changing!",
                    "Don't think I'm undressing in front of you.")));
            return;
        }
        List<MyPair<PersonalityTrait, String>> comments = new ArrayList<>();
        comments.add(new MyPair<>(PersonalityTrait.critical, "I guess it will have to do."));
        comments.add(new MyPair<>(PersonalityTrait.cold, "Not sure I'll use it much, but okay."));
        comments.add(new MyPair<>(PersonalityTrait.anxious, "I hope I get to use it soon!"));
        comments.add(new MyPair<>(PersonalityTrait.friendly, "Thank you so much!3"));
        comments.add(new MyPair<>(PersonalityTrait.irritable, "Re-equipping again? Make up your mind already!#"));
        comments.add(new MyPair<>(PersonalityTrait.generous, "For me? But I didn't get you anything..."));
        comments.add(new MyPair<>(PersonalityTrait.unkind, "Meh."));
        comments.add(new MyPair<>(PersonalityTrait.greedy, "It's a good start, but I need some other things too."));
        comments.add(new MyPair<>(PersonalityTrait.snobby, "It's quite plain... Alright, if I have to."));
        comments.add(new MyPair<>(PersonalityTrait.jovial, "You want me to sell this?"));
        comments.add(new MyPair<>(PersonalityTrait.rude, "I don't want it. I have to? Alright...#"));
        comments.add(new MyPair<>(PersonalityTrait.naive, "A present? For me? Yay!3"));
        comments.add(new MyPair<>(PersonalityTrait.narcissistic, "It doesn't really suit me, but I'll take it anyway."));
        comments.add(new MyPair<>(PersonalityTrait.stingy, "I hope you don't expect me to give you anything."));
        comments.add(new MyPair<>(PersonalityTrait.playful, "Now I'll be ready for action!"));
        comments.add(new MyPair<>(PersonalityTrait.romantic, "It's what I always wanted!3"));
        Collections.shuffle(comments);
        
        for (MyPair<PersonalityTrait, String> pair : comments) {
            if (who.hasPersonality(pair.first)) {
                partyMemberSay(who, pair.second);
                return;
            }
            if (MyRandom.randInt(4) == 0) {
                break;
            }
        }
        
        partyMemberSay(who, MyRandom.sample(List.of("For me? Okay.", "I'll take care of it.",
                "New equipment, good.", "A fine item from the looks of it.", "This should come in handy.",
                "I'll take that.", "I'll make good use of this.", "I've been looking for something like that.",
                "Perfect!", "Thank you.", "I appreciate that.", "Alright, I'll use that.", "I like it.",
                "I think I deserved that.")));
    }

    public boolean maySell(Model model) {
        return sellingEnabled && getSellableItems(model).size() > 0;
    }

    private boolean isCurrentlyEquipped(Model model, Item it) {
        return !model.getParty().getInventory().getAllItems().contains(it);
    }

    public void setSellingMode(Model model) {
        if (showingBuyItems) {
            toggleBuySell(model);
        }
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
        if (MyRandom.randInt(100) < 2) {
            shopInventory.addAll(model.getItemDeck().draw(1, Prevalence.veryRare, 0.0));
        }
        if (MyRandom.rollD10() == 10) {
            Weapon w = (Weapon) (model.getItemDeck().draw(ItemDeck.allWeapons(), 1, Prevalence.unspecified, 0.0).get(0));
            w.setImbuement(MyRandom.sample(ItemDeck.allImbuements()));
            shopInventory.add(w);
        }
        Collections.sort(shopInventory);
        int dieRoll = MyRandom.rollD6();
        if (dieRoll == 5) {
            shopInventory.add(new MaterialsForSaleItem(MyRandom.randInt(1, 10)));
        } else if (dieRoll == 6) {
            shopInventory.add(new IngredientsForSale(MyRandom.randInt(1, 10)));
        }
        return shopInventory;
    }

    public void setMayOnlyBuyOne(boolean b) {
        this.mayOnlyBuyOne = b;
    }

    public boolean mayBuy() {
        return !buyItems.getElementList().isEmpty();
    }
}

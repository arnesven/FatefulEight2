package model.states.dailyaction;

import model.Model;
import model.characters.PersonalityTrait;
import model.items.Item;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import model.states.GameState;
import model.states.dailyaction.shops.ShopCustomer;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.sprites.Sprite;
import view.sprites.WeaponPairSprite;
import view.subviews.ShopInteriorSubView;

import java.awt.*;
import java.util.List;

public class CustomerNode extends InsideDailyActionNode {
    private static final Point CURSOR_SHIFT = new Point(-4, -4);
    private final ShopCustomer customer;

    public CustomerNode(ShopCustomer customer) {
        super("Customer");
        this.customer = customer;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new InteractWithCustomerState(model, customer);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        customer.drawYourself(model, p);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    @Override
    public Point getCursorShift() {
        return CURSOR_SHIFT;
    }

    private static class InteractWithCustomerState extends GameState {
        private final ShopCustomer customer;

        public InteractWithCustomerState(Model model, ShopCustomer customer) {
            super(model);
            this.customer = customer;
        }

        @Override
        public GameState run(Model model) {
            String itemName = customer.getItem().getName();
            if (customer.isDealMade()) {
                customerSay("Thanks again for the " + itemName + "!");
                return null;
            }

            String line = "I'm looking for " + Item.getNameWithArticle(customer.getItem()) +
                    ", but this shop never gets it in stock.";

            customerSay(line);
            Item foundInParty = MyLists.find(model.getParty().getInventory().getAllItems(),
                    it -> it.getName().equals(itemName));
            if (foundInParty != null) {
                print("Do you want to make a deal with the customer for the " + itemName + "? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("Actually. " + iOrWeCap() + " happen to have one here...");
                    customerSay("Really? How lucky! Would you accept " + customer.getGoldOffer() +
                            " gold for " + MyStrings.itOrThem(itemName) + "?");
                    print("Sell the " + itemName + " to the customer for " + customer.getGoldOffer() + " gold? (Y/N) ");
                    if (yesNoInput()) {
                        leaderSay("That's a fair offer. Show me the gold.");
                        println("The customer hands you " + customer.getGoldOffer() + " gold, and you give " +
                                himOrHer(customer.getGender()) + " the " + itemName + ".");
                        model.getParty().earnGold(customer.getGoldOffer());
                        model.getParty().removeFromInventory(foundInParty);
                        customer.setDealMade(true);
                        this.completeAchievement(ShopCustomer.ACHIEVEMENT_KEY);
                        customerSay("Thanks a bunch!");
                        if (!model.getParty().getLeader().hasPersonality(PersonalityTrait.rude) &&
                            !model.getParty().getLeader().hasPersonality(PersonalityTrait.unkind)) {
                            if (MyRandom.flipCoin()) {
                                leaderSay("Use " + MyStrings.itOrThem(itemName) + " well.");
                            } else if (foundInParty instanceof Weapon) {
                                leaderSay("Be careful with " + MyStrings.itOrThem(itemName) +
                                        ", " + MyStrings.itsOrTheyre(itemName) + " sharp.");
                            } else if (foundInParty instanceof Clothing) {
                                leaderSay("I hope it fits you.");
                            } else {
                                leaderSay("You're welcome.");
                            }
                        }
                        return null;
                    }
                }
            }
            leaderSay(MyRandom.sample(List.of("Sorry. I can't help you.", "Tough luck.",
                    "Okay...", "How annoying!", "I hope you find what your looking for eventually.",
                    "I'll keep my eyes open for you.")));
            return null;
        }

        private void customerSay(String line) {
            if (getModel().getSubView() instanceof ShopInteriorSubView) {
                ((ShopInteriorSubView)getModel().getSubView()).addCalloutAtCustomer(line.length());
            }
            printQuote("Customer", line);
        }
    }
}

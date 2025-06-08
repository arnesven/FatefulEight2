package model.states.events;

import model.Model;
import model.classes.Classes;
import model.headquarters.Headquarters;
import model.items.Item;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.ShopState;
import model.tasks.ReturnToInvestedInShopTask;
import util.MyRandom;

import java.util.List;

public class InvestInShopEvent extends DailyEventState {
    private static final int[] INVEST_LEVELS = new int[]{25, 50, 100};
    private static final String INVEST_DAY_PREFIX = "INVESTED_IN_SHOP_DAY";
    private static final String INVEST_GOLD_PREFIX = "INVESTED_IN_SHOP_GOLD";
    private static final double EXPONENT = 1.25;

    public InvestInShopEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        if (hasInvestmentHere(getModel())) {
            return null;
        }
        return new GuideData("Go to empty shop",
                "I know a particular shop in town. It's been around for ages, but... I don't think it's doing too well. " +
                        "I went in there recently and they didn't have a single thing for sale!");
    }

    public static int updateHeadquarters(Model model, String hqLocation) {
        if (!model.getSettings().getMiscCounters().containsKey(INVEST_DAY_PREFIX + hqLocation)) {
            return 0;
        }
        int days = model.getSettings().getMiscCounters().get(INVEST_DAY_PREFIX + hqLocation);
        int investment = model.getSettings().getMiscCounters().get(INVEST_GOLD_PREFIX + hqLocation);
        if ((model.getDay() + 1 - days) % 7 == 0) {
            return calcProfit(model, days, investment);
        }
        return 0;
    }

    @Override
    protected void doEvent(Model model) {
        if (hasInvestmentHere(model)) {
           askAboutBusiness(model);
        } else {
           offerInvestment(model);
        }
    }

    private void askAboutBusiness(Model model) {
        UrbanLocation urb = (UrbanLocation) model.getCurrentHex().getLocation();
        int days = model.getSettings().getMiscCounters().get(INVEST_DAY_PREFIX + urb.getPlaceName());
        int investment = model.getSettings().getMiscCounters().get(INVEST_GOLD_PREFIX + urb.getPlaceName());
        println("You step into the shop where you invested money. There are now plenty of wares for sale, " +
                "and there are even some customers roaming the shop.");
        showRandomPortrait(model, Classes.MERCHANT, "Shopkeeper");
        portraitSay("Hello there, welcome back partner.");
        leaderSay("Where is the person " + iOrWe() + " spoke to before?");
        portraitSay("Our business has grown, and we have several employees now.");
        leaderSay("That's good to hear. Business has been good then?");
        if (hasHeadquartersHere(model)) {
            if (model.getDay() - days < 7) {
                portraitSay("Yes, but there hasn't been enough time to turn a profit yet.");
                leaderSay("I see...");
                portraitSay("But don't worry! When we do, we'll deliver your share directly to your house in town.");
                leaderSay("Alright then.");
            } else {
                portraitSay("Yes it has. We've been sending your share to your house here in town.");
                leaderSay("Oh, really. That's good to hear. Well, keep up the good work then I suppose.");
                portraitSay("We will.");
            }
        } else {
            int profit = calcProfit(model, days, investment);
            if (profit == 0) {
                portraitSay("Yes, but there hasn't been enough time to turn a profit yet. Please return again later.");
            } else {
                if (profit < 10) {
                    portraitSay("Yes, somewhat. Here is your share.");
                } else if (profit < 50) {
                    portraitSay("Yes, it has. Here is your share.");
                } else {
                    portraitSay("Yes, very much so. Here is your share.");
                }
                println("You received " + profit + " gold from the shopkeeper.");
                model.getParty().addToGold(profit);
                leaderSay("Very good. Keep up the good work.");
            }
        }
        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Browse wares", "Leave shop")); // TODO: Invest more
        if (choice == 0) {
            leaderSay("Can I buy something now?");
            portraitSay("Of course. I would give you a discount. But there really isn't any point, " +
                    "since you are already receiving a percentage of our proceeds.");
            leaderSay("Fair enough.");
            int itemRows = 2;
            for (int investLevel : INVEST_LEVELS) {
                if (investment == investLevel) {
                    break;
                }
                itemRows++;
            }
            List<Item> stock = model.getItemDeck().draw(itemRows * 8);
            ShopState shopState = new ShopState(model, "Investment Trader", stock, new boolean[]{true});
            print("Press enter to continue.");
            waitForReturn();
            shopState.run(model);
        }
        leaderSay("Good bye.");
        portraitSay("Good bye partner.");
    }

    private static int calcProfit(Model model, int days, int investment) {
        return (int)Math.floor(Math.pow(model.getDay() - days, EXPONENT) * investment / 100.0);
    }

    private static boolean hasInvestmentHere(Model model) {
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            UrbanLocation urb = (UrbanLocation) model.getCurrentHex().getLocation();
            return model.getSettings().getMiscCounters().containsKey(INVEST_DAY_PREFIX + urb.getPlaceName());
        }
        return false;
    }

    private void offerInvestment(Model model) {
        println("As you step into a shop, you're immediately greeted by the shopkeeper.");
        showRandomPortrait(model, Classes.MERCHANT, "Shopkeeper");
        portraitSay("Hello there. How can I help you?");
        leaderSay("Uhm, well, I would browse your wares but...");
        println("You look around the shop and see nothing but empty shelves and dusty display cases.");
        leaderSay("Did a supply shipment get lost or something?");
        portraitSay("Actually... times have not been the best for us. " +
                "We're currently seeking investors to help us get back on our feet. Would you be interested in helping our business?");
        leaderSay("Possibly... What would be the arrangement, and what sums are we talking about?");
        portraitSay("Well, even a small contribution would yield dividends. With a small investment, " +
                "say " + INVEST_LEVELS[0] + " gold, I could give you 5% of my weekly earnings. " +
                "A medium investment of " + INVEST_LEVELS[1] + " gold, and I would give you 10%.");
        leaderSay("How about a " + INVEST_LEVELS[2] + " gold?");
        portraitSay("Such a large investment would really help! In return, I'd give you 20% of my weekly earnings.");
        println("How much would you like to invest in the shop?");
        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Small (" + INVEST_LEVELS[0] + " gold)",
                "Medium (" + INVEST_LEVELS[1] + " gold)", "Large (" + INVEST_LEVELS[2] + " gold)", "No thanks"));
        int investment = 0;
        if (choice < INVEST_LEVELS.length) {
            investment = INVEST_LEVELS[choice];
        }
        if (investment == 0) {
            leaderSay("I think " + iOrWe() + " need some time to think about this.");
            portraitSay("I understand. One should always think carefully before entering into a partnership.");
        } else {
            if (model.getParty().getGold() < investment) {
                leaderSay("I would love to invest " + investment + " gold, but " + iOrWe() +
                        " just don't have the money right now.");
                portraitSay("Yeah, times are tough.");
            } else {
                setInvestedInTown(model, investment);
                leaderSay("I want to invest " + investment + " gold.");
                portraitSay("You do? That's fantastic.");
                println("You hand over " + investment + " gold to the shopkeeper.");
                model.getParty().addToGold(-investment);
                leaderSay("But I expect to see good profit from this in the end.");
                portraitSay("You will, I guarantee it.");
                if (hasHeadquartersHere(model)) {
                    portraitSay("Ah, I see you have a house in town. " +
                            "I'll just send your share of the profits directly to your house.");
                } else {
                    portraitSay("I'll have your share of the profits ready for you the very next time you return.");
                }
                leaderSay("Wonderful. Well until we see each other again then.");
                portraitSay("See you around... partner.");
                JournalEntry.printJournalUpdateMessage(model);
                model.getParty().addDestinationTask(new ReturnToInvestedInShopTask(model.getParty().getPosition(),
                        (UrbanLocation) model.getCurrentHex().getLocation(), investment));
            }
        }
    }

    private void setInvestedInTown(Model model, int investment) {
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            UrbanLocation urb = (UrbanLocation) model.getCurrentHex().getLocation();
            model.getSettings().getMiscCounters().put(INVEST_DAY_PREFIX + urb.getPlaceName(), model.getDay());
            model.getSettings().getMiscCounters().put(INVEST_GOLD_PREFIX + urb.getPlaceName(), investment);
        }
    }

    private boolean hasHeadquartersHere(Model model) {
        if (model.getCurrentHex().getLocation() == null ||
                !(model.getCurrentHex().getLocation() instanceof UrbanLocation)) {
            return false;
        }
        return model.getParty().hasHeadquartersIn((UrbanLocation) model.getCurrentHex().getLocation());
    }
}

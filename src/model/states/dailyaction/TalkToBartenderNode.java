package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.classes.Skill;
import model.items.FoodDummyItem;
import model.items.Item;
import model.items.ObolsDummyItem;
import model.items.potions.BeerPotion;
import model.items.potions.WinePotion;
import model.items.special.TentUpgradeItem;
import model.items.weapons.FishingPole;
import model.states.AcceptDeliveryEvent;
import model.states.GameState;
import model.states.TradeWithBartenderState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.SubView;
import view.subviews.TavernSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TalkToBartenderNode extends DailyActionNode {
    private static final Sprite STOOL = new Sprite32x32("barstool", "world_foreground.png", 0x55,
            MyColors.GRAY, MyColors.DARK_RED, MyColors.DARK_GREEN, MyColors.CYAN);
    private final boolean inTown;

    private boolean workDone = false;
    private List<Item> itemsForSale;

    public TalkToBartenderNode(boolean inTown) {
        super("Talk to bartender");
        this.inTown = inTown;
        itemsForSale = new ArrayList<>(List.of(new ObolsDummyItem(10),
                new FoodDummyItem(5)));
        itemsForSale.add(new TentUpgradeItem());
        if (MyRandom.randInt(4) == 0) {
            itemsForSale.add(new FishingPole());
        }
        for (int i = MyRandom.randInt(4); i > 0; --i) {
            itemsForSale.add(new BeerPotion());
        }
        for (int i = MyRandom.randInt(3); i > 0; --i) {
            itemsForSale.add(new WinePotion());
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TalkToBartenderState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return STOOL;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(STOOL.getName(), p, STOOL);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (workDone) {
            model.setTimeOfDay(TimeOfDay.EVENING);
        }
    }

    private class TalkToBartenderState extends GameState {
        public TalkToBartenderState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            List<String> options = new ArrayList<>(List.of("Get Advice", "Trade"));
            boolean buyHorse = model.getParty().getHorseHandler().getAvailableHorse(model) != null;
            if (!model.getTimeOfDay().equals(TimeOfDay.EVENING)) {
                options.add("Ask For Work");
            }
            if (buyHorse && !inTown) {
                options.add("Buy Horse");
            }
            if (model.getParty().getHorseHandler().size() > 0 && !inTown) {
                options.add("Sell Horse");
            }
            options.add("Never mind");
            int selected = multipleOptionArrowMenu(model, 32, 18, options);
            if (selected == 0) {
                getAdvice(model);
            } else if (selected == 1) {
                new TradeWithBartenderState(model, itemsForSale).run(model);
            } else if (options.get(selected).contains("Buy Horse")) {
                BuyHorseState state = new BuyHorseState(model, "Bartender") {
                    @Override
                    protected void sellerSay(String seller, String text) {
                        bartenderSay(model, text);
                    }
                };
                state.run(model);
            } else if (options.get(selected).contains("Sell Horse")) {
                SellHorseState state = new SellHorseState(model, "Bartender") {
                    @Override
                    protected void buyerSay(String text) {
                        bartenderSay(model, text);
                    }
                };
                state.run(model);
            } else if (options.get(selected).contains("Ask For Work")){
                askForWork(model);
            }
            return model.getCurrentHex().getDailyActionState(model);
        }

        private void askForWork(Model model) {
            leaderSay(MyRandom.sample(List.of("Uhm, are there any chores you need done around here?",
                    "Got any work for me?", "I need work, can I do something for you?",
                    "Hey, can you give me a job?")));
            if (model.getSettings().getMiscFlags().containsKey("innworkdone") &&
                    model.getSettings().getMiscFlags().get("innworkdone")) {
                bartenderSay(model,  "Sorry, not at the moment.");
                return;
            }
            workDone = true;
            model.getSettings().getMiscFlags().put("innworkdone", true);
            int dieRoll = MyRandom.rollD10();
            if (dieRoll < 4) { // 1 - 3
                washDishes(model);
            } else if (dieRoll < 6 && !inTown) { // 4 - 5
                cleanStables(model);
            } else if (dieRoll < 8) { // 6 - 7
                helpWithBooks(model);
            } else if (dieRoll < 10) { // 8 - 9
                sharpenKnives(model);
            } else {
                offerDeliveryTask(model);
            }
        }

        private void washDishes(Model model) {
            bartenderSay(model,  "We do have an enormous amount of dishes that need washing. Won't pay much though.");
            leaderSay("I'll do it.");
            println("You spend the rest of the day cleaning up in the kitchen.");
            bartenderSay(model,  "Good work. Here's your pay.");
            model.getParty().addToGold(Math.min(4, model.getParty().size()));
            println("You got " + model.getParty().size() + " gold.");
        }

        private void cleanStables(Model model) {
            bartenderSay(model,  "The stable needs cleaning. Make it tidy in there and I'll pay you.");
            leaderSay("I'll do it.");
            println("You spend the rest of the day cleaning up the filthy stables.");
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 5);
            if (success) {
                bartenderSay(model,  "Wow! I'm sure the ponies will be please. Good work. Here's your pay.");
                model.getParty().addToGold(5);
                println("You got 5 gold.");
            } else {
                bartenderSay(model,  "What's this? This place is even messier " +
                        "than it was before? I won't pay for such shoddy work!");
                leaderSay("Rats...");
            }
        }

        private void helpWithBooks(Model model) {
            bartenderSay(model,  "The previous owner of this place left the books in complete disarray. " +
                    "Can you have a look at them and set them straight?");
            leaderSay("I'll do it.");
            println("You spend the rest of the day trying to make sense of the bartender's economical situation.");
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Logic, 6);
            if (success) {
                println("You finally manage to understand the system of symbols and numbers, then transcribe them into " +
                        "notes which are more generally understandable.");
                bartenderSay(model,  "Oh, now I understand. Thank you for clearing this up. Good work. Here's your pay.");
                model.getParty().addToGold(6);
                println("You got 6 gold.");
            } else {
                println("Despite your best efforts, the ledgers and papers are completely beyond your understanding. " +
                        "After many hours you are forced to admit that you aren't getting anywhere.");
                bartenderSay(model,  "That's a shame. Well, at least you tried. Here's something for your troubles.");
                model.getParty().addToGold(2);
                println("You got 2 gold.");
            }
        }

        private void sharpenKnives(Model model) {
            bartenderSay(model,  "The knives in the kitchen are very blunt. Could you sharpen them for me?");
            leaderSay("I'll do it.");
            println("You spend the rest of the day sharpening all the knives in the kitchen.");
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Blades, 6);
            if (success) {
                bartenderSay(model,  "Excellent. You've saved me a trip to a town to have this done." +
                        " Here's your pay.");
                model.getParty().addToGold(8);
                println("You got 8 gold.");
            } else {
                bartenderSay(model,  "... These knives are blunter than a sledgehammer. " +
                        "I completely overestimated your ability. " +
                        "I won't pay for such shoddy work!");
            }
        }


        private void offerDeliveryTask(Model model) {
            AcceptDeliveryEvent deliveryEvent = new AcceptDeliveryEvent(model, "Bartender") {
                @Override
                protected void senderSpeak(String text) {
                    bartenderSay(model, text);
                }
            };
            deliveryEvent.offerDeliveryTask(model);
        }

        private void getAdvice(Model model) {

            bartenderSay(model,  "I used to be an adventurer like you, then I took an arrow to the knee. " +
                    "Now I run this place, and I'm doing pretty well for myself, but I do sometimes dream back to the " +
                    "good old days when I was in a party of adventurers. The things we accomplished...");
            leaderSay("Got any good advice on adventuring?");
            String line = MyRandom.sample(List.of(
                    "Keep your party members well rested and you will be more " +
                            "likely to succeed in your endeavors. We have rooms here with very comfortable beds.",
                    "Up-and-coming adventurers can usually contribute some coin to the party's purse. " +
                            "Hiring on a few may give you the starting capital you need.",
                    "Before you get a handle on things, stick to the road. You never know what will jump you out in the bush.",
                    "A little armor is much better than having none. I had to learn that the hard way.",
                    "Stock up on rations when you can. You never know when you have to make a longer trip than you intended.",
                    "Towns are great places to find jobs and quests.",
                    "Be wary of the Brotherhood. Think twice about making any deals with them.",
                    "Try to keep your fellow party members happy. Things can get out of hand quickly if they " +
                            "start disliking the leader.",
                    "Having a good formation during combat is crucial. And watch out for enemies with ranged weapons.",
                    "Sometimes innkeepers and bartenders, like myself, have some work that need to be done. " +
                            "It may not pay much, but at least it will put food on the table.",
                    "Don't get ahead of yourself by hiring on too many hands too quickly. Large parties are much more " +
                            "costly and difficult to maintain than small ones.",
                    "Always keep a lock pick handy. You know... if you lock yourself out of your house.",
                    "Try to get a good mix of characters in your party. It's important to have the right balance " +
                            "of brawn, brains, stealth and magic."));
            bartenderSay(model,  line);
        }

        private void bartenderSay(Model model, String line) {
            SubView view = model.getSubView();
            if (view instanceof TavernSubView) {
                ((TavernSubView)view).addCalloutAtBartender(line.length());
            }
            printQuote("Bartender", line);
        }

    }
}

package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.classes.Skill;
import model.horses.Horse;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TalkToBartenderNode extends DailyActionNode {
    private static final Sprite STOOL = new Sprite32x32("barstool", "world_foreground.png", 0x55,
            MyColors.GRAY, MyColors.DARK_RED, MyColors.DARK_GREEN, MyColors.CYAN);
    private final boolean inTown;

    private boolean workDone = false;

    public TalkToBartenderNode(boolean inTown) {
        super("Talk to bartender");
        this.inTown = inTown;
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
            List<String> options = new ArrayList<>(List.of("Get Advice", "Buy Rations"));
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
            options.add("Buy Obols");
            options.add("Sell Obols");
            int selected = multipleOptionArrowMenu(model, 32, 18, options);
            if (selected == 0) {
                getAdvice(model);
            } else if (selected == 1) {
                new BuyRationsState(model).run(model);
            } else if (options.get(selected).contains("Buy Horse")) {
                new BuyHorseState(model, "Bartender").run(model);
            } else if (options.get(selected).contains("Sell Horse")) {
                new SellHorseState(model).run(model);
            } else if (options.get(selected).contains("Buy Obols")) {
                buyObols(model);
            } else if (options.get(selected).contains("Ask For Work")) {
                askForWork(model);
            } else {
                sellObols(model);
            }
            return model.getCurrentHex().getDailyActionState(model);
        }

        private void askForWork(Model model) {
            leaderSay("Uhm, are there any chores you need done around here?");
            if (model.getSettings().getMiscFlags().containsKey("innworkdone") &&
                    model.getSettings().getMiscFlags().get("innworkdone")) {
                printQuote("Bartender", "Sorry, not at the moment.");
                return;
            }
            workDone = true;
            model.getSettings().getMiscFlags().put("innworkdone", true);
            int dieRoll = MyRandom.rollD6();
            if (dieRoll < 3) {
                printQuote("Bartender", "We do have an enormous amount of dishes that need washing. Won't pay much though.");
                leaderSay("I'll do it.");
                println("You spend the rest of the day cleaning up in the kitchen.");
                printQuote("Bartender", "Good work. Here's your pay.");
                model.getParty().addToGold(Math.min(4, model.getParty().size()));
                println("You got " + model.getParty().size() + " gold.");
            } else if (dieRoll < 5 && !inTown) {
                printQuote("Bartender", "The stable needs cleaning. Make it tidy in there and I'll pay you.");
                leaderSay("I'll do it.");
                println("You spend the rest of the day cleaning up the filthy stables.");
                boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 5);
                if (success) {
                    printQuote("Bartender", "Wow! I'm sure the ponies will be please. Good work. Here's your pay.");
                    model.getParty().addToGold(5);
                    println("You got 5 gold.");
                } else {
                    printQuote("Bartender", "What's this? This place is even messier " +
                            "than it was before? I won't pay for such shoddy work!");
                    leaderSay("Rats...");
                }
            } else if (dieRoll < 6) {
                printQuote("Bartender", "The previous owner of this place left the books in complete disarray. " +
                        "Can you have a look at them and set them straight?.");
                leaderSay("I'll do it.");
                println("You spend the rest of the day trying to make sense of the bartender's economical situation.");
                boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Logic, 6);
                if (success) {
                    println("You finally manage to understand the system of symbols and numbers, then transcribe them into " +
                            "notes which are more generally understandable.");
                    printQuote("Bartender", "Oh, now I understand. Thank you for clearing this up. Good work. Here's your pay.");
                    model.getParty().addToGold(6);
                    println("You got 6 gold.");
                } else {
                    println("Despite your best efforts, the ledgers and papers are completely beyond your understand. " +
                            "After many hours you are forced to admit that you aren't getting anywhere.");
                    printQuote("Bartender", "That's a shame. Well, at least you tried. Here's something for your troubles.");
                    model.getParty().addToGold(2);
                    println("You got 2 gold.");
                }
            } else {
                printQuote("Bartender", "The knives in the kitchen are very blunt. Could you sharpen them for me?");
                leaderSay("I'll do it.");
                println("You spend the rest of the day sharpening all the knives in the kitchen.");
                boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Blades, 6);
                if (success) {
                    printQuote("Bartender", "Excellent. You've saved me a trip to a town to have this done." +
                            " Here's your pay.");
                    model.getParty().addToGold(8);
                    println("You got 8 gold.");
                } else {
                    printQuote("Bartender", "... These knives are blunter than a sledgehammer. " +
                            "I completely overestimated your ability. " +
                            "I won't pay for such shoddy work!");
                }
            }
        }

        private void getAdvice(Model model) {
            printQuote("Bartender", "I used to be an adventurer like you, then I took an arrow to the knee. " +
                    "Now I run this place, and I'm doing pretty well for myself, but I do sometimes dream back to the " +
                    "good old days when I was in a party of adventurers. The things we accomplished...");
            leaderSay("Got any good advice on adventuring?");
            String line = MyRandom.sample(List.of("Keep your party members well rested and you will be more " +
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
                    "Having a good formation during combat is crucial. And watch out for enemies with ranged weapons."));
            printQuote("Bartender", line);
        }

        private void buyObols(Model model) {
            println("You have " + model.getParty().getObols() + " obols.");
            model.getTutorial().obols(model);
            int spend = 0;
            do {
                print("You get 10 obols for each gold. How much would you like to spend? ");
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
                model.getParty().addToObols(take*10);
                model.getParty().addToGold(take);
                println("You now have " + model.getParty().getObols() + " obols.");
            }
        }
    }
}

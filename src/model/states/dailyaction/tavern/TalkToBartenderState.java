package model.states.dailyaction.tavern;

import model.Model;
import model.TimeOfDay;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.states.GameState;
import model.states.TradeWithBartenderState;
import model.states.dailyaction.BuyHorseState;
import model.states.dailyaction.SellHorseState;
import util.MyLists;
import util.MyRandom;
import view.subviews.SubView;
import view.subviews.TavernSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TalkToBartenderState extends GameState {

    private static final List<InnWorkAction> ALL_INN_WORKS = List.of(
            new WashDishesInnWork(),
            new WashDishesInnWork(),
            new WashDishesInnWork(),
            new CleanStablesInnWork(),
            new CleanStablesInnWork(),
            new HelpWithBooksInnWork(),
            new HelpWithBooksInnWork(),
            new MusicalPerformanceInnWork(),
            new MusicalPerformanceInnWork(),
            new SharpenKnivesInnWork(),
            new SharpenKnivesInnWork(),
            new OfferDeliveryInnWork(),
            new OfferDeliveryInnWork(),
            new MonsterHuntInnWork(),
            new MonsterHuntInnWork()
    );

    private final TalkToBartenderNode talkToBartenderNode;

    public TalkToBartenderState(TalkToBartenderNode talkToBartenderNode, Model model) {
        super(model);
        this.talkToBartenderNode = talkToBartenderNode;
    }

    @Override
    public GameState run(Model model) {
        List<String> options = new ArrayList<>(List.of("Get Advice", "Trade"));
        boolean buyHorse = model.getParty().getHorseHandler().getAvailableHorse(model) != null;
        if (!model.getTimeOfDay().equals(TimeOfDay.EVENING)) {
            options.add("Ask For Work");
        }
        if (buyHorse && !talkToBartenderNode.isInTown()) {
            options.add("Buy Horse");
        }
        if (model.getParty().getHorseHandler().size() > 0 && !talkToBartenderNode.isInTown()) {
            options.add("Sell Horse");
        }
        options.add("Never mind");
        int selected = multipleOptionArrowMenu(model, 32, 18, options);
        if (selected == 0) {
            getAdvice(model);
        } else if (selected == 1) {
            new TradeWithBartenderState(model, talkToBartenderNode.getItemsForSale()).run(model);
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
        } else if (options.get(selected).contains("Ask For Work")) {
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
            bartenderSay(model, "Sorry, not at the moment.");
            return;
        }
        talkToBartenderNode.setWorkDone(true);
        model.getSettings().getMiscFlags().put("innworkdone", true);

        List<InnWorkAction> works = new ArrayList<>(List.of(MyRandom.sample(ALL_INN_WORKS)));
        SkillCheckResult result = model.getParty().getLeader().testSkillHidden(Skill.SeekInfo,
                SkillChecks.adjustDifficulty(model, 8),
                model.getParty().getLeader().getRankForSkill(Skill.Persuade));
        if (result.isSuccessful()) {
            println("(Seek Info " + result.asString() + ")");
            int tries = 0;
            while (works.size() < 2 && tries < 100) {
                InnWorkAction work2 = MyRandom.sample(ALL_INN_WORKS);
                if (!work2.getName().equals(works.get(0).getName())) {
                    works.add(work2);
                }
                tries++;
            }
            Collections.shuffle(works);
        }

        for (InnWorkAction ia : works) {
            bartenderSay(model, ia.getDescription());
        }
        InnWorkAction innWork = works.get(0);
        if (works.size() == 1) {
            leaderSay("I'll do it.");
        } else {
            int choice = multipleOptionArrowMenu(model, 24, 24, MyLists.transform(works, InnWorkAction::getName));
            innWork = works.get(choice);
        }
        innWork.doWork(model, this);
    }

    private void getAdvice(Model model) {
        bartenderSay(model, "I used to be an adventurer like you, then I took an arrow to the knee. " +
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
                        "of brawn, brains, stealth and magic.",
                "I have some gear for sale. I recommend getting a fishing pole, there's plenty of good fishing spots around",
                "Check with travellers at inns and taverns, sometimes they're heading the same way you are and will " +
                        "pay you a little for escorting them",
                "Horses will let you travel faster, but stabling them at inns and taverns isn't free.",
                "Always look for ingredients and materials while you travel. You never know when you might need them",
                "If you can't afford to buy they items you want, try crafting them instead. Crafting benches can be found in towns and castles",
                "Always keep a few potions in your inventory, just trust me on that.",
                "Hiring a guide is a good way to get the most out of your journey through the countryside, or while in town.",
                "Carefully think about how you equip your party members. Consider their strength and weaknesses.",
                "Always keep some room in your tent, that way you can accommodate a newcomer to your party at any time."));
        bartenderSay(model, line);
    }

    public void bartenderSay(Model model, String line) {
        SubView view = model.getSubView();
        if (view instanceof TavernSubView) {
            ((TavernSubView) view).addCalloutAtBartender(line.length());
        }
        printQuote("Bartender", line);
    }

}

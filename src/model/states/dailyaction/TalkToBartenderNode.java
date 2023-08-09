package model.states.dailyaction;

import model.Model;
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

    public TalkToBartenderNode() {
        super("Talk to bartender");
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
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private static class TalkToBartenderState extends GameState {

        public TalkToBartenderState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            List<String> options = new ArrayList<>(List.of("Get Advice", "Buy Rations"));
            boolean buyHorse = model.getParty().getHorseHandler().getAvailableHorse(model) != null;
            if (buyHorse) {
                options.add("Buy Horse");
            }
            if (model.getParty().getHorseHandler().size() > 0) {
                options.add("Sell Horse");
            }
            int selected = multipleOptionArrowMenu(model, 32, 18, options);
            if (selected == 0) {
                getAdvice(model);
            } else if (selected == 1){
                new BuyRationsState(model).run(model);
            } else if (buyHorse && selected == 2) {
                new BuyHorseState(model, "Bartender").run(model);
            } else {
                new SellHorseState(model).run(model);
            }
            return model.getCurrentHex().getDailyActionState(model);
        }

        private void getAdvice(Model model) {
            println("I used to be an adventurer like you, then I took an arrow to the knee. " +
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
            println("Bartender: \"" + line + "\"");
        }
    }
}

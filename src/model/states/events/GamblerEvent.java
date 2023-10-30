package model.states.events;

import model.Model;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class GamblerEvent extends DailyEventState {

    private int potSize;
    private ConstableEvent innerEvent;

    public GamblerEvent(Model model) {
        super(model);
        potSize = MyRandom.randInt(36)*2 + 2;
    }

    @Override
    protected void doEvent(Model model) {
        println("You pass by an alley and see a small crowd gathered there. A few of " +
                "them are squatting, some of them shouting and laughing. You casually " +
                "meander over and look what the commotion is about.");
        model.getParty().randomPartyMemberSay(model, List.of("Dice... I should've guessed"));
        showRandomPortrait(model, Classes.THF, Race.ALL, "Gambler");
        portraitSay("Hey newcomer, it's two gold to roll the dice. Want in?");
        if (model.getParty().getGold() < 2) {
            leaderSay("We don't have the coin, or the time for this.");
        } else {
            print("Do you want to play dice? (Y/N) ");
            if (yesNoInput()) {
                println("You hand the gambler 2 gold.");
                model.getParty().addToGold(-2);
                portraitSay("It's simple. You roll two dice and add them together. On eight or more, you get your money back. " +
                        "Otherwise your money goes into the pot. On double sixes, you win the pot.");
                leaderSay("How big is the pot?");
                portraitSay("Looks like we're up to " + potSize + " gold now. Who knows, maybe you'll get lucky?");
                while (true) {
                    println("The gambler hands you the dice.");
                    int die1 = MyRandom.randInt(1, 6);
                    int die2 = MyRandom.randInt(1, 6);
                    println("You roll a " + die1 + " and a " + die2 + ", for a total of " + (die1 + die2) + ".");
                    if (die1 == 6 && die2 == 6) {
                        leaderSay("Jackpot!");
                        portraitSay("You lucky bastard...#");
                    } else if (die1 + die2 < 8) {
                        potSize += 2;
                        portraitSay("Bad luck newcomer. But hey, now the pot is even bigger. " + potSize + " gold!");
                    } else {
                        portraitSay("Good roll! Here's your money back newcomer.");
                        println("The party receives 2 gold.");
                        model.getParty().addToGold(2);
                    }
                    portraitSay("Wanna go again?");
                    if (model.getParty().getGold() < 2) {
                        println("Unfortunately you cannot afford to continue the game. So you excuse yourself.");
                        break;
                    }
                    print("Do you? (Y/N) ");
                    if (!yesNoInput()) {
                        break;
                    } else {
                        println("You hand the gambler 2 gold.");
                        model.getParty().addToGold(-2);
                        if (MyRandom.rollD10() == 1) {
                            println("The gambler is about to hand you the dice when somebody shouts 'constable!'. The gambler " +
                                    "quickly scoops up the pot and bounds away down the street and the crowd disperses.");
                            innerEvent = new ConstableEvent(model);
                            innerEvent.doEvent(model);
                            return;
                        }
                    }
                }

            }
        }
        println("You leave the gamblers to their game.");
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent != null) {
            return innerEvent.haveFledCombat();
        }
        return super.haveFledCombat();
    }
}

package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class GamblerEvent extends DailyEventState {

    private static final int COST_TO_PLAY = 5;
    private int potSize;
    private ConstableEvent innerEvent;

    public GamblerEvent(Model model) {
        super(model);
        potSize = MyRandom.randInt(54)*2 + 10;
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Seek out gamblers",
                "There are always some gamblers about, if you're into that kind of stuff");
    }

    @Override
    protected void doEvent(Model model) {
        println("You pass by an alley and see a small crowd gathered there. A few of " +
                "them are squatting, some of them shouting and laughing. You casually " +
                "meander over and look what the commotion is about.");
        model.getParty().randomPartyMemberSay(model, List.of("Dice... I should've guessed"));
        showRandomPortrait(model, Classes.THF, Race.ALL, "Gambler");
        portraitSay("Hey newcomer, it's five obols to roll the dice. Want in?");
        if (model.getParty().getObols() < COST_TO_PLAY) {
            leaderSay("We don't have the coin, or the time for this.");
        } else {
            print("Do you want to play dice? (Y/N) ");
            if (yesNoInput()) {
                println("You hand the gambler " + COST_TO_PLAY + " obols.");
                model.getParty().addToObols(-COST_TO_PLAY);
                portraitSay("It's simple. You roll two dice and add them together. On eight or more, you get your money back. " +
                        "Otherwise your money goes into the pot. On double sixes, you win the pot.");
                leaderSay("How big is the pot?");
                portraitSay("Looks like we're up to " + potSize + " obols now. Who knows, maybe you'll get lucky?");
                while (true) {
                    println("The gambler hands you the dice.");
                    int die1 = MyRandom.randInt(1, 6);
                    int die2 = MyRandom.randInt(1, 6);
                    println("You roll a " + die1 + " and a " + die2 + ", for a total of " + (die1 + die2) + ".");
                    if (die1 == 6 && die2 == 6) {
                        leaderSay("Jackpot!");
                        portraitSay("You lucky bastard...#");
                        leaderSay("Pay up man, I won that pot fair and square.");
                        println("Looks around nervously as to assess his options.");
                        leaderSay("Fine! Take it... I was getting bored of this game anyway.");
                        model.getParty().addToObols(potSize);
                        println("The party gains " + potSize + " obols.");
                        println("The crowd then disperses and the gambler stomps off.");
                        return;
                    } else if (die1 + die2 < 8) {
                        potSize += 2;
                        portraitSay("Bad luck newcomer. But hey, now the pot is even bigger. " + potSize + " obols!");
                    } else {
                        portraitSay("Good roll! Here's your money back newcomer.");
                        println("The party receives " + COST_TO_PLAY + " obols.");
                        model.getParty().addToObols(COST_TO_PLAY);
                    }
                    portraitSay("Wanna go again?");
                    randomSayIfPersonality(PersonalityTrait.greedy, List.of(model.getParty().getLeader()),
                            "Just think, we could win those obols!");
                    if (model.getParty().getObols() < COST_TO_PLAY) {
                        println("Unfortunately you cannot afford to continue the game. So you excuse yourself.");
                        break;
                    }
                    print("Do you? (Y/N) ");
                    if (!yesNoInput()) {
                        break;
                    } else {
                        println("You hand the gambler " + COST_TO_PLAY + " obols.");
                        model.getParty().addToObols(-COST_TO_PLAY);
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

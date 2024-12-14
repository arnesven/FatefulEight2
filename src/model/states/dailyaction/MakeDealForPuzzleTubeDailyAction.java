package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.FindPuzzleTubeEvent;
import model.states.events.SilentNoEventState;
import util.MyPair;

class MakeDealForPuzzleTubeDailyAction extends GameState {

    private final UrbanLocation location;

    public MakeDealForPuzzleTubeDailyAction(Model model, UrbanLocation location) {
        super(model);
        this.location = location;
    }

    @Override
    public GameState run(Model model) {
        printQuote(location.getLordName(), "Yes hello? What is the meaning of this?");
        leaderSay("I'm wondering if you know anything about Dwarven Puzzle Tubes?");
        printQuote(location.getLordName(), "Perhaps. Why are you wondering?");
        leaderSay("We are looking for one, you wouldn't happen to have one do you?");
        printQuote(location.getLordName(), "I may have something like that. Now let's see, where did I put it.");
        println("The " + location.getLordTitle() + " brings out a puzzle tube.");
        int basePrice = 50;
        printQuote(location.getLordName(), "I suppose I could let it go for " + basePrice + " gold.");
        model.getLog().waitForAnimationToFinish();
        int barterPrice = basePrice / 2;
        if (model.getParty().getGold() < barterPrice) {
            leaderSay("That's outrageous, it's just a toy.");
            printQuote(location.getLordName(), "All the same, that's my price.");
            leaderSay("Bah... let's get out of here.");
        } else {
            int agreedPrice = basePrice;
            MyPair<Boolean, GameCharacter> succ = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 9);
            if (succ.first) {
                partyMemberSay(succ.second, "That's a lot of money for a silly knickknack, how about " + barterPrice + "?");
                printQuote(location.getLordName(), "I suppose that's true. You've got yourself a deal.");
                agreedPrice = barterPrice;
            } else {
                leaderSay("That's outrages, it's just a toy.");
                printQuote(location.getLordName(), "All the same, that's my price.");
            }
            if (model.getParty().getGold() < agreedPrice) {
                leaderSay("But... but I just don't have that much!");
                printQuote(location.getLordName(), "Well, there's plenty of work which needs to be done in town. " +
                        "I'm sure somebody will give you a job. Now please, I am a busy " + GameState.manOrWoman(location.getLordGender()) + "!");
                leaderSay("Bah... let's get out of here.");
            } else {
                print("Pay " + agreedPrice + " for the Puzzle Tube? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("Okay, here's your money.");
                    model.getParty().addToGold(agreedPrice);
                    printQuote(location.getLordName(), "And here is your puzzle tube. I hope you have fun with it, " +
                            "I could never figure out how it worked or what it was for.");
                    new FindPuzzleTubeEvent(model).doTheEvent(model);
                    // TODO: Ask where did you get it
                } else {
                    leaderSay("On second thought. We may have to hold on to our gold for now.");
                    printQuote(location.getLordName(), "What? Stop wasting my time. Get out of here you!");
                    leaderSay("Sorry. Bye.");
                }
            }
        }
        return new SilentNoEventState(model);
    }
}

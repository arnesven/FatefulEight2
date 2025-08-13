package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.puzzletube.DwarvenPuzzleConstants;
import model.items.puzzletube.MysteryOfTheTubesDestinationTask;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.FindPuzzleTubeEvent;
import model.states.events.SilentNoEventState;
import model.tasks.DestinationTask;
import util.MyLists;
import util.MyPair;

class MakeDealForPuzzleTubeDailyAction extends GameState {

    private final UrbanLocation location;

    public MakeDealForPuzzleTubeDailyAction(Model model, UrbanLocation location) {
        super(model);
        this.location = location;
    }

    @Override
    public GameState run(Model model) {
        lordSay("Yes hello? What is the meaning of this?");
        leaderSay("I'm wondering if you know anything about Dwarven Puzzle Tubes?");
        lordSay("Perhaps. Why are you wondering?");
        leaderSay("We are looking for one, you wouldn't happen to have one do you?");
        lordSay("I may have something like that. Now let's see, where did I put it.");
        println("The " + location.getLordTitle() + " brings out a puzzle tube.");
        int basePrice = 50;
        lordSay("I suppose I could let it go for " + basePrice + " gold.");
        model.getLog().waitForAnimationToFinish();
        int barterPrice = basePrice / 2;
        if (model.getParty().getGold() < barterPrice) {
            leaderSay("That's outrageous, it's just a toy.");
            lordSay("All the same, that's my price.");
            leaderSay("Bah... let's get out of here.");
        } else {
            int agreedPrice = basePrice;
            MyPair<Boolean, GameCharacter> succ = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 9);
            if (succ.first) {
                partyMemberSay(succ.second, "That's a lot of money for a silly knickknack, how about " + barterPrice + "?");
                lordSay("I suppose that's true. You've got yourself a deal.");
                agreedPrice = barterPrice;
            } else {
                leaderSay("That's outrages, it's just a toy.");
                lordSay("All the same, that's my price.");
            }
            if (model.getParty().getGold() < agreedPrice) {
                leaderSay("But... but I just don't have that much!");
                lordSay("Well, there's plenty of work which needs to be done in town. " +
                        "I'm sure somebody will give you a job. Now please, I am a busy " + GameState.manOrWoman(location.getLordGender()) + "!");
                leaderSay("Bah... let's get out of here.");
            } else {
                print("Pay " + agreedPrice + " for the Puzzle Tube? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("Okay, here's your money.");
                    model.getParty().spendGold(agreedPrice);
                    lordSay("And here is your puzzle tube.");
                    new FindPuzzleTubeEvent(model).doTheEvent(model);
                    DestinationTask task = MyLists.find(model.getParty().getDestinationTasks(),
                            dt -> dt instanceof MysteryOfTheTubesDestinationTask);
                    if (task == null) {
                        throw new IllegalStateException("No Mystery of the Tubes Task after getting one from lord!");
                    }
                    lordSay( "I hope you have fun with it, " +
                            "I could never figure out how it worked or what it was for.");
                    leaderSay("How did you get it in the first place?");
                    lordSay("Oh... let see now. Ah yes, it was gifted to me long ago " +
                            "by a toymaker. a dwarf. I seem to remember he was quite famous at the time, " +
                            "although I found him rather eccentric.");
                    leaderSay("Do you remember his name?");
                    CastleLocation kingdomForToymaker = model.getWorld().getKingdomForPosition(task.getPosition());
                    if (((MysteryOfTheTubesDestinationTask)task).isInKingdom(model)) {
                        lordSay("Yes, I think his name was " + DwarvenPuzzleConstants.TOYMAKER_NAME +
                                ". He used to live not far from here, but it must be well over two decades ago.");
                        leaderSay("Can you mark the location on my map?");
                        lordSay("Sure. ");
                        ((MysteryOfTheTubesDestinationTask)task).progressToResidenceKnown();
                        leaderSay("Thanks a bunch. We'll get out of your hair now.");
                        lordSay("Good bye then.");
                    } else {
                       lordSay("I'm sorry no. All I remember is that he used to live in the " +
                               CastleLocation.placeNameToKingdom(kingdomForToymaker.getPlaceName()) +
                               ". Maybe somebody from that part of the realm could tell you more.");
                        ((MysteryOfTheTubesDestinationTask)task).progressToKingdomKnown();
                       leaderSay("Well, that at least gives a direction to search in. Farewell " + location.getLordName() + ".");
                    }

                } else {
                    leaderSay("On second thought. We may have to hold on to our gold for now.");
                    lordSay("What? Stop wasting my time. Get out of here you!");
                    leaderSay("Sorry. Bye.");
                }
            }
        }
        return new SilentNoEventState(model);
    }

    private void lordSay(String line) {
        printQuote(location.getLordName(), line);
    }
}

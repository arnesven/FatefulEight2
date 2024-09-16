package model.states.events;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.special.CageWithBird;
import model.items.special.CollectorItem;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import util.MyLists;
import view.LogView;
import view.subviews.PortraitSubView;

import java.util.List;

public class GentlepersonsClubEvent extends DailyEventState {
    private static final String CLUB_MEMBER_KEY = "GentlepersonsClubMember";
    private static final int MEMBERSHIP_FEE = 100;

    public GentlepersonsClubEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit Gentleperson's Club",
                "There's a meeting place for snobs called the Gentleperson's Club, but I've heard you have to pay to get in");
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You happen to pass a building with a very elaborate sign out front. " +
                "'Gentleperson's Club' is written in fancy letters upon it. " +
                "You step inside.");
        AdvancedAppearance app = PortraitSubView.makeRandomPortrait(Classes.OFFICIAL);
        println("You enter into a large sized vestibule. A nicely dressed person sits behind a desk in the center of the room.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, app, "Majordomo");
        UrbanLocation castle = (UrbanLocation) model.getCurrentHex().getLocation();
        portraitSay("Welcome to the Gentleperson's Club in " + castle.getPlaceName() + ". Are you a member?");
        if (isMember(model)) {
            leaderSay("As a matter of fact, I am.");
            portraitSay("Right this way then.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            handleMember(model);
        } else {
            askToBecomeMember(model);
        }
        println("You leave the Gentleperson's Club.");
    }

    private void handleMember(Model model) {
        println("You step into a grand parlor. There is only one other person here, a tall gentleman in a fancy robe and a top hat.");
        int count = multipleOptionArrowMenu(model, 24, 24, List.of("Talk to gentleman", "Leave"));
        if (count == 0) {
            showRandomPortrait(model, Classes.ARISTOCRAT, "Collector");
            portraitSay("Hello there, nice to see a new face around here.");
            leaderSay("Not very busy in here is it?");
            portraitSay("Not at the moment no.");
            leaderSay("So... do you come here often?");
            portraitSay("Yes. I am a collector of rare objects. I own an auction house and I'm always looking for " +
                    "odd curious from around the world.");
            leaderSay("Interesting. Is that a lucrative business?");
            portraitSay("I have found it so. My clients often pay large sums to for my items, " +
                    "seeing how they are absolutely unique. Of course, I also have to pay well to acquire them " +
                    "in the first place. You wouldn't happen to have come across any rare objects have you?");
            Item it = findRareObject(model);
            if (it != null) {
                leaderSay("Well, I do have this rare bird...");
                println("You bring out the cage with the rare bird in it. The collector's eyes widen with fascination.");
                leaderSay("I believe it is quite unique.");
                portraitSay("Indeed! It's tremendously beautiful, and I don't think I've ever seen anything like it. " +
                        "Are you willing to sell it to me?");
                leaderSay("It depends on the price. What is your offer?");
                int offer = it.getCost() / 2;
                portraitSay("Hmmm... How does " + offer + " gold sound to you?");
                SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this,
                        model.getParty().getLeader(), Skill.Persuade, 10, 20, 0);
                if (result.isSuccessful()) {
                    leaderSay("Now that I think about it, I'm not sure I'm ready to let it go. It's so transfixing to look at.");
                    offer += it.getCost() / 4;
                    portraitSay("I agree... A shame... Would an offer of " + offer + " gold change your mind?");
                } else {
                    leaderSay("Perhaps...");
                }
                print("Sell the rare bird for " + offer + " gold? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("The terms are acceptable to me. Do you have the money here?");
                    portraitSay("Yes, of course. Here it is.");
                    println("The collector hands you " + offer + " gold.");
                    model.getParty().addToGold(offer);
                    leaderSay("Then the bird is yours.");
                    portraitSay("Ah... a real find! Great doing business with you.");
                    leaderSay("My pleasure");
                    model.getParty().getInventory().remove(it);
                } else {
                    leaderSay("... Actually, I'm going to keep the bird for now. It's too precious to me.");
                    portraitSay("I understand. I too would be hesitant to relinquishing something so wonderful.");
                }
            }
        }
    }

    private Item findRareObject(Model model) {
        List<Item> rarities = MyLists.filter(model.getParty().getInventory().getAllItems(),
                (Item it) -> (it instanceof CollectorItem));
        if (rarities.isEmpty()) {
            return null;
        }
        return rarities.get(0);
    }

    private void askToBecomeMember(Model model) {
        leaderSay("Uhm, no... What is this place?");
        portraitSay("This is the Gentleperson's Club, a exclusive sorority for aristocrats, " +
                "artists, actors, musicians or anybody who appreciates the aesthetics of the finer points of life.");
        leaderSay("And what do your members generally do here?");
        portraitSay("Members come here to make connections, discuss arts, exchange ideas or just relax.");
        leaderSay("So anybody can join?");
        portraitSay("Of course. Anybody who can pay the membership fee that is.");
        leaderSay("I see. Just out of curiosity, how much is it?");
        portraitSay("It's just " + MEMBERSHIP_FEE + " gold. Very affordable if you ask me. As a member, you gain access to " +
                "all of our club locations, we're located at each castle in the realm. " +
                "So how about it, should I sign you into our rosters?");
        if (model.getParty().getGold() < MEMBERSHIP_FEE) {
            leaderSay("I don't really think it's very affordable.");
            portraitSay("Well, perhaps not for everybody...");
            leaderSay("We'll just be on our way now.");
        } else {
            print("Do you spend " + MEMBERSHIP_FEE + " to become a member of the Gentleperson's Club? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-MEMBERSHIP_FEE);
                leaderSay("Here's the " + MEMBERSHIP_FEE + " gold. Do we get a membership card or something?");
                portraitSay("No need. We'll make sure all of our locations know your name and appearance, you're one of us now.");
                leaderSay("That's nice of you.");
                portraitSay("Right this way to the club common rooms.");
                model.getSettings().getMiscFlags().put(CLUB_MEMBER_KEY, true);
                model.getLog().addAnimated(LogView.GOLD_COLOR +
                        "You have become a member of the Gentleperson's Club. " +
                        "Their locations will now appear when you visit castles.\n" + LogView.DEFAULT_COLOR);
                handleMember(model);
            } else {
                leaderSay("I think we'll think about it for a while.");
                portraitSay("Yes of course...");
            }
        }
    }

    public static boolean isMember(Model model) {
        return model.getSettings().getMiscFlags().get(CLUB_MEMBER_KEY) != null;
    }
}

package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.special.CageWithBird;
import model.items.special.CollectorItem;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.TrainingState;
import util.MyLists;
import util.MyPair;
import view.LogView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GentlepersonsClubEvent extends DailyEventState {
    private static final String CLUB_MEMBER_KEY = "GentlepersonsClubMember";
    private static final int MEMBERSHIP_FEE = 100;
    private boolean politicianTalkedTo = false;
    private boolean informationBrokerMet = false;

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
        println("You step into a grand parlor. It is filled with fancy plush furniture and the walls are decorated with " +
                "beautiful portraits and ornaments. There are three people here, " +
                "a tall gentleman in a fancy robe and a top hat, somebody who looks like a politician, and an information broker.");
        do {
            int count = multipleOptionArrowMenu(model, 24, 24, List.of("Talk to gentleman",
                    "Talk to politician", "Talk to information broker", "Leave"));
            if (count == 0) {
                talkToCollector(model);
            } else if (count == 1) {
                talkToPolitician(model);
            } else if (count == 2) {
                talkToInformationBroker(model);
            } else {
                break;
            }
        } while (true);
    }

    private void talkToInformationBroker(Model model) {
        showRandomPortrait(model, Classes.SPY, "Information Broker");
        if (informationBrokerMet) {
            portraitSay("I'm sorry, I don't think there's anything more I can teach you.");
            leaderSay("All right. See you around.");
            return;
        }
        portraitSay("Good day sir. Are you new here?");
        leaderSay("Yes. What do you do?");
        portraitSay("I'm an information broker. My job is to know things, and people, and places.");
        leaderSay("Intriguing. How does one attain such a profession?");
        portraitSay("Through years and years of building up a contact network. I was a spy in our lords " +
                "service for many years. Then, when things got a little to hot, I settled down and opened a little " +
                "pub. It was quite popular, not for its food or drink, but for the meetings and deals that were " +
                "made there, it was quite the water hole for greasers and fixers. After a year or so, I decided to sell " +
                "the joint and continue working primarily as a contact specialist. I found the Gentleperson's club to be " +
                "a great place to work my trade.");
        leaderSay("You've got quite the story.");
        portraitSay("Everybody has a story. Why don't you tell me yours?");
        print("Tell your story to the information broker? (Y/N) ");
        if (yesNoInput()) {
            println("You take a little time explaining your adventures to the information broker, who listens attentively.");
            portraitSay("Fascinating. I'm always amazed by the lives of adventurers such as yourselves. Thank you for " +
                    "telling me your story. In return, would you permit you to offer you some advice on making contacts and seeking information?");
            doSkillTraining(model, "information broker", Skill.SeekInfo, "We had better be on our way now.");
            informationBrokerMet = true;
        } else {
            leaderSay("Actually, we need to be on our way.");
            portraitSay("I see. Well, good bye then.");
        }
    }

    private void talkToPolitician(Model model) {
        showRandomPortrait(model, Classes.NOB, "Politician");
        if (politicianTalkedTo) {
            portraitSay("I'm sorry. I really need to focus on this speech now.");
            leaderSay("Sorry...");
            return;
        }
        portraitSay("Hello there. You wouldn't happen to know a synonym for 'society' do you?");
        leaderSay("Uhm... maybe 'community'?");
        portraitSay("Yes! That will work.");
        leaderSay("What are you doing?");
        portraitSay("Oh, I'm working on a speech for the local authority here. Times have been a bit tough, so it needs " +
                "to be particularly inspiring.");
        leaderSay("Oh I see. You're a politician?");
        portraitSay("Yes, or at least I was. I used to be quite famous.");
        leaderSay("What happened?");
        portraitSay("Due a, uhm misunderstanding a few years a go I had to resign my office.");
        leaderSay("What kind of misunderstanding?");
        portraitSay("I'd rather not get into that. Anyway nowadays I function more as a leadership coach. Say, in return for " +
                "helping me with my speech, maybe I can teach you a think or two about being a leader?");
        doSkillTraining(model, "politician", Skill.Leadership,
                iOrWeCap() + " had better let you get back to your speech.");
        politicianTalkedTo = true;
    }

    private void doSkillTraining(Model model, String trainer, Skill skill, String endText) {
        print("Permit the " + trainer + " to give you a lesson in " + skill.getName() + "? (Y/N) ");
        if (yesNoInput()) {
            GameCharacter student = model.getParty().getLeader();
            if (model.getParty().size() > 1) {
                print("Who should take the lesson? ");
                student = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            partyMemberSay(student, "I'll could use a lesson.");
            portraitSay("Wonderful. At what level would you like the lesson to be?");
            int level = multipleOptionArrowMenu(model, 24, 24, new ArrayList<>(Arrays.asList(TrainingState.LEVELS)));
            portraitSay("Perfect. Let's get started.");
            boolean result = TrainingState.doSkillTraining(model, this, student, skill, level);
            if (!result) {
                portraitSay("I'm sorry, I don't think we're on the same page here.");
                partyMemberSay(student, "I guess not.");
            } else {
                partyMemberSay(student, "That was a real eye-opener!");
                portraitSay("At your service!");
            }
            leaderSay(endText);
            portraitSay("Good day to you.");
        } else {
            leaderSay("Actually, we have to get going.");
            portraitSay("Fair enough. See you around.");
        }
    }

    private void talkToCollector(Model model) {
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
        } else {
            leaderSay("I'm afraid I don't have anything like that.");
            portraitSay("I see. Well, nice talking to you. Good day.");
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

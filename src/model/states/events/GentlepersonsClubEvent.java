package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.horses.HorseItemAdapter;
import model.items.Item;
import model.items.potions.FineWine;
import model.items.special.CollectorItem;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.races.AllRaces;
import model.states.DailyEventState;
import model.states.TrainingState;
import model.tasks.DestinationTask;
import model.tasks.JoinGentlepersonsClubTask;
import util.*;
import view.LogView;
import view.subviews.CollapsingTransition;
import view.subviews.GuildHallImageSubView;
import view.subviews.PortraitSubView;

import java.util.*;

public class GentlepersonsClubEvent extends DailyEventState {
    private static final String CLUB_MEMBER_KEY = "GentlepersonsClubMember";
    private static final int MEMBERSHIP_FEE = 100;
    private static final Integer RATING_MAX_RANK = 5;
    private UrbanLocation castleLocation;

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
        this.castleLocation = (UrbanLocation) model.getCurrentHex().getLocation();
        CollapsingTransition.transition(model, GuildHallImageSubView.getInstance("Gentleperson's Club"));
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
            leaderSay("Thanks. Just gonna get a drink.");
            portraitSay("Of course, help yourselves.");
            giveDrinks(model);
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            handleMember(model);
        } else {
            askToBecomeMember(model);
            if (!isMember(model) && !MyLists.any(model.getParty().getDestinationTasks(), dt ->
                    dt instanceof JoinGentlepersonsClubTask)) {
                model.getParty().addDestinationTask(new JoinGentlepersonsClubTask(model));
                JournalEntry.printJournalUpdateMessage(model);
            }
        }
        println("You leave the Gentleperson's Club.");
    }

    private void handleMember(Model model) {
        List<ClubPerson> peopleHere = makePeopleInClub();
        println("You step into a grand parlor. It is filled with fancy plush furniture and the walls are decorated with " +
                "beautiful portraits and ornaments. There are " + MyStrings.numberWord(peopleHere.size()) + " people here.");
        do {
            println("Who would you like to approach?");
            List<String> options = MyLists.transform(peopleHere, ClubPerson::getName);
            options.add("Leave club");
            int count = multipleOptionArrowMenu(model, 24, 24, options);
            if (count == peopleHere.size()) {
                break;
            }
            peopleHere.get(count).handlePerson(model);
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        } while (true);
    }

    private List<ClubPerson> makePeopleInClub() {
        List<ClubPerson> people = new ArrayList<>(List.of(
                new Collector(), new Politician(), new InformationBroker(),
                new Screenwriter(), new Mathematician(), new Diplomat(),
                new RetiredAdventurer()));
        return MyRandom.sampleN(
                MyRandom.randInt(2, 5),
                people);
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
        portraitSay("This is the Gentleperson's Club, an exclusive sorority for aristocrats, " +
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

                DestinationTask task = MyLists.find(model.getParty().getDestinationTasks(),
                        dt -> dt instanceof JoinGentlepersonsClubTask);
                if (task != null) {
                    ((JoinGentlepersonsClubTask)task).setCompleted(true);
                }
                portraitSay("Right this way to the club common rooms. Oh, and members always receive a free drink upon arrival.");
                randomSayIfPersonality(PersonalityTrait.gluttonous, new ArrayList<>(), "Free booze? This place is great!");
                leaderSay("Sure, we'll have a drink");
                giveDrinks(model);
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
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

    private void giveDrinks(Model model) {
        for (int i = 0; i < model.getParty().size(); ++i) {
            new FineWine().addYourself(model.getParty().getInventory());
        }
        println("You collect " + MyStrings.numberWord(model.getParty().size()) + " drinks from the bar.");
    }

    public static boolean isMember(Model model) {
        return model.getSettings().getMiscFlags().get(CLUB_MEMBER_KEY) != null;
    }

    private static abstract class ClubPerson {

        private final String name;

        protected boolean alreadyTalkedTo = false;

        public ClubPerson(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void handlePerson(Model model);
    }

    private class Collector extends ClubPerson {
        private final AdvancedAppearance appearance;

        public Collector() {
            super("Gentleman");
            this.appearance = PortraitSubView.makeRandomPortrait(Classes.ARISTOCRAT);
        }

        @Override
        public void handlePerson(Model model) {
            showExplicitPortrait(model, appearance, "Collector");
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
                SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, GentlepersonsClubEvent.this,
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
    }

    private class Politician extends ClubPerson {
        private final AdvancedAppearance appearance;

        public Politician() {
            super("Politician");
            this.appearance = PortraitSubView.makeRandomPortrait(Classes.NOB);
        }

        @Override
        public void handlePerson(Model model) {
            showExplicitPortrait(model, appearance, "Politician");
            if (alreadyTalkedTo) {
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
            alreadyTalkedTo = true;
        }
    }

    private class InformationBroker extends ClubPerson {
        private final AdvancedAppearance appearance;

        public InformationBroker() {
            super("Information Broker");
            this.appearance = PortraitSubView.makeRandomPortrait(Classes.SPY);
        }

        @Override
        public void handlePerson(Model model) {
            showExplicitPortrait(model, appearance, "Information Broker");
            if (alreadyTalkedTo) {
                portraitSay("I'm sorry, I don't think there's anything more I can teach you.");
                leaderSay("All right. See you around.");
                return;
            }
            portraitSay("Good day sir. Are you new here?");
            leaderSay("Yes. What do you do?");
            portraitSay("I'm an information broker. My job is to know things, and people, and places.");
            leaderSay("Intriguing. How does one attain such a profession?");
            portraitSay("Through years and years of building up a contact network. I was a spy in the " + castleLocation.getLordTitle() + "'s " +
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
                alreadyTalkedTo = true;
            } else {
                leaderSay("Actually, we need to be on our way.");
                portraitSay("I see. Well, good bye then.");
            }
        }
    }

    private class Screenwriter extends ClubPerson {
        private final AdvancedAppearance appearance;

        public Screenwriter() {
            super("Screenwriter");
            this.appearance = PortraitSubView.makeRandomPortrait(Classes.BRD);
        }

        @Override
        public void handlePerson(Model model) {
            println("You approach a person who has a large a fancy hat.");
            showExplicitPortrait(model, appearance, "Screenwriter");
            if (alreadyTalkedTo) {
                portraitSay("I really must get on with the script now. Please don't disturb me.");
                leaderSay("Of course. " + iOrWeCap() + "'ll let you work.");
                return;
            }
            leaderSay("Hello there. Working on something?");
            portraitSay("Yes, it's my latest play! I must have the script ready by next week.");
            leaderSay("Anything " + iOrWe() + " can do to help?");
            portraitSay("Perhaps. I'm having a hard time with the ending actually. May I pick your brain?");
            print("Do you help the screenwriter with the script? (Y/N) ");
            if (!yesNoInput()) {
                leaderSay("I don't think " + iOrWe() + " are of much help to you.");
                portraitSay("I understand. Good bye then.");
                leaderSay("Bye.");
                return;
            }
            leaderSay("Certainly. What's the story?");
            portraitSay("A king has died, and his three daughters are plunging the kingdom into political turmoil " +
                    "over the matter of succession. The eldest daughter has the birthright, but not a lot of popular support, " +
                    "the people favor the youngest sister. The middle sister has the support of the military.");
            leaderSay("So it's just about court intrigue?");
            portraitSay("No no, that's just the setup. The situation gets shaken up when the kingdom " +
                    "is assailed by a furious host of undead legions. The three sisters are forced to work together " +
                    "to repel the invaders.");
            leaderSay("Oh my goodness. So it's a war story?");
            portraitSay("Heavens no. The sisters learn that the undead army is controlled by an evil sorcerer, " +
                    "who's seated in a dark stronghold in a faraway land. They need travel there together to defeat him!");
            leaderSay("Uh-huh... is that it?");
            portraitSay("No no, you haven't even heard the third act yet.");
            leaderSay("Which is?");
            portraitSay("A wonderful plot twist! The sorcerer is actually the king's father, the princesses grandfather! " +
                    "Now, about the ending...");
            print("What suggestion do you have for the screenwriter's play? ");
            int count = multipleOptionArrowMenu(model, 24, 24, List.of("Catharsis and compromise",
                    "Betrayal and blood", "Reduce complexity"));
            if (count == 0) {
                leaderSay("The three sisters should work together to defeat the sorcerer. Then rule the kingdom in a triumvirate.");
                portraitSay("Yes of course. That would reverberate well with the rest of the story. A fine way to wrap it up. I " +
                        "think the audience will love it.");
            } else if (count == 1) {
                leaderSay("One sisters should betray the others by joining with the sorcerer grandfather.");
                portraitSay("Another turn of events? I love it! The audience will never see that coming!");
                leaderSay("... no, I'm sure they won't.");
            } else {
                leaderSay("Uhm... how about cutting the third act? Sounds like you've got enough going on without the sorcerer grandfather.");
                portraitSay("Hmm... yes, perhaps you are right. I'll take it under advisement.");
            }
            portraitSay("Say, in return for helping me with the script. Would you consider letting me " +
                    "teaching you a thing or two about the grand stage?");
            doSkillTraining(model, "Screenwriter", Skill.Entertain, "You better get back to your script.");
            alreadyTalkedTo = true;
        }
    }

    private class Mathematician extends ClubPerson {
        private final CharacterAppearance appearance;

        public Mathematician() {
            super("Mathematician");
            this.appearance = PortraitSubView.makeOldPortrait(Classes.PROFESSOR, AllRaces.randomRace(), MyRandom.flipCoin());
        }

        @Override
        public void handlePerson(Model model) {
            showExplicitPortrait(model, appearance, "Mathematician");
            portraitSay("Hello there. Nice to see you again.");
            if (alreadyTalkedTo) {
                leaderSay("Hmmm... I guess your little habit does makes sense to me now.");
                portraitSay("See what I mean? However, I don't think I can teach you anything more right now. If you " +
                        "want to know more, come visit me at the university.");
                leaderSay("Okay then...");
                return;
            }
            leaderSay("Uhm, I don't think we've met before.");
            portraitSay("Are you sure, you look familiar.");
            leaderSay("I'm certain.");
            portraitSay("I'm sorry. I've met so many students in my time, all faces just seem kind of familiar to me.");
            leaderSay("So you just assume you know everybody?");
            portraitSay("I'm right more often than not.");
            leaderSay("You are a teacher?");
            portraitSay("Yes. I'm a mathematician. I teach classes at the college here, in trigonometry, algebra and combinatorics.");
            leaderSay("Sounds advanced. I'm surprised so many students need to learn these subjects.");

            portraitSay("Mathematics is central to many aspects in common life. If you'd permit me, I could " +
                    "instruct you briefly, then perhaps you would see the use of it.");
            doSkillTraining(model, "Mathematician", Skill.Logic, iOrWeCap() + " better not take up any more of your time.");
            alreadyTalkedTo = true;
        }
    }

    private class Diplomat extends ClubPerson {
        private final AdvancedAppearance appearance;

        public Diplomat() {
            super("Diplomat");
            this.appearance = PortraitSubView.makeRandomPortrait(Classes.NOB);
        }

        @Override
        public void handlePerson(Model model) {
            showExplicitPortrait(model, appearance, "Diplomat");
            if (alreadyTalkedTo) {
                portraitSay("Oh, it's you again. We could share stories. But I don't think I can teach you anything else.");
                leaderSay("Okay. Until we meet again then.");
                portraitSay("Bye");
                return;
            }
            portraitSay("Good day to you. Would you share a drink to me?");
            leaderSay("Sure. What do you do?");
            portraitSay("I'm an envoy in " + castleLocation.getLordTitle() + " " + castleLocation.getLordName() + "'s service. " +
                    "I sometimes travel to other kingdoms to negotiate with their regents.");
            leaderSay("That sounds like tricky work.");
            portraitSay("It certainly can be. It's not always easy to persuade the advisors of other courts. Quite often there is " +
                    "a great deal of conflict of interest.");
            leaderSay("How long have you been a diplomat?");
            portraitSay("It must be, oh, five years now. I was a domestic advisor before attaining this position.");
            leaderSay("Then you must be quite good at your work.");
            portraitSay("I would say so. Although, sometimes I wonder if the " + castleLocation.getLordTitle() + " didn't make me " +
                    "an envoy to get me away from the court. I can be rather influential when I want to.");
            leaderSay("A very useful trait. Do you have any advice for a layman?");
            portraitSay("Perhaps. I could maybe give you a hint or two about the finer points of the art of negotiation.");
            doSkillTraining(model, "Diplomat", Skill.Persuade, "Perhaps we'll meet again someday, at another castle.");
            alreadyTalkedTo = true;
        }
    }

    private class RetiredAdventurer extends ClubPerson {
        private final CharacterAppearance portrait;

        public RetiredAdventurer() {
            super("Retired adventurer");
            this.portrait = PortraitSubView.makeOldPortrait(Classes.SWORD_MASTER, AllRaces.randomRace(), MyRandom.flipCoin());
        }

        @Override
        public void handlePerson(Model model) {
            if (alreadyTalkedTo) {
                println("You look around a bit, but it seems the retired adventurer has left the Club.");
                return;
            }
            showExplicitPortrait(model, portrait, "Retired adventurer");
            alreadyTalkedTo = true;
            leaderSay("I don't think there's anybody else in here who carries a sword on their back.");
            portraitSay("It's an old habit. I haven't taken it out of its scabbard in years. But I never take it off. " +
                    "It's one of my principles. It's kept me alive many a time.");
            leaderSay("Had many adventures?");
            portraitSay("Too many to count. But my success is not my achievement alone. I had a good party, " +
                    "but we got off the path many years ago. I've settled down here in " + castleLocation.getPlaceName() + ". " +
                    "I count myself lucky. Many adventurers don't live long enough to get the spend the gold they made.");
            leaderSay("What was your party called?");
            String partyName = MyRandom.sample(List.of("Magnificent Mates", "Superb Squad", "Prominent Pack",
                    "Glorious Group", "Courageous Clique"));
            portraitSay("We were called 'The " + partyName + "'. Pretty good name huh?");
            leaderSay("I think I've heard of you!");
            portraitSay("I'm not surprised, we were pretty famous.");
            leaderSay("Can you give me and my party any adventuring advice?");
            if (model.getParty().size() < 3) {
                portraitSay("I would offer to rate your party, but seeing as there is so few of you " +
                        "my general advice would be to recruit more party members!");
                leaderSay("That makes sense, I guess.");
                portraitSay("Good luck with your adventuring.");
                leaderSay("Thanks. Bye now.");
                return;
            }
            portraitSay("I can rate your party if you want.");
            print("Let the retired adventurer rate the party? (Y/N) ");
            if (!yesNoInput()) {
                leaderSay("Naw.. I think we're good. Thanks anyway.");
                portraitSay("You bet. Good luck!");
            }
            leaderSay("Sure, how do usually go about doing that?");
            portraitSay("Well, first I'll have to ask your party members some questions. Then I'll make an assessment on " +
                    "how well your party is doing in the categories; combat, wealth and skills. Oh, and I should tell you, I'm " +
                    "no mage, so any assessment made by me doesn't factor in spells or magic in any way.");
            leaderSay("Okay then. Let's get started.");
            println("The retired adventurer takes a long time asking each party member many questions and meticulously jotting down the answers. " +
                    "Then " + heOrShe(portrait.getGender()) + " he scribbles down what looks like some calculations.");

            MyPair<Integer, String> combatAssessment = getCombatAssessment(model);
            portraitSay("Considering each party members fortitude, agility, combat skill and protective gear, " +
                    "I give your entire party a combat score of " + combatAssessment.first + ".");
            leaderSay("Is that good?");
            portraitSay("I would say it's pretty " + combatAssessment.second + ".");
            leaderSay("Any suggestions for how to boost it?");
            if (model.getParty().size() < 6) {
                portraitSay("The best way of boosting your combat power is probably just hiring on some more hands.");
            } else {
                int dieRoll = MyRandom.rollD6();
                if (dieRoll <= 2) {
                    portraitSay("I recommend changing classes of some of your party members into fighters.");
                } else if (dieRoll <= 4) {
                    portraitSay("I recommend equipping your party members with better armor.");
                } else {
                    portraitSay("I recommend equipping your party members with better weapons.");
                }
            }

            MyPair<Integer, String> wealthAssessment = getWealthAssessment(model);
            portraitSay("Moving on to wealth. Considering not only your cash on hand, but the value of your equipped gear, " +
                    "the items in your inventory, and your mounts, " +
                    "I give your party a wealth score of " + wealthAssessment.first/10 + ".");
            leaderSay("That's a number... what does it mean?");
            portraitSay("It means that the current economic situation of your party is " + wealthAssessment.second + ".");
            leaderSay("I see.");

            portraitSay("Finally, I have analyzed your skill coverage.");
            leaderSay("What is that?");
            portraitSay("It is a metric indicating how well your party members complement each " +
                    "other in different proficiencies.");
            leaderSay("Ah, I see. And how are we doing in that regard?");
            List<MyPair<Skill, Integer>> deficiencies = calculateSkillDeficiencies(model);
            MyPair<Integer, String> skillAssessment = getSkillAssessment(model, deficiencies);
            if (skillAssessment.first == 0) {
                portraitSay("I dare say your skill coverage is nearly perfect. My scoring method gives a " +
                        "higher score the poorer the coverage. Your party's score was zero!");
                leaderSay("That's a relief!");
            } else {
                portraitSay("Your skill coverage score is " + skillAssessment.first +
                        ", the lower that score is the better, with a score of zero being 'perfect'.");
                leaderSay("So " + skillAssessment.first + " is...");
                portraitSay("In my opinion, it is " + skillAssessment.second + ".");
                leaderSay("How could we improve our skill coverage?");
                String suggestedHire = findSuggestedHire(model, deficiencies);
                portraitSay("Well, apart from equipping your party members with gear which covers up the weak spot in your coverage, " +
                        "I would recommend recruiting a " + suggestedHire + ", or possibly changing the class of one of your party members into a " + suggestedHire + ".");
                leaderSay("Interesting. We'll keep that in mind.");
            }
            portraitSay("That's it, my professional opinion. I hope it helps you somehow.");
            leaderSay("It's given us a bit to think about at least.");
            portraitSay("Great. Well, it was nice meeting you. I'm going to leave now. Perhaps we'll meet again sometime.");
            leaderSay("I hope we will.");
        }

        private MyPair<Integer, String> getCombatAssessment(Model model) {
            int combatScore = (int)MyLists.doubleAccumulate(model.getParty().getPartyMembers(),
                    character ->
                            character.getMaxHP()/3.0 +
                                    character.getSpeed()/2.0 +
                                    character.getAP() +
                                    character.calcAverageDamage());
            String assessment = findLevel(combatScore,
                                          new int[]{         50,    100,    150,    200,    250,     300,     350},
                                          new String[]{"dismal", "poor", "okay", "fair", "good", "great", "superb", "excellent"});
            return new MyPair<>(combatScore, assessment);
        }

        private MyPair<Integer, String> getWealthAssessment(Model model) {
            int goldSum = MyLists.intAccumulate(model.getParty().getInventory().getAllItems(), Item::getCost);
            goldSum += model.getParty().getGold() + model.getParty().getObols() / 200;
            goldSum += MyLists.intAccumulate(model.getParty().getPartyMembers(),
                    (GameCharacter gc) ->
                            gc.getEquipment().getWeapon().getCost() +
                            gc.getEquipment().getClothing().getCost() +
                                    (gc.getEquipment().getAccessory() != null ?
                                            gc.getEquipment().getAccessory().getCost() :
                                            0));
            goldSum += MyLists.intAccumulate(model.getParty().getHorseHandler().getHorsesAsItems(), (HorseItemAdapter::getCost));
            String assessment = findLevel(goldSum,
                    new int[]{            100,        200,       400,    800,        1500,     3000},
                    new String[]{"really bad", "not good", "alright", "good", "very good",  "great", "fantastic"});
            return new MyPair<>(goldSum, assessment);
        }

        private List<MyPair<Skill, Integer>> calculateSkillDeficiencies(Model model) {
            List<MyPair<Skill, Integer>> skillDeficiencies = new ArrayList<>();
            for (Skill s : Skill.values()) {
                if (s != Skill.UnarmedCombat) {
                    int maxRank = MyLists.maximum(model.getParty().getPartyMembers(),
                            (GameCharacter gc) -> gc.getRankForSkill(s));
                    skillDeficiencies.add(new MyPair<>(s, RATING_MAX_RANK - Math.min(maxRank, 5))); // No rank higher than 5 considered.
                }
            }
            skillDeficiencies.sort(Comparator.comparingInt(p -> p.second));
            return skillDeficiencies;
        }

        private MyPair<Integer, String> getSkillAssessment(Model model, List<MyPair<Skill, Integer>> skillDeficiencies) {
            List<MyPair<Skill, Integer>> worstSkills = new ArrayList<>(skillDeficiencies);
            int score = MyLists.intAccumulate(skillDeficiencies, (MyPair<Skill, Integer> p) -> p.second);
            Collections.reverse(worstSkills);
            System.out.println("Worst skills:");
            for (MyPair<Skill, Integer> p : worstSkills) {
                System.out.println(p.first.getName() + ": " + p.second);
            }

            String assessment = findLevel(score,
                    new int[]{           1,       10,           20,       30,          50},
                    new String[]{"perfect", "great", "pretty good", "decent", "not great", "lacking"});

            return new MyPair<>(score, assessment);
        }


        private String findSuggestedHire(Model model, List<MyPair<Skill, Integer>> deficiencies) {
            List<MyPair<CharacterClass, Integer>> distances = new ArrayList<>();
            for (CharacterClass charClass : Classes.allClasses) {
                int distance = 0;
                for (MyPair<Skill, Integer> p : deficiencies) {
                    int weight = Math.min(RATING_MAX_RANK, charClass.getWeightForSkill(p.first));
                    int diff = Math.min(p.second, weight) - p.second;
                    distance += diff*diff;
                }
                distances.add(new MyPair<>(charClass, distance));
            }
            distances.sort(Comparator.comparingInt(t -> t.second));
            return distances.get(0).first.getFullName();
        }

        private String findLevel(int score, int[] levels, String[] levelNames) {
            for (int i = 0; i < levels.length; ++i) {
                if (score < levels[i]) {
                    return levelNames[i];
                }
            }
            return levelNames[levels.length];
        }
    }
}

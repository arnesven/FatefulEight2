package model.states.events;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.FacialExpression;
import model.characters.appearance.InvisibleAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.PermanentlyInvisibleCondition;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.races.ElvenRace;
import model.races.Race;
import model.states.DailyEventState;
import model.tasks.DestinationTask;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;

import java.awt.*;
import java.util.List;

public class InvisibleEvent extends PersonalityTraitEvent {
    public InvisibleEvent(Model model, PersonalityTrait trait, GameCharacter main) {
        super(model, trait, main);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex().getLocation() instanceof UrbanLocation;
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        model.getParty().benchPartyMembers(
                MyLists.filter(model.getParty().getPartyMembers(), gc -> gc != main));
        println(main.getName() + " is browsing the market and comes to a fruit stand.");
        showRandomPortrait(model, Classes.MERCHANT, "Fruitseller");
        portraitSay("Fruit! Get exotic fruit here! Care for an apple " + (main.getGender() ? "madam":"sir") + ". Or a pear perhaps?");
        partyMemberSay(main, "That does sound rather yummy. I think I'll have one... " +
                "Wait, what are those fruit back there?");
        portraitSay("You mean these Ogeon?");
        println("The fruitseller points to a bunch of very bright orange things in a basket.");
        partyMemberSay(main, "Yes, those look absolutely delicious. I'll have one of those.");
        portraitSay("They are, but I'm afraid I can't sell you those " + (main.getGender() ? "madam":"sir") + ".",
                FacialExpression.sad);
        partyMemberSay(main, "Why not?", FacialExpression.disappointed);
        portraitSay("They're known to be poisonous to " + makeRacePlural(main.getRace()) + "!");
        partyMemberSay(main, "Rubbish! I have a belly like an iron pot. I simply must taste it. " +
                "Now bring it here.");
        portraitSay("I'm sorry, but I can't sell it to you. I could lose my license!", FacialExpression.disappointed);
        partyMemberSay(main, "Hmmph! Then I shan't have anything else either!", FacialExpression.disappointed);
        portraitSay("Fine. Come back if you change your mind.");
        println("Another customer approaches the fruit stand and the fruitseller turns " +
                hisOrHer(getPortraitGender()) + " attention away from " + main.getFirstName() + ".");
        print("The tantalizing fruit is just within " + main.getFirstName() + "'s reach. Do you grab the Ogeon and run? (Y/N) ");
        if (!yesNoInput()) {
            partyMemberSay(main, "It's tempting, but why risk it?");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            println(main.getName() + " returns to the party.");
            model.getParty().unbenchAll();
            leaderSay("Did you find anything interesting at the market " + main.getFirstName() + "?");
            partyMemberSay(main, "Forbidden fruit, quite literally.");
            return;
        }

        println(main.getFirstName() + " snatches one of the Ogeon from the basket and quickly makes a run for it.");
        portraitSay("Hey! Stop! Thief!", FacialExpression.angry);
        println("The fruitseller tries to get the attention of the constables, but " + main.getFirstName() +
                " quickly blends in to the crowd at the busy market.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println(main.getFirstName() + " gets to a back street and stops to catch " + hisOrHer(main.getGender()) + " breath.");
        println(heOrSheCap(main.getGender()) + " holds up the strange fruit to inspect it more closely. " +
                "It's orange hue almost shimmers in the afternoon light.");
        partyMemberSay(main, "Spectacular! I've never seen anything like it.", FacialExpression.excited);
        println(heOrSheCap(main.getGender()) + " takes a bite. The taste is sweet, juicy and rich.");
        model.getLog().waitForAnimationToFinish();
        main.setAppearance(new InvisibleAppearance(main));
        main.addCondition(new PermanentlyInvisibleCondition());
        partyMemberSay(main, "So good!");
        println(main.getFirstName() + " finishes eating the fruit and promptly returns to the party.");
        println("As " + heOrShe(main.getGender()) + " makes " + hisOrHer(main.getGender()) + " through town, " +
                heOrShe(main.getGender())+ " notices people are looking and pointing at " + himOrHer(main.getGender()) + ".");
        partyMemberSay(main, "What's all the commotion about? Oh well...");
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
        GameCharacter leader = model.getParty().getLeader();
        println("As " + heOrShe(main.getGender()) + " approaches, " + leader.getName() + " appears startled.");
        partyMemberSay(main, "I'm back. What did I miss?");
        if (leader.hasPersonality(PersonalityTrait.cowardly) || leader.hasPersonality(PersonalityTrait.anxious)) {
            leaderSay("Yikes! A g-g-g ghost!", FacialExpression.afraid);
        } else if (leader.hasPersonality(PersonalityTrait.irritable) ||leader.hasPersonality(PersonalityTrait.aggressive)) {
            leaderSay("I'm not afraid of you ghost!", FacialExpression.disappointed);
        } else {
            leaderSay("Uhm, " + main.getFirstName() + "? Is that you? Did you die and turn into a ghost?", FacialExpression.questioning);
        }
        partyMemberSay(main, "Ghost? What are you talking about?");
        leaderSay("We can't really see you " + main.getFirstName() + "...");
        partyMemberSay(main, "You mean I'm invisible?");
        println(main.getFirstName() + " looks down at " + hisOrHer(main.getGender()) + " body.");
        leaderSay("Well, not your clothes and gear, just you. Look at your hands.");
        partyMemberSay(main, "Oh my word! You're right. What in the world...");
        leaderSay("What happened to you?");
        partyMemberSay(main, "The fruit... it must have been... Curse my damn insatiable appetite!");
        leaderSay("A fruit turned you invisible.");
        partyMemberSay(main, "It was a particularly rare variety. I'd never seen one before. " +
                "My darn appetite got the better of me. The fruitseller tried to warn me, " +
                "said it was poisonous for " + makeRacePlural(main.getRace()) + ".");
        leaderSay("And you didn't listen!", FacialExpression.disappointed);
        partyMemberSay(main, "I just dismissed it as the normal niminy-piminy. I didn't think I would get cursed!");
        leaderSay("Maybe the effect will wear off?");
        partyMemberSay(main, "Maybe... let's wait and see.");
        println("The party waits for several hours but " + main.getName() + "'s condition does not change.");
        partyMemberSay(main, "I'm starting to freak out a little bit here!");
        leaderSay("Calm down. There must be some kind of cure. Can you remember what the fruit was called?");
        partyMemberSay(main, "No... Wait, yes. It was O... o... something.");
        GameCharacter expertInParty = null;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != main) {
                SkillCheckResult result = gc.testSkillHidden(Skill.Academics, 10, gc.getRankForSkill(Skill.MagicGreen) / 2);
                if (result.isSuccessful()) {
                    expertInParty = gc;
                    println(gc.getName() + " suddenly recalls something (Academics " + result.asString() + ").");
                    break;
                }
            }
        }

        if (expertInParty != null) {
            partyMemberSay(expertInParty, "Ogeon? Yes, that fruit is said to have magical properties. " +
                    "I didn't know it turned " + makeRacePlural(main.getRace()) + " invisible though.");
            partyMemberSay(main, "Do you know if there is a cure?");
            partyMemberSay(expertInParty, "I have no idea. What we need is an enchanter or alchemist, " +
                    "probably one who's an expert in botany.");
            partyMemberSay(main, "Okay... Do you know anybody like that?");
            model.getWorld().dijkstrasByLand(model.getParty().getPosition());
            Point position = model.getWorld().shortestPathToNearestTownOrCastle(4).getLast();
            WorldHex hex = model.getWorld().getHex(position);
            UrbanLocation townOrCastle = (UrbanLocation) hex.getLocation();
            partyMemberSay(expertInParty, "In fact I do. There's a famous enchanter in " + townOrCastle.getPlaceName() +
                    " called 'the great' Grumbledook. He's been around for ages and knows everything about enchantments, curses " +
                    "and other magical oddities. We should go visit him.");
            partyMemberSay(main, "Alright. Let's go right away. I don't want to stay this way!");
            model.getParty().addDestinationTask(new FindGrumbledookTask(position, townOrCastle, main));
            JournalEntry.printJournalUpdateMessage(model);
        } else {
            leaderSay("Was it an orange?", FacialExpression.questioning);
            partyMemberSay(main, "Of course it wasn't an orange! Oranges don't turn people invisible!");
            leaderSay("Fine. I'm only trying to help...");
            partyMemberSay(main, "Ogeon! It was called an ogeon.");
            leaderSay("Okay, great. We'll have to ask around town if anybody knows anything more about it.");
            model.getParty().addDestinationTask(new AskAroundTownDestinationTask(model.getParty().getPosition(), main));
            JournalEntry.printJournalUpdateMessage(model);
        }

    }

    private static String makeRacePlural(Race race) {
        if (race.id() == Race.DWARF.id()) {
            return "dwarves";
        }
        if (race instanceof ElvenRace) {
            return "elves";
        }
        return race.getName().toLowerCase() + "s";
    }

    private static class AskAroundTownDestinationTask extends DestinationTask {
        private final GameCharacter victim;
        private boolean completed = false;

        public AskAroundTownDestinationTask(Point position, GameCharacter victim) {
            super(position, "");
            this.victim = victim;
        }

        @Override
        public JournalEntry getJournalEntry(Model model) {
            return new JournalEntry() {
                @Override
                public String getName() {
                    return "Ask about Ogeons";
                }

                @Override
                public String getText() {
                    if (isComplete()) {
                        return  "You found somebody who knew about the 'Ogeon' fruit. Apparently the great " +
                                "enchanter, Grumbledook, could help with a remedy for the problem.\n\n" +
                                "Completed";
                    }
                    return "Ask around town if anybody knows about a magical fruit called 'Ogeon', which can turn " +
                            makeRacePlural(victim.getRace()) + " invisible, and if there's a cure.";
                }

                @Override
                public boolean isComplete() {
                    return AskAroundTownDestinationTask.this.isCompleted();
                }

                @Override
                public boolean isFailed() {
                    return false;
                }

                @Override
                public boolean isTask() {
                    return true;
                }

                @Override
                public Point getPosition(Model model) {
                    return null;
                }
            };
        }

        @Override
        public JournalEntry getFailedJournalEntry(Model model) {
            return getJournalEntry(model);
        }

        @Override
        public boolean drawTaskOnMap(Model model) {
            return false;
        }

        @Override
        public DailyAction getDailyAction(Model model) {
            return new DailyAction("Ask about Ogeon", new AskAroundTownEvent(model, victim, this));
        }

        @Override
        public boolean isFailed(Model model) {
            return false;
        }

        @Override
        public boolean givesDailyAction(Model model) {
            return !isCompleted() && model.getCurrentHex().getLocation() instanceof UrbanLocation;
        }

        @Override
        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean b) {
            this.completed = b;
        }
    }

    private static class AskAroundTownEvent extends DailyEventState {
        private final GameCharacter victim;
        private final AskAroundTownDestinationTask task;

        public AskAroundTownEvent(Model model, GameCharacter victim,
                                  AskAroundTownDestinationTask askAroundTownDestinationTask) {
            super(model);
            this.victim = victim;
            this.task = askAroundTownDestinationTask;
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            partyMemberSay(victim, "I don't want to stay like this forever.");
            if (victim != model.getParty().getLeader()) {
                leaderSay("Okay okay, we'll see if we can find somebody who knows anything about those 'Ogeon' things.");
            } else {
                leaderSay("Let's see if we can find somebody who knows anything about those 'Ogeon' things.");
            }
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 10);
            if (!success) {
                println("The party spends the hole day trying to find somebody who knows anything about the mysterious fruit. " +
                        "But nobody seems to have any good leads.");
                leaderSay("We'll just have to keep trying I guess.", FacialExpression.relief);
                partyMemberSay(victim, "This is the worst...");
                return;
            }

            CharacterClass cls = MyRandom.sample(List.of(Classes.WIZ, Classes.WIT, Classes.MAG, Classes.PRI, Classes.DRU));
            println("The party finally encounters a " + cls.getFullName().toLowerCase() + " who knows something about the 'Ogeon'.");
            showRandomPortrait(model, cls, cls.getFullName());
            portraitSay("Yes, I've heard about this sort of thing happening before.");
            partyMemberSay(victim, "Is there anything to be done?");
            portraitSay("I don't know. But I'm sure the Great Grumbledook knows.");
            partyMemberSay(victim, "Grumbledook? Who's that?");
            model.getWorld().dijkstrasByLand(model.getParty().getPosition());
            Point position = model.getWorld().shortestPathToNearestTownOrCastle(3).getLast();
            WorldHex hex = model.getWorld().getHex(position);
            UrbanLocation townOrCastle = (UrbanLocation) hex.getLocation();
            portraitSay("He's a famous enchanter, you haven't heard of him? " +
                    "He's been around for ages and knows everything about enchantments, curses " +
                    "and other magical oddities.");
            leaderSay("No we haven't heard of him. Where can we find him?");
            portraitSay("If I'm not mistaken, he lives in " + townOrCastle.getPlaceName() + ".");
            leaderSay("Hmm... " + MyStrings.capitalize(townOrCastle.getPlaceName()) + " isn't too far from here...");
            partyMemberSay(victim, "Let's go right away. I don't want to stay this way!");
            leaderSay("Thank you " + cls.getFullName() + " for your help.");
            portraitSay("I'm happy I could be of service.");
            task.setCompleted(true);
            model.getParty().addDestinationTask(new FindGrumbledookTask(position, townOrCastle, victim));
            JournalEntry.printJournalUpdateMessage(model);
        }
    }
}

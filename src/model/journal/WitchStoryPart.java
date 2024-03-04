package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.Quest;
import model.quests.SpecialDeliveryQuest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import model.states.events.ChangeClassEvent;
import view.sprites.Sprite;
import view.sprites.SpriteQuestMarker;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WitchStoryPart extends StoryPart {

    private static final int FIND_WITCH = 1;
    private static final int DO_QUEST = 2;
    private static final int QUEST_DONE = 3;
    private static final int COMPLETE = 4;
    private final Point witchPoint;
    private final String castleName;
    private int internalStep = FIND_WITCH;
    private AdvancedAppearance witchAppearance = PortraitSubView.makeRandomPortrait(Classes.WIT, Race.ALL, true);

    public WitchStoryPart(Point witchPosition, String castleName) {
        this.witchPoint = witchPosition;
        this.castleName = castleName;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new FindTheWitch());
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (internalStep == DO_QUEST) {
            Point position = model.getWorld().getPositionForHex(model.getCurrentHex());
            if (position.x == witchPoint.x && position.y == witchPoint.y) {
                quests.add(getQuestAndSetPortrait(SpecialDeliveryQuest.QUEST_NAME, witchAppearance, "Witch"));
            }
        }
    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (witchPoint.x == x && witchPoint.y == y && internalStep < COMPLETE) {
            model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
        }
    }

    @Override
    public String getHexInfo(Point position) {
        if (position.x == witchPoint.x && position.y == witchPoint.y && internalStep < COMPLETE) {
            return "the Witch in the Woods";
        }
        return super.getHexInfo(position);
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        Point hexPoint = model.getWorld().getPositionForHex(worldHex);
        if (witchPoint.x == hexPoint.x && witchPoint.y == hexPoint.y) {
            String name = "Find Witch";
            if (internalStep > FIND_WITCH) {
                name = "Visit Witch";
            }
            return List.of(new DailyAction(name, new VisitWitchEvent(model)));
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    protected boolean isCompleted() {
        return internalStep >= COMPLETE;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return new PartThreeStoryPart(model.getMainStory().getCastleName(), model.getMainStory().getLibraryTownName());
    }

    public class FindTheWitch extends MainStoryTask {
        public FindTheWitch() {
            super("The Witch in the Woods");
        }

        @Override
        public String getText() {
            if (internalStep == FIND_WITCH) {
                return "Find Everix's acquaintance, the witch, to ask about the crimson pearl.";
            } else if (internalStep == DO_QUEST) {
                return "Complete the '" + SpecialDeliveryQuest.QUEST_NAME + "' quest.";
            } else if (internalStep == QUEST_DONE) {
                return "Return to the witch to get information about the Crimson Pearl.";
            }
            return "You helped the witch in the wood deliver a special potion to her client.\n\nCompleted";
        }

        @Override
        public boolean isComplete() {
            return WitchStoryPart.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            return model.getMainStory().getWitchPosition();
        }
    }

    private class VisitWitchEvent extends DailyEventState {
        public VisitWitchEvent(Model model) {
            super(model);
        }

        @Override
        protected boolean isFreeRations() {
            return true;
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, witchAppearance, "Witch");
            if (internalStep == FIND_WITCH) {
                println("You find a small hut in a dank grove. Light emanates from " +
                        "the window. Inside a witch is stirring a cauldron and " +
                        "mumbling strange rhymes.");
                portraitSay("Arr-arrahum, Arr-arrahum... pluck the strings, and bang the drum...");
                leaderSay("Excuse " + meOrUs() + "...");
                portraitSay("My hat is old, my teeth are gold... I have a bird I like to hold.");
                leaderSay("EXCUSE " + meOrUs().toUpperCase() + "!");
                portraitSay("Yes, my goodness. Some people are just noisy arent they!");
                notLeaderSay("Is she talking to us?");
                leaderSay("We've been told we could find a wise person here.");
                portraitSay("Who told you that?");
                leaderSay("Everix the Druid sent us.");
                portraitSay("Ah, good old Everix. How is she? Still hanging about in that dung heap of a town?");
                leaderSay("Uhm, yes.");
                leaderSay("Anyway, " + iveOrWeve() + " found this crimson orb. It was in the belly of a frogman and it " +
                        "seems it was having a maddening effect on him.");
                portraitSay("How intriguing.");
                leaderSay("It must be magical. And potentially dangerous or valuable, could you tell " + meOrUs() + " more?");
                portraitSay("Of course I can my dear. It is both dangerous AND valuable...");
                leaderSay("Splendid. Please continue.");
                portraitSay("I will. But before I do. You must do something for me.");
                leaderSay("Yes, Everix mentioned you may have a task for " + meOrUs() + ".");
                portraitSay("Of course. Witches can't survive on charity work. We have to make a living too you know.");
                leaderSay("Very well. What is the task that needs doing?");
                portraitSay("A client of mine has requested a very special potion. It takes months to prepare, is " +
                        "exceedingly tricky to make. The ingredients are either very rare or expensive.");
                leaderSay("Let me guess. You want " + meOrUs() + " to get them for you?");
                portraitSay("Don't worry my dear. I've already brewed the potion. Your task will be delivering it to my client.");
                notLeaderSay("Sounds easy enough.");
                portraitSay("It may sound easy, but I fear a third party may have gotten wind of the deal " +
                        "and may attempt to steal it.");
                leaderSay("Fair enough. When do " + iOrWe() + " start?");
                portraitSay("As soon as you are ready. The client lives in a town nearby. And don't worry about collecting " +
                        "any payment. The potion has already been paid for in advance.");
                leaderSay("May " + iOrWe() + " ask what kind of potion it is?");
                portraitSay("You may ask, but I shall not answer. My clients require that business is carried out discretely. " +
                        "Although in this case, it seems somebody has been blabbing.");
                portraitSay("Do this and I'll tell you the story about your crimson orb. If your wondering if it will be worth " +
                        "it, you can stop. It will be. Now off you go.");
                increaseStep(model);
            } else if (internalStep == DO_QUEST) {
                portraitSay("You better deliver that potion soon. My Client is waiting!");
                leaderSay(iOrWeCap() + " are getting around to it.");

            } else if (internalStep == QUEST_DONE) {
                leaderSay("We've done your little task, at some great personal expense. Now please tell us about this pearl");
                portraitSay("Then sit down, and I'll tell you the story.");
                portraitSay("A long time ago there was an evil king who ruled over all this land. He was hated by all his subjects, " +
                        "except for his vassals which were loyal to him");
                portraitSay("The people were in despair. How to overthrow the despot? One group of powerful sorcerers banded together. " +
                        "In service of the people and the greater good, they vowed to make changes.");
                portraitSay("Persuaded by their show of courage, many other mages shared their most guarded secrets with this group of sorcerers " +
                        "who started to call themselves 'the Quad'. There were four of them, you see. And they all became very powerful.");
                portraitSay("They came up with a plan to overthrow the evil king. They had discovered how to control a mind from afar. All " +
                        "that was needed was for the victim to swallow a pearl.");
                leaderSay("A crimson one?");
                portraitSay("A crimson one. Now the king himself had many tasters, and it would be impossible to get him to ingest anything " +
                        "without noticing an object, such as a pearl, in his food. But his vassals were less careful.");
                portraitSay("The sorcerers manage to take control of several of the vassals, and then, using their new puppets and staged a coup.");
                portraitSay("Afterward, the Quad stepped forward as the liberators of the land. But when it was time for them to give up their power...");
                leaderSay("They didn't. Big shocker there.");
                portraitSay("Well, they didn't without a fight. There was much internal turmoil in the land. But finally the Quad were chased off and they " +
                        "retreated to a faraway land, never to be seen again.");
                leaderSay("So this pearl... could it be a remnant of that time?");
                portraitSay("Perhaps. But I find it unlikely. The history books say all the crimson pearls were accounted for and properly disposed of.");
                leaderSay("Could it be that the Quad has returned then?");
                portraitSay("Also unlikely, they existed many hundred years ago. And I surely hope not, for they were some of the most powerful magic users " +
                        "to ever exist in this world. But anything is possible I guess.");
                portraitSay("It's more likely somebody else learned how to make such pearls. But why it found itself into " +
                        "the stomach of a frogman, I can't say. Either way, this is a threat to the kingdom.");
                leaderSay("What do we do now?");
                UrbanLocation castle = model.getWorld().getCastleByName(castleName);
                portraitSay("You should talk to the " + castle.getLordTitle() + " of " + castle.getPlaceName() +
                                 ". The proper authorities must be warned of this. " +
                                 "The court mage will know more about the issue, I'm sure. And the " + castle.getLordTitle() + " will know what to do.");
                if (getPartyAlignment(model) < 0) {
                    leaderSay("No chance we can just sell the pearl to you? Then you can do whatever you like with it, no questions asked...");
                    portraitSay("Thanks for the offer, but I think this time I'll put the greater good ahead of my own interests.");
                }
                increaseStep(model);
                transitionStep(model);
            } else {
                portraitSay("I'm afraid I have no more information for you, unless you want to learn about witchcraft. Do you?");
                ChangeClassEvent change = new ChangeClassEvent(model, Classes.WIT);
                print("The witch offering to instruct you in the ways of witchcraft, ");
                change.areYouInterested(model);
                portraitSay("Good luck taking on those sorcerers. Farewell!");
            }
        }
    }
}

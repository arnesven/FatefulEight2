package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.map.WorldHex;
import model.quests.Quest;
import model.quests.SpecialDeliveryQuest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import view.sprites.Sprite;
import view.sprites.SpriteQuestMarker;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WitchStoryPart extends StoryPart {

    private static final Sprite MAP_SPRITE = new SpriteQuestMarker();
    private static final int INITIAL_STATE = 0;
    private static final int FIND_WITCH = 1;
    private static final int DO_QUEST = 2;
    private final Point witchPoint;
    private int internalStep = INITIAL_STATE;
    private static AdvancedAppearance witchAppearance = PortraitSubView.makeRandomPortrait(Classes.WIT, Race.ALL, true);

    public WitchStoryPart(Point witchPosition) {
        this.witchPoint = witchPosition;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        if (internalStep > 0) {
            return List.of(new FindTheWitch());
        }
        return new ArrayList<>();
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress(int track) {
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
        if (internalStep > INITIAL_STATE) {
            if (witchPoint.x == x && witchPoint.y == y) {
                model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 2);
            }
        }
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        if (internalStep > INITIAL_STATE) {
            Point hexPoint = model.getWorld().getPositionForHex(worldHex);
            if (witchPoint.x == hexPoint.x && witchPoint.y == hexPoint.y) {
                String name = "Find Witch";
                if (internalStep > FIND_WITCH) {
                    name = "Visit Witch";
                }
                return List.of(new DailyAction(name, new VisitWitchEvent(model)));
            }
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    public StoryPart transition(Model model) {
        return null;
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
            }
            return "Completed";
        }

        @Override
        public boolean isComplete() {
            return false;
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
            //   The witch promises to tell all she knows about the pearl, if the party delivers
            //   a package for her. Seems easy enough, but there are others who are interested in
            //   the contents of the package, ready to intercept the party en route.
            //   Once completed, the Witch explains that the Crimson Pearl is a sorcerer's tool
            //   for dominating somebody's mind. But how did it end up in a frogman's belly?
            //   Pearls like these haven't been made for centuries, and only by a secluded cult of
            //   sorcerer's known as the Cordial Quad. It is the general belief that they were all wiped out centuries ago.
            //   The party should inform the proper authorities about this.
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
                model.getMainStory().increaseStep(model, StoryPart.TRACK_B);
            } else if (internalStep == DO_QUEST) {
                portraitSay("You better deliver that potion soon. My Client is waiting!");
                leaderSay(iOrWeCap() + " are getting around to it.");
            } else {
                portraitSay("Now sit down, and I'll tell you the story about the crimson pearls.");
            }
        }
    }
}

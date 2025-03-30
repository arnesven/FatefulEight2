package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.mainstory.*;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.EscapeTheDungeonQuest;
import model.quests.Quest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import model.tasks.DestinationTask;
import util.MyLists;
import util.MyStrings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PartSixStoryPart extends StoryPart {
    private final String castle;
    private final GainSupportOfRemotePeopleTask gainSupportOfRemotePeopleTask;
    private final List<GainSupportOfNeighborKingdomTask> gainSupportOfNeighborKingdomTasks;
    private int internalStep = 0;

    public PartSixStoryPart(Model model, String castleName) {
        this.castle = castleName;
        this.gainSupportOfRemotePeopleTask = model.getMainStory().makeRemoteKingdomSupportTask(model);
        this.gainSupportOfNeighborKingdomTasks = model.getMainStory().makeNeighborKingdomTasks(model);
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        if (internalStep <= 2) {
            return List.of(new EscapeTheDungeonJournalEntry(castle));
        }
        if (allSupportTasksDone()) {
            return List.of(new LeadTheAssaultJournalEntry(castle));
        }
        List<JournalEntry> entries = MyLists.transform(gainSupportOfNeighborKingdomTasks,
                task -> task.getJournalEntry(null));
        entries.add(gainSupportOfRemotePeopleTask.getJournalEntry(null));
        return entries;
    }

    private boolean allSupportTasksDone() {
        return MyLists.all(getAllSupportTasks(), DestinationTask::isCompleted);
    }

    private List<DestinationTask> getAllSupportTasks() {
        List<DestinationTask> tasks = new ArrayList<>(gainSupportOfNeighborKingdomTasks);
        tasks.add(gainSupportOfRemotePeopleTask);
        return tasks;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) { }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {

    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        Point witchPoint = model.getMainStory().getWitchPosition();
        if (witchPoint.x == x && witchPoint.y == y && internalStep == 2) {
            model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
        }
        if (internalStep > 2) {
            for (DestinationTask dt : getAllSupportTasks()) {
                if (!dt.isCompleted()) {
                    Point p = dt.getPosition();
                    if (p.x == x && p.y == y) {
                        model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
                    }
                }
            }
        }
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        if (internalStep == 1) {
            CastleLocation castleLoc = model.getWorld().getCastleByName(castle);
            return List.of(new DailyAction("Talk to Everix", new TalkToEverixEvent(model, castleLoc)));
        }
        Point witchPoint = model.getMainStory().getWitchPosition();
        Point hexPoint = model.getWorld().getPositionForHex(worldHex);
        if (internalStep == 2 && witchPoint.x == hexPoint.x && witchPoint.y == hexPoint.y) {
            return List.of(new DailyAction("Visit Witch", new VisitWitchEvent(model)));
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    public VisitLordEvent getVisitLordEvent(Model model, UrbanLocation location) {
        if (model.getMainStory().isFugitive(model)) {
            return new GetOutOfCastleEvent(model);
        }
        if (internalStep == 3) {
            for (GainSupportOfNeighborKingdomTask task : gainSupportOfNeighborKingdomTasks) {
                if (task.isAtLocation(model, location)) {
                    return new GetSupportFromLordEvent(model, location, task);
                }
            }
        }
        return super.getVisitLordEvent(model, location);
    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    private class EscapeTheDungeonJournalEntry extends MainStoryTask {
        public EscapeTheDungeonJournalEntry(String castle) {
            super("Escape " + castle);
        }

        @Override
        public String getText() {
            if (internalStep == 0) {
                return "Complete the '" + EscapeTheDungeonQuest.QUEST_NAME + "' quest.";
            } else if (internalStep == 1) {
                return "Talk to Everix.";
            }
            return "Seek refuge at the witch of the woods.";
        }

        @Override
        public boolean isComplete() {
            return PartSixStoryPart.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            if (internalStep == 0) {
                return model.getMainStory().getCastlePosition(model);
            }
            if (internalStep == 1) {
                return model.getParty().getPosition();
            }
            return model.getMainStory().getWitchPosition();
        }
    }

    private class TalkToEverixEvent extends DailyEventState {
        private final CharacterAppearance everixPortrait;
        private final CastleLocation castle;
        private final GameCharacter whosUncle;

        public TalkToEverixEvent(Model model, CastleLocation castleLoc) {
            super(model);
            InitialStoryPart init = ((InitialStoryPart)model.getMainStory().getStoryParts().get(0));
            this.everixPortrait = init.getEverixPortrait();
            this.whosUncle = init.getWhosUncle();
            this.castle = castleLoc;
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, everixPortrait, "Everix");
            forcePortraitEyes(true);
            leaderSay("Everix, can you hear me?");
            model.getLog().waitForAnimationToFinish();
            forcePortraitEyes(false);
            portraitSay("Uhh... what? Where am I?");
            leaderSay("It's all right Everix, you're safe.");
            portraitSay("Oh, it's you...");
            println("Slowly, Everix gets up.");
            leaderSay("Didn't expect to see us again did you?");
            portraitSay("No. But I'm glad to see you. The last thing I remember is " + castle.getLordName() +
                    "'s thugs beating me up, and interrogating me.");
            leaderSay("Interrogating you, about what?");
            portraitSay("They were asking about you and that damn crimson pearl.");
            leaderSay("About us? And the pearl? Wait a minute, Everix, let's back up. " +
                    "Why were you in " + castle.getLordName() + "'s dungeons anyway?");
            String companyName = "'" + model.getParty().getLeader().getFullName() + "'s Company'";
            portraitSay("It was about a week ago I think. " + castle.getLordName() +
                    "'s troops came to our town. They were asking if anybody had seen or " +
                    "heard about a group called " + companyName + ".");

            String uncle = whosUncle.getFirstName() + "'s uncle";
            portraitSay("I kept out of sight, but " + uncle + " must " +
                    "have told them about you, and I think he must have mentioned the crimson pearl you found " +
                    "because once they'd found me they kept asking me about it.");
            if (model.getParty().getPartyMembers().contains(whosUncle)) {
                partyMemberSay(whosUncle, "My uncle... what happened then?");
                uncle = "your uncle";
            } else {
                leaderSay("Then what happened?");
            }
            portraitSay("They rounded some of us up, " + uncle +
                    " included, and hauled us off to " + castle.getPlaceName() + ", and tossed us in the dungeon.");
            leaderSay("What did you tell them.");
            portraitSay("Everything I knew, which wasn't much. " + MyStrings.capitalize(uncle) +
                    " however, when he realized " + castle.getLordTitle() + "'s intentions were less than honorable, he " +
                    "wasn't to keen on cooperating.");
            if (model.getParty().getPartyMembers().contains(whosUncle)) {
                partyMemberSay(whosUncle, "That old fool.");
                portraitSay("I'm sorry to say, I don't think he survived their beatings.");
                partyMemberSay(whosUncle, "Uncle... No!");
                leaderSay(whosUncle.getFirstName() + ", I'm so sorry...");
            }
            leaderSay("Did you tell them about your friend the Witch?");
            portraitSay("No, I never got around to it. What did she tell you about the crimson pearl? " +
                    "What's all this about really?");
            String direction = model.getMainStory().getExpandDirectionName().toLowerCase();
            println("You spend some time filling Everix in on what you've found out about " +
                    "the crimson pearl and the quad. You tell about your adventures at the Ancient Stronghold," +
                    " and the latest events at " + castle.getName() + ", including your encounter with Damal the advisor " +
                    "and Damal's story about the mysterious envoy from the " + direction + ".");
            portraitSay("That's quite the tale. And now you can add 'escaped from prison' to the end of it. " +
                    castle.getLordName() + "'s men will be looking high and lo for you now. Your probably the most wanted " +
                    "individuals in the kingdom.");
            leaderSay("Somehow we have to set this right. Any advice on where to go from here?");
            portraitSay("Let's stay clear of urban areas, unless you want to go back to jail. " +
                    "We could go see my witch friend. Maybe she has some clue as to what's going on.");
            leaderSay("Alright. We're off.");
            progress();
        }
    }

    private class VisitWitchEvent extends DailyEventState {
        private final CharacterAppearance witchAppearance;

        public VisitWitchEvent(Model model) {
            super(model);
            WitchStoryPart witchStoryPart = (WitchStoryPart) MyLists.find(model.getMainStory().getStoryParts(),
                    (StoryPart sp) -> sp instanceof WitchStoryPart);
            this.witchAppearance = witchStoryPart.getWitchAppearance();
        }

        @Override
        protected boolean isFreeRations() {
            return true;
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, witchAppearance, "Witch");
            String expandDir = model.getMainStory().getExpandDirectionName().toLowerCase();
            portraitSay("Oh... you lot. You must gain the support of the kingdoms surrounding " + castle + ", as well as the " +
                    model.getMainStory().getRemotePeopleName() + " of the " + expandDir + ".");
            progress();
        }
    }

    private static class LeadTheAssaultJournalEntry extends MainStoryTask {
        public LeadTheAssaultJournalEntry(String castle) {
            super("Assault on " + castle);
        }

        @Override
        public String getText() {
            return "ASdf";
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public Point getPosition(Model model) {
            return model.getMainStory().getCastlePosition(model);
        }
    }
}

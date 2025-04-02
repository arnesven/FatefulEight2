package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
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
        if (internalStep == 3) {
            for (DestinationTask dt : getAllSupportTasks()) {
                if (model.partyIsInOverworldPosition(dt.getPosition()) && dt.givesDailyAction(model)) {
                    return List.of(dt.getDailyAction(model));
                }
            }
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    public VisitLordEvent getVisitLordEvent(Model model, UrbanLocation location) {
        if (model.getMainStory().isPersonaNonGrata(model)) {
            return new GetOutOfCastleEvent(model);
        }
        if (internalStep == 3) {
            for (GainSupportOfNeighborKingdomTask task : gainSupportOfNeighborKingdomTasks) {
                if (task.isAtLocation(model, location)) {
                    return task.makeLordEvent(model, location);
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
        private final CharacterAppearance everixAppearance;

        public VisitWitchEvent(Model model) {
            super(model);
            WitchStoryPart witchStoryPart = (WitchStoryPart) MyLists.find(model.getMainStory().getStoryParts(),
                    (StoryPart sp) -> sp instanceof WitchStoryPart);
            this.witchAppearance = witchStoryPart.getWitchAppearance();
            InitialStoryPart init = ((InitialStoryPart)model.getMainStory().getStoryParts().get(0));
            this.everixAppearance = init.getEverixPortrait();
        }

        @Override
        protected boolean isFreeRations() {
            return true;
        }

        @Override
        protected void doEvent(Model model) {
            showWitch(model);

            portraitSay("Oh... you lot. I was wondering when you...");
            showEverix(model);
            portraitSay("It's been a long time Edwina.");
            showWitch(model);
            portraitSay("Everix, what happened to you?");
            showEverix(model);
            CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
            portraitSay(castle.getLordName() + "'s thugs got me. It's alright, just some light bruising.");
            showWitch(model);
            portraitSay(castle.getLordTitle() + " " + castle.getLordName() + " must have really lost his mind, " +
                    "I keep hearing word about people trying to leave " + CastleLocation.placeNameShort(castle.getPlaceName()) + ".");
            leaderSay("Why do people want to leave?");
            portraitSay("Because " + castle.getLordName() + "'s troops are everywhere, and they are harassing everybody. Lately it feels " +
                            "like the " + CastleLocation.placeNameToKingdom(castle.getPlaceName()) + " is turning in to a big prison. " +
                    "Why "  + heOrShe(castle.getLordGender()) + "'s trying to make all of " + hisOrHer(castle.getLordGender()) + " miserable?");
            leaderSay(iOrWeCap() + " think he may be under the influence of one of those crimson pearls.");
            portraitSay("What makes you think that?");
            leaderSay("We met one of " + castle.getLordName() + "'s former advisors in the dungeons below " + castle.getPlaceName() + ". " +
                    "Just a few weeks ago a strange envoy had arrived at the castle bringing gifts. She was acting suspiciously... it was around that time " +
                    castle.getLordTitle() + " " + castle.getLordName() + " went nuts.");
            showEverix(model);
            portraitSay("I'm sure this envoy is working for whoever is behind all of this.");
            showWitch(model);
            portraitSay("It certainly seems that way. What did they look like?");
            leaderSay("Let me see... I think Damal mentioned she was tall, dark and fair.");
            portraitSay("That's not much to work with I'm afraid.");
            leaderSay("We have to get back to the castle and try to find her, and interrogate her!");
            portraitSay("I don't think you'll be able to get anywhere near " + castle.getPlaceName() + " at the moment.");
            showEverix(model);
            portraitSay("Not with " + castle.getLordName() + " patrolling every inch of the kingdom in search of you. " +
                    "You'll be lucky if you can get into any town in " + CastleLocation.placeNameShort(castle.getPlaceName()) + ".");
            boolean said = randomSayIfPersonality(PersonalityTrait.aggressive, List.of(model.getParty().getLeader()), "Let them come! I'll send them all to hell!");
            if (said) {
                leaderSay("Calm down. We can't get carried away.");
            } else {
                randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()), "Can't we just go somewhere far far away where " +
                        castle.getLordName() + " won't ever find us?");
            }
            leaderSay("We need a plan.");
            showWitch(model);
            portraitSay("Indeed, and allies.");
            leaderSay("Hmm... we're a little thin in that section I'm afraid.");
            portraitSay("Not necessarily. Right now " + CastleLocation.placeNameShort(castle.getPlaceName()) + "'s neighboring kingdoms " +
                    "are being subjected to " + castle.getLordName() + "'s spies and agents, as well as scores of fugitives. " +
                    "And I hear the orcish raids have spread to other kingdoms while mysteriously stopped in " + CastleLocation.placeNameShort(castle.getPlaceName()) + ". " +
                    "I don't think those rulers are so happy with " + castle.getLordTitle() + " " + castle.getLordName() + " right now.");
            said = randomSayIfPersonality(PersonalityTrait.intellectual, List.of(), "We could seize the opportunity and win them to our cause.");
            if (!said) {
                leaderSay("How does that help us?");
                portraitSay("We could seize this opportunity to win them to our cause.");
            }
            portraitSay("Have them lend us some support, maybe even some armed forces. With them on our side we should be " +
                    "able to stage a big enough diversion that a small assault on " + castle.getPlaceName() + " could be successful. Then you could finally confront " +
                    castle.getLordTitle() + " " + castle.getLordName() + " and track down our mystery woman.");
            leaderSay("Perhaps... It seems risky.");
            showEverix(model);
            portraitSay("You could even the odds a bit. You could try to gain the support of the " + model.getMainStory().getRemotePeopleName() + ".");
            leaderSay("The " + model.getMainStory().getRemotePeopleName() + "?");
            showWitch(model);
            portraitSay("Yes, they reside in the lands to the " + model.getMainStory().getExpandDirectionName().toLowerCase() +
                    ". With them on your side as well you are sure to prevail!");
            leaderSay("Well, we've got to get out of " + CastleLocation.placeNameShort(castle.getPlaceName()) +
                    " anyway. We might as well take a little tour and try to make some new friends.");
            randomSayIfPersonality(PersonalityTrait.brave, List.of(model.getParty().getLeader()), "Sounds like a great adventure! What are we waiting for?");
            showEverix(model);
            portraitSay("I don't think I'm up for the trip. I need to recuperate here for a while.");
            showWitch(model);
            portraitSay("Stay here as long as you wish Everix. You're welcome to stay too, but I would get going sooner rather than later. " +
                    "This kingdom's suffering is increasing day by day.");
            leaderSay(iveOrWeve() + " got a big journey in front of " + meOrUs() + ".");
            portraitSay("Be safe in your travels. Keep a low profile, at least while in " + CastleLocation.placeNameShort(castle.getPlaceName()) + ".");
            showEverix(model);
            portraitSay("Thank you for rescuing me. I owe you one.");
            leaderSay("Goodbye.");
            progress();
        }

        private void showEverix(Model model) {
            showExplicitPortrait(model, everixAppearance, "Everix");
        }

        private void showWitch(Model model) {
            showExplicitPortrait(model, witchAppearance, "Witch");
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

package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.mainstory.*;
import model.map.*;
import model.quests.*;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import model.states.dailyaction.TownDailyActionState;
import model.tasks.DestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyStrings;
import util.MyTriplet;
import view.LogView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartSixStoryPart extends StoryPart {
    private final String castle;
    private final GainSupportOfRemotePeopleTask gainSupportOfRemotePeopleTask;
    private final List<GainSupportOfNeighborKingdomTask> gainSupportOfNeighborKingdomTasks;
    private  Point assaultPoint;
    private int internalStep = 0;
    private boolean completed = false;

    public PartSixStoryPart(Model model, String castleName) {
        this.castle = castleName;
        this.gainSupportOfRemotePeopleTask = model.getMainStory().makeRemotePeopleSupportTask(model);
        this.gainSupportOfNeighborKingdomTasks = model.getMainStory().makeNeighborKingdomTasks(model);
        setAssaultPoint(model);
    }

    private void setAssaultPoint(Model model) {
        Map<Integer, Integer> expandDirMap = Map.of(
                WorldBuilder.EXPAND_EAST, Direction.SOUTH,
                WorldBuilder.EXPAND_NORTH, Direction.SOUTH_EAST,
                WorldBuilder.EXPAND_WEST, Direction.NORTH_EAST,
                WorldBuilder.EXPAND_SOUTH, Direction.NORTH);
        Point castlePoint = new Point(model.getMainStory().getCastlePosition(model));
        int expandDir = model.getMainStory().getExpandDirection();
        Point dxdy = Direction.getDxDyForDirection(castlePoint, expandDirMap.get(expandDir));
        model.getWorld().move(castlePoint, dxdy.x, dxdy.y);
        assaultPoint = castlePoint;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        if (internalStep <= 2) {
            return List.of(new EscapeTheDungeonJournalEntry(castle));
        }
        List<JournalEntry> entries = MyLists.transform(gainSupportOfNeighborKingdomTasks,
                task -> task.getJournalEntry(null));
        entries.add(gainSupportOfRemotePeopleTask.getJournalEntry(null));
        if (allSupportTasksDone()) {
            entries.add(new LeadTheAssaultJournalEntry(castle));
        }
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
    public void addFactionStrings(List<MyPair<String, String>> result) {
        gainSupportOfRemotePeopleTask.addFactionString(result);
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) { }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return new PartSevenStoryPart(model, castle);
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        MyTriplet<String, CharacterAppearance, String> triplet = gainSupportOfRemotePeopleTask.addQuests(model);
        if (triplet != null) {
            quests.add(getQuestAndSetPortrait(triplet.first, triplet.second, triplet.third));
        }
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
        if (internalStep >= 3) {
            for (DestinationTask dt : getAllSupportTasks()) {
                if (model.partyIsInOverworldPosition(dt.getPosition()) && dt.givesDailyAction(model)) {
                    return List.of(dt.getDailyAction(model));
                }
            }
        }
        if (allSupportTasksDone() && model.partyIsInOverworldPosition(assaultPoint)) {
            return List.of(new DailyAction("Go to Rendezvous", new GoToRendezvousEvent(model, castle)));
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    public VisitLordEvent getVisitLordEvent(Model model, UrbanLocation location) {
        if (model.getMainStory().isPersonaNonGrata(model)) {
            return new GetOutOfCastleEvent(model);
        }
        if (internalStep >= 3) {
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
        return completed;
    }

    public boolean witchTalkedTo() {
        return internalStep >= 2;
    }

    public GainSupportOfRemotePeopleTask getRemotePeopleTask() {
        return gainSupportOfRemotePeopleTask;
    }

    public void setSupportTasksCompleted() {
        gainSupportOfRemotePeopleTask.setCompleted();
        for (GainSupportOfNeighborKingdomTask task : gainSupportOfNeighborKingdomTasks) {
            task.setCompleted(true);
        }
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
            portraitSay("I have some provisions stowed away for situations such as these. Here, you can have them.");
            println("You gained 100 rations.");
            model.getParty().addToFood(100);
            leaderSay("Thank you very much. This will help a lot.");
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

    private class LeadTheAssaultJournalEntry extends MainStoryTask {
        private final String castle;

        public LeadTheAssaultJournalEntry(String castle) {
            super("Assault on " + castle);
            this.castle = castle;
        }

        @Override
        public String getText() {
            if (completed) {
                return "You gained the support of the neighboring kingdoms of " + castle + ". While the castle is stormed " +
                        "by troops, you sneak inside the castle to unravel the mystery of the Crimson pearl, the Quad and the " +
                        "strange envoy who appears to have driven the regent mad.\n\nCompleted";
            }
            return "The time has come to get back into " + castle + " and get to the bottom of the mystery of " +
                    "the Crimson Pearl, the Quad and the strange envoy who appears to have driven the regent mad.\n\n" +
                    "Meet with the forces of the neighboring kingdoms at the rendezvous point and lead the assault.";
        }

        @Override
        public boolean isComplete() {
            return completed;
        }

        @Override
        public Point getPosition(Model model) {
            return assaultPoint;
        }
    }

    private class GoToRendezvousEvent extends DailyEventState {
        private final String castle;
        private final CastleLocation enemyKingdom;
        private final CastleLocation kingdom1;
        private final CastleLocation kingdom2;
        private boolean questStarted = false;

        public GoToRendezvousEvent(Model model, String castle) {
            super(model);
            this.castle = castle;
            enemyKingdom = model.getWorld().getCastleByName(castle);
            kingdom1 = gainSupportOfNeighborKingdomTasks.getFirst().getKingdom(model);
            kingdom2 = gainSupportOfNeighborKingdomTasks.getLast().getKingdom(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("At the rendezvous point, many tents and pavilions have been erected. There are soldiers everywhere and you " +
                    "can spot banners from both " + CastleLocation.placeNameToKingdom(kingdom1.getPlaceName()) +
                    " and " + CastleLocation.placeNameToKingdom(kingdom2.getPlaceName()) + ".");
            leaderSay("The armies of " + CastleLocation.placeNameToKingdom(castle) +
                    " must really be in disarray if all of these soldiers where able " +
                    "to set up camp right on " + enemyKingdom.getLordName() + "'s doorstep.");
            println("A larger more grand tent is located atop a small hill. There are guards posted outside, you figure " +
                    "whoever is commanding these soldiers are in there. You step into the tent and are surprised " +
                    "to see some familiar faces.");
            model.getLog().waitForAnimationToFinish();
            showLord1(model);
            portraitSay("Speak of the devil! We were just talking about you.");
            showLord2(model);
            portraitSay("I'm glad you made it. We were beginning to think you wouldn't come.");
            println(kingdom1.getLordName() + " and " + kingdom2.getLordName() + " are standing by a war table surrounded " +
                    "by there highest ranking officials.");
            leaderSay("My lords. What are you doing here?");
            showLord1(model);
            portraitSay("We wanted to see this through for ourselves. And honestly, when our scouts reported very little " +
                    "in the way of resisting forces in our paths to get here, we just couldn't hold back.");
            leaderSay("What's happened. Where are " + enemyKingdom.getLordName() + "'s armies?");
            showLord2(model);
            portraitSay("It appears, that, when we both attacked " + CastleLocation.placeNameShort(enemyKingdom.getPlaceName()) +
                    " simultaneously, their forces were spread too thin.");
            showLord1(model);
            portraitSay("My spies have also learned that they have quite the problem with morale, people are defecting.");
            if (gainSupportOfRemotePeopleTask.supportsFromTheSea()) {
                showLord2(model);
                portraitSay("And, we've had reports that " + model.getMainStory().getRemotePeopleName() +
                        " have been raiding the shores of " + CastleLocation.placeNameShort(enemyKingdom.getPlaceName()) + ".");
            } else {
                showExplicitPortrait(model, gainSupportOfRemotePeopleTask.getLeaderPortrait(), gainSupportOfRemotePeopleTask.getLeaderName());
                portraitSay("Those cowards are fleeing before my warriors before we can even get close.");
                leaderSay(gainSupportOfRemotePeopleTask.getLeaderName() + "! What are you doing here?");
                portraitSay("I promised you my support didn't I? I am a man of my word.");
                leaderSay("Undeniably! It is great to have you here.");
            }
            showLord2(model);
            portraitSay("Well, we're all here, why don't we look over these battle plans? Are you leading the assault " +
                    model.getParty().getLeader().getFirstName() + "?");
            leaderSay("Uhm...");
            InitialStoryPart init = ((InitialStoryPart)model.getMainStory().getStoryParts().get(0));
            CharacterAppearance everixPortrait = init.getEverixPortrait();
            showExplicitPortrait(model, everixPortrait, "Everix");
            portraitSay("I would advise against it.");
            println("You turn around and see Everix and the Witch of the Woods in the entrance to the tent.");
            leaderSay("Everix. What are you doing here?");
            portraitSay("I have a personal vendetta with " + enemyKingdom.getLordName() +
                    ". I won't soon forget what " + heOrShe(enemyKingdom.getLordGender()) + " did to my village.");
            leaderSay("Of course. You have as much right as anybody to see " + enemyKingdom.getLordName() + " brought to justice. " +
                    "But, we aren't assaulting the castle?");
            portraitSay("Oh by all means! But if we want to take the mysterious envoy by surprise I would suggest a stealthier approach. " +
                    "We want answers, remember?");
            leaderSay("We got out from the dungeons through the sewers. I'm sure we can find our way back in again.");
            println("Everix nods, then she faces " + kingdom1.getLordName() + " and " + kingdom2.getLordName() + ".");
            portraitSay("Give us a few hours, then have your soldiers storm the castle. " +
                    "That should make it a little easier sneaking around in there.");
            showLord1(model);
            portraitSay("That sounds like a good plan. You find " + enemyKingdom.getLordName() +
                    ", and that envoy fellow, and we'll take care of the rest.");
            model.getLog().addAnimated(LogView.GOLD_COLOR +
                    "WARNING! Once you proceed from here, you will not be able to return for some time. \n" + LogView.DEFAULT_COLOR);
            print("Are you ready to go now? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Yes " + imOrWere() + " ready. Let's go.");
                portraitSay("Alright. Good luck!");
                transitionStep(model);
                questStarted = true;
                completed = true;
            } else {
                leaderSay("Not quite yet, there are still some things that need to be prepared.");
                portraitSay("Okay. Return here when you are ready. But don't take too long. " +
                        "It's hard to say how long we can hold this position.");
                leaderSay("I'll be quick, I promise.");
            }
        }

        private void showLord1(Model model) {
            removePortraitSubView(model);
            showExplicitPortrait(model, model.getLordPortrait(kingdom1), kingdom1.getLordName());
        }

        private void showLord2(Model model) {
            removePortraitSubView(model);
            showExplicitPortrait(model, model.getLordPortrait(kingdom2), kingdom2.getLordName());
        }

        @Override
        protected GameState getEveningState(Model model) {
            if (questStarted) {
                MainQuest q = MainStory.getQuest(MindMachineQuest.QUEST_NAME);
                return new QuestState(model, q, model.getParty().getPosition());
            }
            return super.getEveningState(model);
        }
    }
}

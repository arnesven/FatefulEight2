package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.RandomAppearance;
import model.classes.Classes;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.quests.OrcWarCampQuest;
import model.quests.Quest;
import model.quests.SpecialDeliveryQuest;
import model.races.AllRaces;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import view.SimpleMessageView;
import view.party.CharacterCreationView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PartFourStoryPart extends StoryPart {
    private static final int INITIAL_STEP = 0;
    private static final int TRAVEL_STEP = 1;
    private static final int RETURN_STEP = 2;
    private static final int COMPLETE = 3;
    private final String castleName;
    private int step;
    private final Point campPoint;
    private AdvancedAppearance general = PortraitSubView.makeRandomPortrait(Classes.PAL, AllRaces.ALL);
    private AdvancedAppearance captain = PortraitSubView.makeRandomPortrait(Classes.CAP, AllRaces.ALL);
    private AdvancedAppearance marshall = PortraitSubView.makeRandomPortrait(Classes.PAL, AllRaces.ALL);


    public PartFourStoryPart(String castleName, Point campPoint) {
        this.step = INITIAL_STEP;
        this.castleName = castleName;
        this.campPoint = campPoint;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        entries.add(new InformLordEntry());
        return entries;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        if (location instanceof CastleLocation &&
                ((CastleLocation) location).getName().equals(castleName)) {
            return new PartFourEvent(model, model.getWorld().getCastleByName(castleName));
        }
        return super.getVisitLordEvent(model, location);
    }

    @Override
    public void progress() {
        step++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (step == TRAVEL_STEP) {
            Point position = model.getWorld().getPositionForHex(model.getCurrentHex());
            if (position.x == campPoint.x && position.y == campPoint.y) {
                CastleLocation loc = model.getWorld().getCastleByName(castleName);
                quests.add(getQuestAndSetPortrait(OrcWarCampQuest.QUEST_NAME, model.getLordPortrait(loc), loc.getLordName()));
            }
        }
    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (campPoint.x == x && campPoint.y == y && INITIAL_STEP < step && step < COMPLETE) {
            model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
        }
    }

    @Override
    public StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    private class InformLordEntry extends MainStoryTask {
        public InformLordEntry() {
            super("Return to " + castleName);
        }

        @Override
        public String getText() {
            if (step == INITIAL_STEP) {
                return "Inform the lord of " + castleName + " about what you have learned about the Quad.";
            } else if (step == TRAVEL_STEP) {
                return "Travel to the location of the war camp and investigate it.";
            } else if (step == RETURN_STEP) {
                return "Return to the lord of " + castleName + " to the deliver the intel you've gathered " +
                        "and receive your reward.";
            }
            return "Completed";
        }

        @Override
        public boolean isComplete() {
            return PartFourStoryPart.this.isCompleted();
        }
    }

    private class PartFourEvent extends DailyEventState {
        private final CastleLocation castle;

        public PartFourEvent(Model model, CastleLocation castleByName) {
            super(model);
            this.castle = castleByName;
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            if (step == INITIAL_STEP) {
                println("You are ushered into a room of the castle that you haven't seen before. " +
                        "The " + castle.getLordTitle() + " and a group of military types are standing around " +
                        "a table where a map is spread out. They all seem very serious and engrossed in a solemn " +
                        "conversation. You patiently stand to the side.");
                model.getLog().waitForAnimationToFinish();
                showExplicitPortrait(model, general, "General");
                portraitSay("... I'm sure they'll attack from here.");
                showExplicitPortrait(model, captain, "Captain");
                portraitSay("I'm sorry general, but I think you are mistaken. Our scouts report movement " +
                        "from this direction.");
                showExplicitPortrait(model, marshall, "Marshall");
                portraitSay("You're both wrong. This field camp is just a diversion, the real threat is somewhere else.");
                showLord(model);
                portraitSay("Hmm... This is very troublesome...");
                println("The " + castle.getLordTitle() + " notices you.");
                portraitSay("Ah, my friends. You have returned. Was Willis able to help you learn more about the Quad?");
                leaderSay("Yes. In fact, we believe the spirits of the Quad have come back to take revenge on the denizens of this land. " +
                        "We found maps indicating the location of their ancient stronghold far to the " +
                        model.getMainStory().getExpandDirectionName() + ".");
                model.getLog().waitForAnimationToFinish();
                showExplicitPortrait(model, general, "General");
                portraitSay("My lord, could it be connected to...");
                showLord(model);
                portraitSay("It's certainly seems so.");
                leaderSay("Excuse me, but what is going on?");
                portraitSay("Well, our scouts have come back with reports of a large Orc army gathering to the " +
                        model.getMainStory().getExpandDirectionName() + ".");
                showExplicitPortrait(model, captain, "Captain");
                portraitSay("And there have been raids in the border regions.");
                leaderSay("Should we be worried? Don't you have an army or something?");
                showLord(model);
                portraitSay("Yes, but we're getting conflicting reports of where the main camp is located and my advisers here " +
                        "are not in agreement on where we should deploy.");
                showExplicitPortrait(model, general, "General");
                portraitSay("We should deploy our army here, where our scouts spotted the large war camp.");
                showExplicitPortrait(model, captain, "Captain");
                portraitSay("We must protect our settlements, we should deploy our forces to the border villages!");
                showExplicitPortrait(model, marshall, "Marshall");
                portraitSay("We must protect our keep. We should keep most of our forces here. But we still need more intel.");
                println("The " + castle.getLordTitle() + " turns to you again.");
                showLord(model);
                portraitSay("Friends, I must ask you for another favor. Can you go to the location of the war camp " +
                        "and investigate it?");
                leaderSay("What about the Quad's stronghold?");
                portraitSay("Yes, that is most likely the source of this evil, but for now we must have more information " +
                        "so we can make a good tactical decision.");
                leaderSay("If you say so. Where's the camp?");
                portraitSay("I'll mark it on your map. Remember, no need to assault it. Just find out if this is the main " +
                        "bulk of the enemy force and what the invaders' orders are.");
                leaderSay("Leave it to us.");
                portraitSay("Thank you.");
                model.transitionToDialog(new SimpleMessageView(model.getView(),
                        "Warning. It is recommended that your party members " +
                                "are at least level 4 before taking on the orc camp."));
                increaseStep(model);
            } else if (step == TRAVEL_STEP) {
                showLord(model);
                portraitSay("Have you investigated the war camp yet?");
                leaderSay("Not yet. But we will.");
                portraitSay("Please hurry, we need to deploy our forces to deal with these invaders.");
            } else if (step == RETURN_STEP) {
                showLord(model);
                portraitSay("You've returned. I was getting worried. We've had many more raids since you " +
                        "were last here. Please tell me you have some solid intelligence for us to act on.");
                leaderSay("Indeed we do.");
            }
        }

        private void showLord(Model model) {
            showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
        }
    }
}

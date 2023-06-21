package model.journal;

import model.MainStory;
import model.Model;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.quests.Quest;
import model.quests.RescueMissionQuest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;

import java.util.List;

public class RescueMissionStoryPart extends StoryPart {
    private final String castleName;
    private int internalStep = 0;

    public RescueMissionStoryPart(String castleName) {
        this.castleName = castleName;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new GoToCastleTask(castleName));
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
        if (internalStep == 1) {
            if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof CastleLocation) {
                if (model.getCurrentHex().getLocation().getName().equals(castleName)) {
                    CastleLocation castle = model.getWorld().getCastleByName(castleName);
                    RescueMissionQuest rescue = ((RescueMissionQuest) MainStory.getQuest(RescueMissionQuest.QUEST_NAME));
                    rescue.setPortrait(model.getLordPortrait(castle));
                    rescue.setProvider(castle.getLordName());
                    quests.add(rescue);
                }
            }
        }
    }

    @Override
    public StoryPart transition(Model model) {
        return null;
    }

    @Override
    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        if (location instanceof CastleLocation &&
                ((CastleLocation) location).getName().equals(castleName)) {
            return new RescueMissionLordEvent(model, model.getWorld().getCastleByName(castleName));
        }
        return super.getVisitLordEvent(model, location);
    }

    private class RescueMissionLordEvent extends DailyEventState {
        private final CastleLocation castle;

        public RescueMissionLordEvent(Model model, CastleLocation castle) {
            super(model);
            this.castle = castle;
        }

        @Override
        protected void doEvent(Model model) {
            if (internalStep == 0) {
                setCurrentTerrainSubview(model);
                showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
                portraitSay("Welcome traveller, what can I do for you.");
                TownLocation town = model.getMainStory().getStartingLocation(model);
                leaderSay("We've come from the town of " + town.getName() + ", " +
                        "We helped them with some trouble with their local population of frogmen.");
                portraitSay("Ah yes, that issue...");
                leaderSay("Anyway, it's been taken care of. We were promised a reward, but it seems they were out of coin...");
                portraitSay("Very unfortunate, but I'm afraid we can't...");
                leaderSay("Sorry to interrupt you, but you owe the " + town.getLordTitle() + " of " + town.getTownName() +
                        " some money. Am I right?");
                leaderSay("That's correct.");
                leaderSay("I've been authorized to collect a debt of " + InitialStoryPart.REWARD_GOLD +
                        " gold, as payment for the resolving the frogmen problem.");
                portraitSay("Hmm, that seems plausible. Let me just get my purse.");
                println("The party receives " + InitialStoryPart.REWARD_GOLD + " gold!");
                model.getParty().addToGold(InitialStoryPart.REWARD_GOLD);
                leaderSay("Thank you. Now we'll just be on our way...");
                portraitSay("Just a moment please.");
                leaderSay("Yes?");
                portraitSay("For a while now I've been looking for a capable team of adventurers like yourselves. " +
                        "I have a task that needs doing.");
                leaderSay("I'm listening.");
                portraitSay("Well. It's my most trusted advisor and top agent, Caid, he's been kidnapped. " +
                        "I need somebody to rescue him");
                leaderSay("Kidnapped? By whom?");
                portraitSay("I'm not sure. They haven't made themselves known.");
                leaderSay("Then how do you know he's been kidnapped?");
                portraitSay("He hasn't left my side for years, now he's been missing for a week. " +
                        "Last time I saw him he was going to the dodgy end of town to investigate some shady dealings.");
                leaderSay("Maybe he's just taken a vacation?");
                if (model.getParty().size() > 1) {
                    GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    partyMemberSay(gc, "Maybe he just got knocked off?");
                }
                portraitSay("Not likely, he's also my master-at-arms and my personal fencing instructor. " +
                        "He can handle himself. Will you look for him? I'll pay you. I'll pay you handsomely.");
                leaderSay("Perhaps we'll look into it.");
                model.getMainStory().increaseStep(model, StoryPart.TRACK_A);
            } else {
                setCurrentTerrainSubview(model);
                showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
                portraitSay("Have you found Caid yet?");
                leaderSay("Not yet.");
            }
        }
    }

    public class GoToCastleTask extends MainStoryTask {
        private final String castleName;

        public GoToCastleTask(String castleName) {
            super("Reward at " + castleName + "");
            this.castleName = castleName;
        }

        @Override
        public String getText() {
            if (internalStep == 0) {
                return "Travel to " + castleName + " to claim " + InitialStoryPart.REWARD_GOLD +
                        " gold, your reward for dealing with the frogmen problem. ";
            }
            return "Complete the '" + RescueMissionQuest.QUEST_NAME + "' Quest";
        }

        @Override
        public boolean isComplete() {
            return internalStep > 1;
        }
    }
}

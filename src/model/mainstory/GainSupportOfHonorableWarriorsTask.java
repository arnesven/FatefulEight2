package model.mainstory;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;
import model.quests.Quest;
import model.races.Race;
import model.states.DailyEventState;
import util.MyTriplet;

import java.awt.*;
import java.util.List;

public class GainSupportOfHonorableWarriorsTask extends GainSupportOfRemotePeopleTask {
    private static final int INITIAL_STEP = 0;
    private final boolean completed;
    private int step = INITIAL_STEP;

    public GainSupportOfHonorableWarriorsTask() {
        super(WorldBuilder.EASTERN_PALACE_LOCATION);
        this.completed = false;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Honorable Warriors") {
            @Override
            public String getText() {
                return "Gain the support of the Honorable Warriors in the Far Eastern town.";
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfHonorableWarriorsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfHonorableWarriorsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }


    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        return null;
    }

    public DailyEventState generateEvent(Model model) {
        if (step == INITIAL_STEP) {
            return new JustArrivedInTownEvent(model);
        }
        return null;
    }

    private class JustArrivedInTownEvent extends DailyEventState {
        public JustArrivedInTownEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("As you" + (model.getParty().size() > 1 ? "r party" : "") +
                    " stroll into this exotic town you notice everybody is staring at you. " +
                    "Children are pointing, some laughing, some clinging to their parents. A man approaches you.");
            showRandomPortrait(model, Classes.FARMER, Race.EASTERN_HUMAN,"Villager");
            portraitSay("You don't belong here outsider. What's your business?");
            waitForReturn();
            step++;
        }
    }
}

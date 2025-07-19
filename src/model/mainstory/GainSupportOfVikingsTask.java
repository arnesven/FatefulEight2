package model.mainstory;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.mainstory.vikings.MeetWithChieftainEvent;
import model.mainstory.vikings.NotAdmittedToLonghouseEvent;
import model.map.WorldBuilder;
import model.map.locations.VikingVillageLocation;
import model.quests.SavageVikingsQuest;
import model.races.Race;
import model.states.DailyEventState;
import util.MyPair;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class GainSupportOfVikingsTask extends GainSupportOfRemotePeopleTask {
    private static final String REMOTE_PEOPLE_NAME = "The Vikings";

    public static final int INITIAL_STEP = 0;
    private static final int QUEST_DONE = 1;
    private static final int LOKI_MET = 2;
    private static final int EATING_CONTEST_DONE = 3;
    private static final int SPRINTING_CONTEST_DONE = 4;
    private static final int WRESTLING_CONTEST_DONE = 5;

    private final AdvancedAppearance chieftainPortrait;

    private boolean completed = false;
    private int step = INITIAL_STEP;

    public GainSupportOfVikingsTask(Model model) {
        super(WorldBuilder.VIKING_VILLAGE_LOCATION);
        chieftainPortrait = PortraitSubView.makeRandomPortrait(Classes.VIKING_CHIEF, Race.NORTHERN_HUMAN, false);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask(REMOTE_PEOPLE_NAME) {

            @Override
            public String getText() {
                String first = "Gain the support of the Vikings of the North.";
                if (step > INITIAL_STEP) {
                    first += "\n\nYou have managed to persuade the vikings you are not an enemy.";
                }

                if (step >= WRESTLING_CONTEST_DONE) {
                    first += "\n\nYou have passed Loki's initial tests. The final test is to accompany " +
                            "the Vikings on a raid on the Sixth Order monks on Faith Island. " +
                            "\n\nAlternatively you could warn the monks of the upcoming raid.";
                } else if (step >= LOKI_MET) {
                    first += "\n\nIn order to ally with the Vikings, you must prove to Chieftain Loki that you are " +
                            "a true viking by passing certain 'tests'.";
                }
                return first;
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfVikingsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfVikingsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }


    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        if (model.getCurrentHex().getLocation() instanceof VikingVillageLocation &&
                step == INITIAL_STEP) {
            return new MyTriplet<>(SavageVikingsQuest.QUEST_NAME,
                    model.getParty().getLeader().getAppearance(), "Yourself");
        }
        return null;
    }

    @Override
    public void setQuestSuccessful() {
        step = QUEST_DONE;
    }

    @Override
    public void addFactionString(List<MyPair<String, String>> result) {
        if (isCompleted()) { // TODO: Only if helped in raid
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Ally"));
        } else if (step == INITIAL_STEP) {
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Persona Non Grata"));
        } else {
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Friend"));
        }
    }

    public DailyEventState generateEvent(Model model, boolean fromLonghouse) {
        if (fromLonghouse) {
            if (step == INITIAL_STEP) {
                return new NotAdmittedToLonghouseEvent(model, this);
            } else if (step >= QUEST_DONE) {
                return new MeetWithChieftainEvent(model, true, this);
            }
        }
        if (step == INITIAL_STEP) {
            return new JustArrivedInVikingTownEvent(model);
        }
        return null;
    }

    public AdvancedAppearance getChieftainPortrait() {
        return chieftainPortrait;
    }

    public boolean isEatingContestDone() {
        return step >= GainSupportOfVikingsTask.EATING_CONTEST_DONE;
    }

    public void setEatingContestDone() {
        step = GainSupportOfVikingsTask.EATING_CONTEST_DONE;
    }

    public boolean isSprintingContestDone() {
        return step >= GainSupportOfVikingsTask.SPRINTING_CONTEST_DONE;
    }

    public void setSprintingContestDone() {
        step = GainSupportOfVikingsTask.SPRINTING_CONTEST_DONE;
    }

    public boolean isWrestlingContestDone() {
        return step >= GainSupportOfVikingsTask.WRESTLING_CONTEST_DONE;
    }

    public void setWrestlingContestDone() {
        step = GainSupportOfVikingsTask.WRESTLING_CONTEST_DONE;
    }

    public void setLokiMet() {
        step = GainSupportOfVikingsTask.LOKI_MET;
    }

    public boolean isLokiMet() {
        return step >= GainSupportOfVikingsTask.LOKI_MET;
    }
}

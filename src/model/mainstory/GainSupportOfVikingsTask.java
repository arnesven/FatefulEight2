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
import model.map.locations.MonasteryLocation;
import model.map.locations.VikingVillageLocation;
import model.quests.SavageVikingsQuest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.events.VisitMonasteryEvent;
import util.MyPair;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class GainSupportOfVikingsTask extends GainSupportOfRemotePeopleTask {
    public static final String CHIEFTAIN = "Chieftain Loki";
    public static final String CHIEFTAIN_NAME = "Loki";
    private static final String REMOTE_PEOPLE_NAME = "The Vikings";

    public static final int INITIAL_STEP = 0;
    private static final int QUEST_DONE = 1;
    private static final int LOKI_MET = 2;
    private static final int EATING_CONTEST_DONE = 3;
    private static final int SPRINTING_CONTEST_DONE = 4;
    private static final int WRESTLING_CONTEST_DONE = 5;
    private static final int COMPLETED_MONASTARY_RAIDED = 6;
    private static final int MONKS_WARNED = 7;
    private static final int COMPLETED_RAID_REPELLED = 8;

    private final AdvancedAppearance chieftainPortrait;

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

                if (step == MONKS_WARNED) {
                    return first + "\n\n" +
                            "Instead of gaining the support of the Vikings you have warned the Sixth Order Monks " +
                            "on the Isle of Faith about the Viking raid. The monks have asked you to arm them " +
                            "and train them to defend themselves against the Vikings.";
                }

                if (step == COMPLETED_RAID_REPELLED) {
                    return first + "\n\n" +
                            "Instead of gaining the support of the Vikings you aided the Sixth Order Monks " +
                            "on the Isle of Faith against a Viking raid. You have gained the support of the " +
                            "Sixth Order Monk.\n\nCompleted";
                }

                if (step == COMPLETED_MONASTARY_RAIDED) {
                    return first + "\n\n" +
                            "You proved yourself as a True Viking by participating in a raid on the Monastary " +
                            "on the Isle of Faith. You have gained the support of the Vikings of the North.\n\n" +
                            "Completed";
                }

                if (step > INITIAL_STEP) {
                    first += "\n\nYou have managed to persuade the vikings you are not an enemy.";
                }

                if (step >= WRESTLING_CONTEST_DONE) {
                    first += "\n\nYou have passed Loki's initial tests. The final test is to accompany " +
                            "the Vikings on a raid on the Sixth Order monks on the Isle of Faith. " +
                            "\n\nAlternatively you could warn the monks of the upcoming raid.";
                } else if (step >= LOKI_MET) {
                    first += "\n\nIn order to ally with the Vikings, you must prove to " + CHIEFTAIN + " that you are " +
                            "a true viking by passing certain 'tests'."  +
                            "\n\nAlternatively you could warn the monks on the Isle of Faith of the upcoming Viking raid.";
                }
                return first;
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfVikingsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                if (step >= WRESTLING_CONTEST_DONE) {
                    return WorldBuilder.MONASTERY_POSITION;
                }
                return GainSupportOfVikingsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return step == COMPLETED_MONASTARY_RAIDED || step == COMPLETED_RAID_REPELLED;
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
        if (step == COMPLETED_MONASTARY_RAIDED) {
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Ally"));
        } else if (step == INITIAL_STEP) {
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Persona Non Grata"));
        } else {
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Friend"));
        }

        if (step == COMPLETED_RAID_REPELLED) {
            result.add(new MyPair<>(VisitMonasteryEvent.FACTION_NAME, "Ally"));
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
        return step >= EATING_CONTEST_DONE;
    }

    public void setEatingContestDone() {
        step = EATING_CONTEST_DONE;
    }

    public boolean isSprintingContestDone() {
        return step >= SPRINTING_CONTEST_DONE;
    }

    public void setSprintingContestDone() {
        step = SPRINTING_CONTEST_DONE;
    }

    public boolean isWrestlingContestDone() {
        return step >= WRESTLING_CONTEST_DONE;
    }

    public void setWrestlingContestDone() {
        step = WRESTLING_CONTEST_DONE;
    }

    public void setLokiMet() {
        step = LOKI_MET;
    }

    public boolean isLokiMet() {
        return step >= LOKI_MET;
    }

    public void setMonastaryRaided() {
        step = COMPLETED_MONASTARY_RAIDED;
    }

    public boolean isMonastaryRaided() {
        return step == COMPLETED_MONASTARY_RAIDED;
    }
}

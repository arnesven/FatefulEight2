package model.mainstory;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.map.HexLocation;
import model.map.WorldBuilder;
import model.map.locations.PyramidLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.events.InteractWithJequenEvent;
import model.states.events.MeetWithJequenEvent;
import util.MyPair;
import util.MyRandom;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GainSupportOfJungleTribeTask extends GainSupportOfRemotePeopleTask {
    private static final int INITIAL_STEP = 0;
    private static final int JEQUEN_MET = 1;
    private static final String REMOTE_PEOPLE_NAME = "Jungle Tribe";
    private final AdvancedAppearance jequenPortrait;
    private final String crownLocation;
    private boolean completed = false;
    private int step = INITIAL_STEP;
    private Set<String> cluesGiven = new HashSet<>();

    public GainSupportOfJungleTribeTask(Model model) {
        super(WorldBuilder.JUNGLE_VILLAGE_LOCATION);
        jequenPortrait = PortraitSubView.makeRandomPortrait(Classes.None, Race.SOUTHERN_HUMAN, false);
        this.crownLocation = MyRandom.randInt(4) == 0 ? null : MyRandom.sample(getPyramidList(model)).getName();
        if (crownLocation == null) {
            System.out.println("Jade Crown is hidden in village.");
        } else {
            System.out.println("Jade crown is at " + crownLocation + ".");
        }
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Jungle Tribe") {
            @Override
            public String getText() {
                if (step == INITIAL_STEP) {
                    return "Travel to the southern continent and gain the support of the Jungle Tribe which inhabits that area.";
                }
                String extraFromClues = "";
                for (String str : cluesGiven) {
                    extraFromClues += "\n\nThe Crown may be in the " + str + ".";
                }

                return "You've met with prince Jequen who is the sole heir to the throne of the Southern Kingdom. But he cannot " +
                        "claim the regency without the Jade Crown." + extraFromClues;
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfJungleTribeTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfJungleTribeTask.this.getPosition();
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

    @Override
    public void setQuestSuccessful() {
        this.completed = true;
    }

    @Override
    public void addFactionString(List<MyPair<String, String>> result) {
        if (jequenMet()) {
            result.add(new MyPair<>(REMOTE_PEOPLE_NAME, "Friend"));
        }
    }

    @Override
    public void setCompleted() {
        this.completed = true;
    }

    @Override
    public boolean supportsFromTheSea() {
        return false;
    }

    @Override
    public CharacterAppearance getLeaderPortrait() {
        return PortraitSubView.makeRandomPortrait(Classes.AMZ, Race.SOUTHERN_HUMAN); // TODO: Fix
    }

    @Override
    public String getLeaderName() {
        return "King Jequen";
    }

    public AdvancedAppearance getJequenPortrait() {
        return jequenPortrait;
    }

    public static GainSupportOfJungleTribeTask getTask(Model model) {
        for (StoryPart sp : model.getMainStory().getStoryParts()) {
            if (sp instanceof PartSixStoryPart) {
                return (GainSupportOfJungleTribeTask) ((PartSixStoryPart) sp).getRemotePeopleTask();
            }
        }
        return null;
    }

    public GameState generateJequenEvent(Model model) {
        if (jequenMet()) {
            return new InteractWithJequenEvent(model, this);
        }
        return new MeetWithJequenEvent(model, this);
    }

    public void setJequenMet() {
        step = JEQUEN_MET;
    }

    public boolean jequenMet() {
        return step == JEQUEN_MET;
    }

    public DailyEventState generateTribeCommonerEvent(Model model) {
        if (jequenMet()) {
            int dieRoll = MyRandom.rollD6();
            if (dieRoll == 1) {
                return new JungleTribeKidEvent(model, this); // Pretty useless.
            }
            if (dieRoll < 4) {
                return new JungleTribeCommonerEvent(model, this);
            }
            if (dieRoll < 6) {
                return new JungleTribeElderEvent(model, this);
            }
            //return new FriendOfJaquarEvent(model); // Knows if jaquar came back and where Jaquar may have hid the crown.
        }
        return null;
    }

    public void giveClueAbout(PyramidLocation pyramid) {
        cluesGiven.add(pyramid.getName());
    }

    public PyramidLocation getPyramidClue(Model model, int likelihood) {
        List<PyramidLocation> pyramids = getPyramidList(model);
        if (crownLocation != null) {
            if (MyRandom.rollD10() <= likelihood) {
                return getCrownLocation(model);
            }
            do {
                PyramidLocation pyra = MyRandom.sample(pyramids);
                if (!pyra.getName().equals(crownLocation)) {
                    return pyra;
                }
            } while (true);
        }
        return MyRandom.sample(pyramids);
    }

    private PyramidLocation getCrownLocation(Model model) {
        return (PyramidLocation) model.getWorld().getLocationByName(crownLocation);
    }

    private static List<PyramidLocation> getPyramidList(Model model) {
        return List.of((PyramidLocation)model.getWorld().getLocationByName("Rubiq Pyramid"),
                (PyramidLocation)model.getWorld().getLocationByName("Qanoi Pyramid"),
                (PyramidLocation)model.getWorld().getLocationByName("Sudoq Pyramid"));
    }
}

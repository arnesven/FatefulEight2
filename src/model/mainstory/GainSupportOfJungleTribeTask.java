package model.mainstory;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
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
    private static final int CROWN_RECOVERED = 2;
    private static final int CROWN_GIVEN_TO_JEQUEN = 3;
    private static final String REMOTE_PEOPLE_NAME = "Jungle Tribe";
    private final AdvancedAppearance jequenPortrait;
    private final String crownLocation;
    private boolean completed = false;
    private int step = INITIAL_STEP;
    private Set<String> cluesGiven = new HashSet<>();
    private AdvancedAppearance friendOfJaqarPortrait;

    private boolean friendKnown = false;
    private boolean jaquarTruthKnown = false;

    public GainSupportOfJungleTribeTask(Model model) {
        super(WorldBuilder.JUNGLE_VILLAGE_LOCATION);
        jequenPortrait = PortraitSubView.makeRandomPortrait(Classes.None, Race.SOUTHERN_HUMAN, false);
        friendOfJaqarPortrait = PortraitSubView.makeRandomPortrait(Classes.None, Race.SOUTHERN_HUMAN, true);
        this.crownLocation = null; //MyRandom.randInt(4) == 0 ? null : MyRandom.sample(getPyramidList(model)).getName();
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
                if (step == JEQUEN_MET) {
                    String extra = "";
                    for (String str : cluesGiven) {
                        extra += "\n\nThe Crown may be in the " + str + ".";
                    }

                    if (friendKnown && jaquarTruthKnown) {
                        extra += "\n\nYou've talked to a friend of Jequen's father, prince Jaquar. He informed you that " +
                                "prince Jaquar returned with the crown many years ago. It may be hidden somewhere in the village.";
                    }

                    return "You've met with prince Jequen who is the sole heir to the throne of the Southern Kingdom. But he cannot " +
                            "claim the regency without the Jade Crown." + extra;
                }
                // CROWN GIVEN TO JEQUEN
                return "You recovered the Jade Crown and given it to prince Jequen. With it he will able to claim regency over the " +
                        "Jungle Tribe and the Southern Kingdom. As king, Jequen has promised his support to you.\n\nCompleted";
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
        step = CROWN_RECOVERED;
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
        return jequenPortrait;
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
            int dieRoll = MyRandom.rollD10();
            if (dieRoll == 1) {
                return new JungleTribeKidEvent(model, this); // Pretty useless.
            }
            if (dieRoll < 6) {
                return new JungleTribeCommonerEvent(model, this);
            }
            if (dieRoll < 10) {
                return new JungleTribeElderEvent(model, this);
            }
            return new FriendOfJaquarEvent(model, this);
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

    public AdvancedAppearance getFriendPortrait() {
        return friendOfJaqarPortrait;
    }

    public boolean isFriendOfJaquarKnown() {
        return friendKnown;
    }

    public void setFriendOfJaquarKnown(boolean b) {
        friendKnown = b;
    }

    public boolean isCrownInVillage() {
        return crownLocation == null;
    }

    public void setTruthAboutJaquarKnown(boolean b) {
        jaquarTruthKnown = b;
    }

    public boolean isJaquarTruthKnown() {
        return jaquarTruthKnown;
    }

    public void setCrownGivenToJequen() {
        step = CROWN_GIVEN_TO_JEQUEN;
        completed = true;
    }
}

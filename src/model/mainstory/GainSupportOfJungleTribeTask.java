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
import model.quests.Quest;
import model.races.Race;
import model.states.GameState;
import model.states.events.MeetWithJequenEvent;
import util.MyPair;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class GainSupportOfJungleTribeTask extends GainSupportOfRemotePeopleTask {
    private final AdvancedAppearance jequenPortrait;
    private boolean completed = false;

    public GainSupportOfJungleTribeTask(Model model) {
        super(WorldBuilder.JUNGLE_VILLAGE_LOCATION);
        jequenPortrait = PortraitSubView.makeRandomPortrait(Classes.None, Race.SOUTHERN_HUMAN, false);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Jungle Tribe") {
            @Override
            public String getText() {
                return "Travel to the southern continent and gain the support of the Jungle Tribe which inhabits that area." +
                        (completed ? "\n\nCompleted" : "");
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
        // TODO
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
        return new MeetWithJequenEvent(model);
    }
}

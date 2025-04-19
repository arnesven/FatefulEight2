package model.mainstory;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.OldManHairStyle;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.NoClass;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;
import model.quests.Quest;
import model.races.AllRaces;
import model.races.Race;
import model.states.DailyEventState;
import util.MyTriplet;
import view.MyColors;
import view.subviews.PortraitSubView;

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

    public DailyEventState generateEvent(Model model, boolean waterMill) {
        if (step == INITIAL_STEP) {
            if (waterMill) {
                return new VisitMikosHomeEvent(model, true);
            }
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
            leaderSay("We've come to conduct diplomatic negotiations with your people. Who among you do you call leader?");
            portraitSay("Our Lord Shingen rules this land. But he will surely not see a lowly outsider as yourself.");
            leaderSay("Why not? " + imOrWere() + " not just some scruffy vagabond" + (model.getParty().size() > 1 ? "s." : "."));
            portraitSay("Nevertheless, I'm sure our lord will not meet with you. " +
                    "You had better go see old Miko, he is wise and knows most about western affairs among us. He will advise you.");
            leaderSay("Fine, show " + meOrUs() + " to this Miko fellow.");
            println("The villager shows you to an old mill. Inside, an old, blind emaciated man sits on the floor.");
            portraitSay("Miko, these outsiders are from the west. They say they have urgent business with Lord Shingen.");
            new VisitMikosHomeEvent(model, false).doEvent(model);

        }
    }

    private class VisitMikosHomeEvent extends DailyEventState {
        private final boolean withIntro;

        public VisitMikosHomeEvent(Model model, boolean withIntro) {
            super(model);
            this.withIntro = withIntro;
        }

        @Override
        protected void doEvent(Model model) {
            if (withIntro) {
                setCurrentTerrainSubview(model);
                println("You step inside the old mill. The turning of the big wooden wheel thumps eerily. " +
                        "Upstairs you find an old, blind emaciated man sitting on the floor");
            }
            step++;
            model.getLog().waitForAnimationToFinish();
            CharacterClass grayNoneClass = new NoClass(MyColors.GRAY_RED);
            AdvancedAppearance app = (AdvancedAppearance) PortraitSubView.makeOldPortrait(grayNoneClass, AllRaces.EASTERN_HUMAN, false);
            app.setHairStyle(new OldManHairStyle());
            app.setClass(grayNoneClass);
            showExplicitPortrait(model, app, "Miko");
            forcePortraitEyes(true);
            waitForReturnSilently();
            if (withIntro) {
                portraitSay("Greetings travellers, what is your business in our peaceful town?");
                leaderSay("We've come to conduct diplomatic negotiations with your people. Who among you do you call leader?");
                portraitSay("Our Lord Shingen rules this land. But he will surely not see a lowly outsider as yourself.");
                leaderSay("Why not? " + imOrWere() + " not just some scruffy vagabond" + (model.getParty().size() > 1 ? "s." : "."));
                portraitSay("Nevertheless, I'm sure our lord will not meet with you.");
            } else {
                leaderSay("Yes, urgent indeed.");
            }
        }
    }
}

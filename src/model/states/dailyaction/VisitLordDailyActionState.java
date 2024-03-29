package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.TimeOfDay;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.events.SilentNoEventState;
import model.tasks.SummonTask;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.TownHallSubView;

import java.util.List;
import java.awt.*;

public abstract class VisitLordDailyActionState extends AdvancedDailyActionState {
    private final Summon summon;
    private final UrbanLocation location;
    private boolean spentNight;

    public VisitLordDailyActionState(Model model, Summon summon, UrbanLocation location, boolean breakIn) {
        super(model);
        this.summon = summon;
        this.location = location;
        spentNight = false;
        if (!breakIn) {
            addNode(4, 3, new TalkToLordNode());
        }
        addNode(3, 7, new ExitLocaleNode("Leave " + location.getLordDwelling(), location.getExitSprite()));
        addNode(1, 1, new LordTreasuryNode(model, location, breakIn));
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected abstract DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix);

    private class TalkToLordNode extends DailyActionNode {
        public TalkToLordNode() {
            super("Talk to " + location.getLordTitle());
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new AnswerSummonDailyAction(model);
        }

        @Override
        public Sprite getBackgroundSprite() {
            return TownHallSubView.RUG;
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            if (summon == null) {
                printQuote(location.getLordName(), "Excuse me, what are you doing in here? " +
                        "I've told my servants I'm not to be disturbed with the trifles of commoners.");
                return false;
            }
            if (state.isEvening() && summon.getStep() != Summon.COMPLETE) {
                printQuote(location.getLordName(), "I'm sorry but it's too late in the day now. Please come back tomorrow.");
                return false;
            }
            return true;
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
            if (!spentNight) {
                if (state.isMorning()) {
                    model.setTimeOfDay(TimeOfDay.MIDDAY);
                } else if (!state.isEvening()) {
                    model.setTimeOfDay(TimeOfDay.EVENING);
                }
            }
        }

        @Override
        public boolean returnNextState() {
            return spentNight;
        }
    }

    private class AnswerSummonDailyAction extends GameState {

        public AnswerSummonDailyAction(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            new InnerPortraitEvent(model).run(model);
            if (spentNight) {
                return new LodgingState(model, true).run(model);
            }
            return new SilentNoEventState(model);
        }

        private class InnerPortraitEvent extends DailyEventState {
            public InnerPortraitEvent(Model model) {
                super(model);
            }

            @Override
            protected void doEvent(Model model) {
                String lord = location.getLordName();
                showExplicitPortrait(model, model.getLordPortrait(location), location.getLordName());
                if (summon.getStep() == Summon.ACCEPTED) {
                    String leaderName = model.getParty().getLeader().getName();
                    portraitSay("Hello there. " + leaderName + ", I presume? I've been expecting you.");
                    leaderSay("Yes, that's me. Who are you?");
                    portraitSay("I'm " + lord + ". I'm in charge here. First of all, " +
                            "let me formally welcome you to " + location.getPlaceName() +
                            ", I hope you like our " + location.getLocationType() + ".");
                    model.getParty().randomPartyMemberSay(model,
                            List.of("Enough with the formalities. What is it you want?",
                                    "Get on with it, I haven't got all day!", "It's pleasant enough I suppose.",
                                    "Sure, it's great. Now what do you want?", "Thanks. Can I help you?", "How do you know my name?"));
                    portraitSay("Your reputation has preceded you and I was wondering if you might be able to help me with a problem of mine.");
                    summon.increaseStep();
                }
                if (summon.getStep() != Summon.COMPLETE) {
                    SummonTask task = summon.getTask(model, location);
                    task.doTask(model);
                    if (summon.getStep() == Summon.COMPLETE) {
                        summon.setCompletedOnDay(model.getDay());
                    }
                }

                if (summon.getStep() == Summon.COMPLETE) {
                    portraitSay("Thanks again for helping me with my problem. " +
                            "Please, stay for supper and spend the night, there's room for everyone.");
                    print("Do you wish to spend the night here? (Y/N): ");
                    if (yesNoInput()) {
                        spentNight = true;
                    }
                }
            }
        }
    }
}

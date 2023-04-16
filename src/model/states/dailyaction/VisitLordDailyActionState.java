package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.TimeOfDay;
import model.map.UrbanLocation;
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

    public VisitLordDailyActionState(Model model, Summon summon, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
        spentNight = false;
        addNode(4, 3, new TalkToLordNode());
        addNode(3, 7, new ExitLocaleNode("Leave " + location.getLordDwelling(), location.getExitSprite()));
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
            if (state.isEvening() && summon.getStep() != Summon.COMPLETE) {
                state.println(location.getLordName() + ": \"I'm sorry but it's too late in the day now. Please come back tomorrow.\"");
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
            String lord = location.getLordName();
            if (summon.getStep() == Summon.ACCEPTED) {
                String leaderName = model.getParty().getLeader().getName();
                println(lord + ": \"Hello there. " + leaderName + ", I presume? I've been expecting you.\"");
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Yes, that's me. Who are you?");
                println(lord + ": \"I'm " + lord + ". I'm in charge here. First of all, " +
                        "let me formally welcome you to " + location.getPlaceName() +
                        ", I hope you like our " + location.getLocationType() + ".\"");
                model.getParty().randomPartyMemberSay(model,
                        List.of("Enough with the formalities. What is it you want?",
                                "Get on with it, I haven't got all day!", "It's pleasant enough I suppose.",
                                "Sure, it's great. Now what do you want?", "Thanks. Can I help you?", "How do you know my name?"));
                println(lord + ": \"Your reputation has preceded you and I was wondering if you might be able to help me with a problem of mine.\"");
                summon.increaseStep();
            }
            if (summon.getStep() != Summon.COMPLETE) {
                SummonTask task = summon.getTask(model, location);
                task.doTask(model);
            }

            if (summon.getStep() == Summon.COMPLETE) {
                println(lord + ": \"Thanks again for helping me with my problem. " +
                        "Please, stay for supper and spend the night, there's room for everyone.\"");
                print("Do you wish to spend the night here? (Y/N): ");
                if (yesNoInput()) {
                    spentNight = true;
                }
            }

            if (spentNight) {
                return new LodgingState(model, true).run(model);
            }
            return new SilentNoEventState(model);
        }
    }
}

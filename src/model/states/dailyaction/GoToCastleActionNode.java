package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.KeepSubView;

import java.awt.*;

public class GoToCastleActionNode extends DailyActionNode {
    private final CastleLocation castle;
    private boolean admitted = false;

    public GoToCastleActionNode(CastleLocation location) {
        super("Visit Keep");
        this.castle = location;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        UrbanLocation location = ((UrbanLocation)model.getCurrentHex().getLocation());
        DailyEventState mainStoryEvent = null;
        if (model.getMainStory().isStarted()) {
            mainStoryEvent = model.getMainStory().getVisitLordEvent(model, location);
        }
        if (mainStoryEvent != null) {
            return  mainStoryEvent;
        } else if (model.getParty().getSummons().containsKey(location.getPlaceName())) {
            Summon summon = model.getParty().getSummons().get(location.getPlaceName());
            if (summon.getStep() == Summon.ACCEPTED) {
                state.println("Guard: \"Hey you! Stop right there! Where do you think you're going?\"");
                state.leaderSay("Uhm, I'm going to visit the " + castle.getLordTitle() + ".");
                state.println("Guard: \"Do you have an invitation?\"");
                state.leaderSay("Yes, it's right here...");
                state.println("Guard: \"Very well, proceed inside.\"");
                model.getLog().waitForAnimationToFinish();
            } else {
                state.println("You were admitted to the keep.");
            }
            admitted = true;
            return new VisitCastleLordDailyActionNode(model, summon, location);
        }
        return new VisitCastleEvent(model);
    }

    @Override
    public boolean returnNextState() {
        return admitted;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) { }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isEvening()) {
            state.println("The castle is closed now. Try again tomorrow.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {     }

    private class VisitCastleEvent extends GameState {
        public VisitCastleEvent(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            println("Guard: \"Hey you! Stop right there!\"");
            leaderSay("Who, me?");
            println("Guard: \"Yes you. Where do you think you're going?\"");
            leaderSay("Uhm, I'm going to visit the " + castle.getLordTitle() + ".");
            println("Guard: \"No you're not. Not without the proper invitation and I haven't been informed of any audiences today. Be on your way!\"");
            leaderSay("But...");
            println("Guard: \"No exceptions! Now off you go. Can't have any old riff raff hanging about.\"");
            if (model.getParty().size() == 1) {
                leaderSay("The nerve...#");
            } else {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                model.getParty().partyMemberSay(model, other, "Come on, let's just go. This guy isn't going to let us in.");
            }
            return new EveningState(model);
        }
    }

    private static class VisitCastleLordDailyActionNode extends VisitLordDailyActionState {
        public VisitCastleLordDailyActionNode(Model model, Summon summon, UrbanLocation location) {
            super(model, summon, location);
        }

        @Override
        protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
            return new KeepSubView(advancedDailyActionState, matrix);
        }
    }
}

package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import util.MyPair;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.KeepSubView;

import java.awt.Point;
import java.util.List;

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
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            return handleCastleBreakIn(model, state, location);
        }

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
            return new VisitCastleLordDailyActionNode(model, summon, location, false);
        }
        return new VisitCastleEvent(model);
    }

    private GameState handleCastleBreakIn(Model model, AdvancedDailyActionState state, UrbanLocation location) {
        state.print("The keep is closed, do you want to try to break in? (Y/N) ");
        if (state.yesNoInput()) {
            state.print("The gate is barred from the inside. " +
                    "One party member will have to climb the wall to the balcony while " +
                    "the rest of the party will remain outside. Do you want to continue? (Y/N) ");
            if (state.yesNoInput()) {
                MyPair<Boolean, GameCharacter> pair =
                        model.getParty().doSoloSkillCheckWithPerformer(model, state, Skill.Acrobatics, 8);
                if (pair.first) {
                    GameCharacter cat = pair.second;
                    model.getParty().benchPartyMembers(model.getParty().getPartyMembers());
                    model.getParty().unbenchPartyMembers(List.of(cat));
                    if (model.getParty().getLeader() != cat) {
                        model.getParty().setLeader(cat);
                        state.println("!" + cat.getName() + " has been set as the leader of the party.");
                    }
                    admitted = true;
                    return new VisitCastleLordDailyActionNode(model, null, location, true);
                } else {
                    state.println(pair.second.getName() + " fell off the keep wall and took 2 damage!");
                    pair.second.addToHP(-2);
                    if (pair.second.isDead()) {
                        DailyEventState.characterDies(model, state, pair.second,
                                " has fallen to " + GameState.hisOrHer(pair.second.getGender()) + " death!",
                                true);
                    } else {
                        model.getParty().partyMemberSay(model, pair.second, List.of("Ouch", "Lost my footing!",
                                "I think I've broken something... or some things.", "Yeouch!"));
                    }
                }

            }
        }
        admitted = true;
        return model.getCurrentHex().getEveningState(model, false, false);
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
        private final boolean breakIn;

        public VisitCastleLordDailyActionNode(Model model, Summon summon, UrbanLocation location, boolean breakIn) {
            super(model, summon, location, breakIn);
            this.breakIn = breakIn;
        }

        @Override
        protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
            return new KeepSubView(advancedDailyActionState, matrix, !breakIn);
        }
    }
}

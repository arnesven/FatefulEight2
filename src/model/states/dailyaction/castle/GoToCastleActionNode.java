package model.states.dailyaction.castle;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.mainstory.VisitLordEvent;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.VisitLordDailyActionState;
import model.states.events.LeagueOfMagesEvent;
import util.MyPair;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.KeepSubView;

import java.awt.Point;
import java.util.List;

public class GoToCastleActionNode extends DailyActionNode {
    private final CastleLocation castle;
    private boolean returnNextState = false;

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

        VisitLordEvent mainStoryEvent = null;
        if (model.getMainStory().isStarted()) {
            mainStoryEvent = model.getMainStory().getVisitLordEvent(model, location);
        }
        if (mainStoryEvent != null) {
            returnNextState = mainStoryEvent.returnEveningState();
            if (model.getParty().getSummons().containsKey(location.getPlaceName())) {
                state.print("Are you visiting the " + location.getLordTitle() +
                        " to discuss the mystery of the Crimson Pearl (Y) or do you have other business with " +
                        GameState.hisOrHer(location.getLordGender()) + " (N)? ");
                if (state.yesNoInput()) {
                    return mainStoryEvent;
                }
            } else {
                return mainStoryEvent;
            }
        }

        if (model.getParty().getSummons().containsKey(location.getPlaceName())) {
            return askAboutInvitation(model, state, location);
        }
        if (!model.getWarHandler().getWarsForKingdom((CastleLocation) location).isEmpty()) {
            return askAboutRecruit(model, state, location);
        }
        if (LeagueOfMagesEvent.isMember(model)) {
            return sayMember(model, state, location);
        }
        return new VisitCastleEvent(model);
    }

    private GameState sayMember(Model model, AdvancedDailyActionState state, UrbanLocation location) {
        state.printQuote("Guard", "Hey you! Stop right there! Where do you think you're going?");
        state.leaderSay("Into the keep. We're members of the League of Mages and we need access to the court mage's teleportation service.");
        state.printQuote("Guard", "My apologies. You can pass on through.");
        model.getLog().waitForAnimationToFinish();
        returnNextState = true;
        return new VisitCastleLordDailyActionNode(model, null, location, false);
    }

    private GameState askAboutRecruit(Model model, AdvancedDailyActionState state, UrbanLocation location) {
        state.printQuote("Guard", "Hey there! Are you one of the recruits?");
        state.print("Are you? (Y/N) ");
        if (!state.yesNoInput()) {
            state.print("You pretend like the Guard is talking to somebody else and continue toward the castle gate.");
            return new VisitCastleEvent(model);
        }
        state.leaderSay("Uhm, yes... " + (model.getParty().size() > 1 ? "we are" : "I am") + ".");
        state.printQuote("Guard", "Well you're late! Such tardiness won't be accepted in " +
                "the future. Don't you know we're at war? Go straight through and into the main chamber. " +
                "Talk to the commander. He'll introduce you to the drill sargent. He he, we'll make a soldier out of you.");
        state.leaderSay("Sir, yes sir!");
        returnNextState = true;
        return new VisitCastleLordDailyActionNode(model, null, location, false);
    }

    private GameState askAboutInvitation(Model model, AdvancedDailyActionState state, UrbanLocation location) {
        Summon summon = model.getParty().getSummons().get(location.getPlaceName());
        if (summon.getStep() == Summon.ACCEPTED && !VisitLordDailyActionState.hasMetLordThroughMainStory(model)) {
            state.printQuote("Guard", "Hey you! Stop right there! Where do you think you're going?");
            state.leaderSay("Uhm, I'm going to visit the " + castle.getLordTitle() + ".");
            state.printQuote("Guard", "Do you have an invitation?");
            state.leaderSay("Yes, it's right here...");
            state.printQuote("Guard", "Very well, proceed inside.");
            model.getLog().waitForAnimationToFinish();
        } else {
            state.println("You were admitted to the keep.");
        }
        returnNextState = true;
        return new VisitCastleLordDailyActionNode(model, summon, location, false);
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
                        state.printAlert(cat.getName() + " has been set as the leader of the party.");
                    }
                    returnNextState = true;
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
        returnNextState = true;
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    @Override
    public boolean returnNextState() {
        return returnNextState;
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
            printQuote("Guard", "Hey you! Stop right there!");
            leaderSay("Who, me?");
            printQuote("Guard", "Yes you. Where do you think you're going?");
            leaderSay("Uhm, I'm going to visit the " + castle.getLordTitle() + ".");
            printQuote("Guard", "No you're not. Not without the proper invitation and I haven't been informed of any audiences today. Be on your way!");
            leaderSay("But...");
            printQuote("Guard", "No exceptions! Now off you go. Can't have any old riff raff hanging about.");
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
        private final CastleLocation location;

        public VisitCastleLordDailyActionNode(Model model, Summon summon, UrbanLocation location, boolean breakIn) {
            super(model, summon, location, breakIn);
            this.breakIn = breakIn;
            if (!breakIn) {
                addNode(5, 3, new CourtMageNode((CastleLocation) location));
                addNode(2, 3, new CommanderNode((CastleLocation) location));
            }
            this.location = (CastleLocation)location;
        }

        @Override
        protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
            return new KeepSubView(advancedDailyActionState, matrix, !breakIn, location);
        }
    }
}

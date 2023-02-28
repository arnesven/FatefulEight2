package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.EveningState;
import model.states.GameState;
import view.sprites.Sprite;

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
        if (model.getParty().getSummons().containsKey(location.getPlaceName())) {
            state.println("Guard: \"Hey you! Stop right there! Where do you think you're going?\"");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Uhm, I'm going to visit the " + castle.getLordTitle() + ".");
            state.println("Guard: \"Do you have an invitation?\"");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Yes, it's right here...");
            state.println("Guard: \"Very well, proceed inside.\"");
            model.getLog().waitForAnimationToFinish();
            admitted = true;
            return new VisitLordDailyActionState(model, model.getParty().getSummons().get(location.getPlaceName()), location);
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
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Who, me?");
            println("Guard: \"Yes you. Where do you think you're going?\"");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Uhm, I'm going to visit the " + castle.getLordTitle() + ".");
            println("Guard: \"No you're not. Not without the proper invitation and I haven't been informed of any audiences today. Be on your way!\"");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "But...");
            println("Guard: \"No exceptions! Now off you go. Can't have any old riff raff hanging about.\"");
            if (model.getParty().size() == 1) {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "The nerve...#");
            } else {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                model.getParty().partyMemberSay(model, other, "Come on, let's just go. This guy isn't going to let us in.");
            }
            return new EveningState(model);
        }
    }
}

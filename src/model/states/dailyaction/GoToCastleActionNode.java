package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.states.EveningState;
import model.states.GameState;
import view.sprites.Sprite;

import java.awt.*;

public class GoToCastleActionNode extends DailyActionNode {
    private final CastleLocation castle;

    public GoToCastleActionNode(CastleLocation location) {
        super("Visit Castle");
        this.castle = location;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new VisitCastleEvent(model);
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
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }

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
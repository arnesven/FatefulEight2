package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.travellers.Traveller;
import view.sprites.Animation;
import view.sprites.Sprite;
import view.subviews.PartyManagementEveningSubView;
import view.subviews.SubView;

import java.awt.*;

public class TalkToTravellerNode extends DailyActionNode {
    private static final Point SHIFT = new Point(4, -4);
    private final Sprite sprite;
    private final Traveller traveller;

    public TalkToTravellerNode(Traveller traveller) {
        super("Talk to " + traveller.getName());
        this.sprite = traveller.getAvatarSprite();
        this.traveller = traveller;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TalkToTravellerState(model, traveller, this);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public Point getCursorShift() {
        return SHIFT;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Point p2 = new Point(p.x + 4, p.y);
        if (sprite instanceof Animation) {
            ((Animation) sprite).synch();
        }
        model.getScreenHandler().register(sprite.getName(), p2, sprite, 1);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private static class TalkToTravellerState extends GameState {
        private final Traveller traveller;
        private final TalkToTravellerNode node;

        public TalkToTravellerState(Model model, Traveller traveller, TalkToTravellerNode talkToTravellerNode) {
            super(model);
            this.traveller = traveller;
            this.node = talkToTravellerNode;
        }

        @Override
        public GameState run(Model model) {
            SubView subView = model.getSubView();
            if (subView instanceof PartyManagementEveningSubView) {
                ((PartyManagementEveningSubView)subView).addCalloutAtNode(node, 60);
            }
            int days = traveller.getRemainingDays(model);
            if (days < 0) {
                traveller.travellerSay(model, this, "You promised you would take me to "  +
                        traveller.getDestinationLocation(model).getName() + "! Are we even close?");
                leaderSay("Kind of.");
            } else if (days < 2) {
                traveller.travellerSay(model, this, "I hope we'll be getting to "  +
                        traveller.getDestinationLocation(model).getName() + " soon.");
                leaderSay("We're on our way there now.");
            } else {
                traveller.travellerSay(model, this,
                        "Thanks for taking me to " + traveller.getDestinationLocation(model).getName() + ".");
            }
            return null;
        }
    }
}

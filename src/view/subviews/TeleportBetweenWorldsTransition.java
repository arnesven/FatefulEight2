package view.subviews;

import model.Model;
import model.map.WorldType;

import java.awt.*;

public class TeleportBetweenWorldsTransition extends TeleportingTransition {
    public TeleportBetweenWorldsTransition(SubView fromView, SubView toView, String title, Point position) {
        super(fromView, toView, title, position, false);
    }

    @Override
    protected void moveParty(Model model, Point position, boolean inCaves) {
        model.goBetweenWorlds(WorldType.thePast, position);
    }

    public static void transition(Model model, MapSubView nextSubView, Point position) {
        TransitionView spiral = new TeleportBetweenWorldsTransition(model.getSubView(), nextSubView,
                nextSubView.getTitleText(model), position);
        model.setSubView(spiral);
        spiral.waitToBeOver();
        model.setSubView(nextSubView);
    }
}

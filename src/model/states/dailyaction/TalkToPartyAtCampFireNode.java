package model.states.dailyaction;

import model.Model;
import model.states.dailyaction.tavern.TalkToPartyNode;
import view.sprites.CampfireSprite;

import java.awt.*;

public class TalkToPartyAtCampFireNode extends TalkToPartyNode {

    private static final Point SHIFT = new Point(0, 2);

    private static final CampfireSprite CAMP_FIRE = new CampfireSprite();

    @Override
    public Point getCursorShift() {
        return SHIFT;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Point p2 = new Point(p.x, p.y + 4);
        model.getScreenHandler().register(CAMP_FIRE.getName(), p2, CAMP_FIRE);
    }
}

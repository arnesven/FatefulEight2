package model.states.dailyaction;

import model.Model;
import model.states.dailyaction.tavern.TalkToPartyNode;
import view.sprites.CampfireSprite;

import java.awt.*;

public class TalkToPartyAtCampFireNode extends TalkToPartyNode {

    private static final CampfireSprite CAMP_FIRE = new CampfireSprite();

    @Override
    public void drawYourself(Model model, Point p) {
        p.y += 4;
        model.getScreenHandler().register(CAMP_FIRE.getName(), p, CAMP_FIRE);
    }
}

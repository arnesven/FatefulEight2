package model.states.dailyaction.tavern;

import model.Model;
import model.states.dailyaction.ExitLocaleNode;
import view.subviews.TownHallSubView;

import java.awt.*;

public class ExitTavernNode extends ExitLocaleNode {

    public ExitTavernNode() {
        super("Leave Tavern", TownHallSubView.DOOR);
    }

    @Override
    public void drawYourself(Model model, Point p) {

    }
}

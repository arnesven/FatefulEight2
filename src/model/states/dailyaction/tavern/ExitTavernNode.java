package model.states.dailyaction.tavern;

import model.states.dailyaction.ExitLocaleNode;
import view.subviews.TownHallSubView;

public class ExitTavernNode extends ExitLocaleNode {

    public ExitTavernNode() {
        super("Leave Tavern", TownHallSubView.DOOR);
    }
}

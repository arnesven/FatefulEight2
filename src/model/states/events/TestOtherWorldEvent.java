package model.states.events;

import model.Model;
import model.map.WorldType;
import model.states.DailyEventState;

import java.awt.*;

public class TestOtherWorldEvent extends DailyEventState {
    public TestOtherWorldEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A portal opens in front of you. Do you pass through it?");
        if (yesNoInput()) {
            model.goBetweenWorlds(WorldType.other, new Point(5, 5));
        }
    }
}

package model.states.events;

import model.Model;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

public class HalfOrcEvent extends DailyEventState {
    public HalfOrcEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters a half-orc. This particular half-orc is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            println(" barbarian.");
            new BarbarianEvent(model).run(model);
        } else if (dieRoll <= 6) {
            print(" forester. He offers to train you in the ways of being a Forester, ");
            new ChangeClassEvent(model, Classes.FOR).areYouInterested(model);
        } else if (dieRoll <= 9) {
            println(" a bandit!");
            BanditEvent be = new BanditEvent(model);
            be.doEvent(model);
            // TODO: fleeing here will not lead to forced movement...
        } else {
            adventurerWhoMayJoin(model, Race.HALF_ORC);
        }
    }
}

package model.states.events;

import model.Model;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

public class HalfOrcEvent extends DailyEventState {
    private boolean didFlee;

    public HalfOrcEvent(Model model) {
        super(model);
        didFlee = false;
    }

    @Override
    public boolean haveFledCombat() {
        return didFlee;
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters a half-orc. This particular half-orc is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            println(" barbarian.");
            BarbarianEvent barb = new BarbarianEvent(model);
            barb.setRace(Race.HALF_ORC);
            barb.doEvent(model);
        } else if (dieRoll <= 6) {
            showRandomPortrait(model, Classes.FOR, Race.HALF_ORC, "Forester");
            print(" forester. He offers to train you in the ways of being a Forester, ");
            new ChangeClassEvent(model, Classes.FOR).areYouInterested(model);
        } else if (dieRoll <= 9) {
            println(" a bandit!");
            BanditEvent be = new BanditEvent(model);
            be.setRace(Race.HALF_ORC);
            be.doEvent(model);
            be.doEvent(model);
            this.didFlee = be.haveFledCombat();
        } else {
            adventurerWhoMayJoin(model, Race.HALF_ORC);
        }
    }
}

package model.states.events;

import model.Model;
import model.states.DailyEventState;
import util.MyRandom;

public class OasisEvent extends DailyEventState {
    private DailyEventState innerEvent;

    public OasisEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to oasis",
                "There's " + getDistantDescription() + " just beyond those dunes");
    }

    @Override
    public String getDistantDescription() {
        return "an oasis";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Oasis", "Exhausted from the heat, the party reaches a lush oasis in the " +
                "desert. There is a watering hole here and some much " +
                "needed shade. The party gains 3 rations.");
        model.getParty().addToFood(3);
        int roll = MyRandom.rollD10();
        println("However, places like these are rarely unoccupied...");
        if (roll <= 2) {
            BanditEvent be = new BanditEvent(model);
            be.doEvent(model);
            innerEvent = be;
        } else if (roll <= 4) {
            MerchantEvent me = new MerchantEvent(model);
            me.doEvent(model);
            innerEvent = me;
        } else if (roll <= 6) {
            NomadCampEvent nce = new NomadCampEvent(model);
            nce.doEvent(model);
            innerEvent = nce;
        } else if (roll <= 8) {
            HermitEvent he = new HermitEvent(model);
            he.doEvent(model);
            innerEvent = he;
        } else {
            UnicornEvent ue = new UnicornEvent(model);
            ue.doEvent(model);
            innerEvent = ue;
        }
    }

    @Override
    public boolean haveFledCombat() {
        return innerEvent.haveFledCombat();
    }
}

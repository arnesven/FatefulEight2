package model.states.events;

import model.Model;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

public class DwarfEvent extends DailyEventState {
    public DwarfEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Meet with dwarf", "I know a dwarf who lives around here");
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters a dwarf. This particular dwarf is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            println(" miner.");
            new MinerEvent(model, false, Race.DWARF).run(model);
        } else if (dieRoll <= 6) {
            println(" a skilled craftsman.");
            new ArtisanEvent(model, false).run(model); // TODO: Force this event to use Race=Dwarf
        } else if (dieRoll <= 9) {
            println(" a veteran.");
            new VeteranEvent(model, false).run(model);// TODO: Force this event to use Race=Dwarf
        } else {
            adventurerWhoMayJoin(model, Race.DWARF);
        }
    }
}

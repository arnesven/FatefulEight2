package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class MinerEvent extends DailyEventState {
    private boolean withIntro;

    public MinerEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public MinerEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.MIN, "Miner");
        if (withIntro) {
            print("The party encounters a miner. ");
        }
        print("The miner has all the gear needed to descend deep into " +
                "the earth, to dig for precious gems and metal ore. The " +
                "miner gladly demonstrates the gear and offers to teach you about the life of a miner, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.MIN);
        changeClassEvent.areYouInterested(model);
    }
}

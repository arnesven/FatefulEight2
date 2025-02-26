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
    public String getDistantDescription() {
        return "some people... I think they're half-orcs.";
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
            new SimpleGeneralInteractionEvent(model, Classes.FOR, Race.HALF_ORC, "Forester", " forester.") {
                @Override
                protected boolean doMainEventAndShowDarkDeeds(Model model) {
                    println("The half-orc offers to train you in the ways of being a forester, ");
                    new ChangeClassEvent(model, Classes.FOR).areYouInterested(model);
                    setCurrentTerrainSubview(model);
                    showExplicitPortrait(model, getPortrait(), "Forester");
                    return true;
                }

                @Override
                protected String getVictimSelfTalk() {
                    return "I'm a forester, a lumberjack and a ranger.";
                }
            }.doEvent(model);
        } else if (dieRoll <= 9) {
            println(" a bandit!");
            BanditEvent be = new BanditEvent(model);
            be.setRace(Race.HALF_ORC);
            be.doEvent(model);
            this.didFlee = be.haveFledCombat();
        } else {
            adventurerWhoMayJoin(model, Race.HALF_ORC);
        }
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Meet half-orc", "I know a half-orc living in this area.");
    }
}

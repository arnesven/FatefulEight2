package model.states.events;

import model.Model;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

public class HalflingEvent extends DailyEventState {
    private final int dieRoll;

    public HalflingEvent(Model model) {
        super(model);
        this.dieRoll = MyRandom.rollD10();
    }

    @Override
    public String getDistantDescription() {
        if (dieRoll <= 6 || dieRoll == 10) {
            return "a person traveling alone. I think it's a halfling";
        }
        return "some people... I think they're halflings";
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters a halfling. This particular halfling is a");
        if (dieRoll <= 2) {
            println(" stranger asking for directions.");
            ThiefEvent thief = new ThiefEvent(model, false);
            thief.doEvent(model);
        } else if (dieRoll <= 6) {
            new SimpleGeneralInteractionEvent(model, Classes.FARMER, Race.HALFLING, "Farmer", " farmer.") {
                @Override
                protected boolean doMainEventAndShowDarkDeeds(Model model) {
                    println("The half-ling completely ignores the party.");
                    return true;
                }

                @Override
                protected String getVictimSelfTalk() {
                    return "Just a farmer, minding my own business.";
                }
            }.doEvent(model);
        } else if (dieRoll <= 7) {
            print(" nobleman. ");
            NoblemanEvent noblemanEvent = new NoblemanEvent(model, Race.HALFLING);
            noblemanEvent.doEvent(model);
        } else if (dieRoll <= 9) {
            println(" bard.");
            JesterEvent jester = new JesterEvent(model, "Bard", "bard");
            jester.setRace(Race.HALFLING);
            jester.doEvent(model);
        } else if (dieRoll <= 10) {
            adventurerWhoMayJoin(model, Race.HALFLING);
        }
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit halfling", "I know a halfling living nearby");
    }
}

package model.states.events;

import model.Model;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.items.Equipment;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class FarmersChildEvent extends FarmerEvent {
    public FarmersChildEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit farmer with the kid",
                "I know a farmer nearby who wants adventurers to take on his kid as an apprentice");
    }

    @Override
    public String getDistantDescription() {
        return "Two people, probably a farmer with a son or daughter";
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        boolean gender = MyRandom.randInt(2) == 0;
        showEventCard("Farmer's Child", "The farmer begs you to take " + hisOrHer(getPortrait().getGender()) + " teenage " +
                (gender?"daughter":"son") + " as an apprentice adventurer. The kid seems" +
                " ready to take on the world, but an apprentice may turn out to be a" +
                "liability.");
        print("Are you interested? (Y/N) ");
        if (yesNoInput()) {
            GameCharacter gc = MyRandom.sample(model.getAvailableCharactersByGender(gender));
            gc.setLevel(0);
            gc.setClass(Classes.None);
            gc.setEquipment(new Equipment());
            RecruitState recruitState = new RecruitState(model, RecruitableCharacter.makeOneNamedRecruitable(gc));
            recruitState.run(model);
            setCurrentTerrainSubview(model);
        }
        new GuestEvent(model, getPortrait()).doEvent(model);
        return true;
    }

    @Override
    protected boolean isFreeRations() {
        return true;
    }
}

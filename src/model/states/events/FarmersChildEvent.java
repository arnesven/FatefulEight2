package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.items.Equipment;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class FarmersChildEvent extends DailyEventState {
    public FarmersChildEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        boolean gender = MyRandom.randInt(2) == 0;
        print("You encounter a farmer who's begging you to take his teenage " +
                (gender?"daughter":"son") + " as an apprentice adventurer. The kid seems" +
                " ready to take on the world, but an apprentice may turn out to be a" +
                "liability. Are you interested? (Y/N) ");
        if (yesNoInput()) {
            List<GameCharacter> list = new ArrayList<>();
            list.add(MyRandom.sample(model.getAvailableCharactersByGender(gender)));
            list.get(0).setLevel(0);
            list.get(0).setClass(Classes.None);
            list.get(0).setEquipment(new Equipment());
            RecruitState recruitState = new RecruitState(model, list);
            recruitState.run(model);
        }
        new GuestEvent(model).doEvent(model);
    }

    @Override
    protected boolean isFreeRations() {
        return true;
    }
}

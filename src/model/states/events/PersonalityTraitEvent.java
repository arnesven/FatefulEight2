package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public abstract class PersonalityTraitEvent extends DailyEventState {
    private final GameCharacter mainCharacter;
    private final PersonalityTrait trait;

    public PersonalityTraitEvent(Model model, PersonalityTrait trait, GameCharacter mainCharacter) {
        super(model);
        this.trait = trait;
        this.mainCharacter = mainCharacter;
    }

    public abstract boolean isApplicable(Model model);

    public GameCharacter getMainCharacter() {
        return mainCharacter;
    }

    public PersonalityTrait getTrait() {
        return trait;
    }

    protected void benchAllButMain(Model model) {
        List<GameCharacter> allButMain = new ArrayList<>(model.getParty().getPartyMembers());
        allButMain.remove(mainCharacter);
        model.getParty().benchPartyMembers(allButMain);
    }
}

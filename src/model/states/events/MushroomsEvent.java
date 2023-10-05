package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class MushroomsEvent extends DailyEventState {
    public MushroomsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A large patch of mushrooms cover the ground ahead. " +
                "The party is hungry and they do look delicious, but are " +
                "they edible?");
        print("Do you pick the mushrooms? (Y/N) ");
        if (yesNoInput()) {
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 10);
            if (result) {
                println("The party gains 10 rations!");
                model.getParty().addToFood(10);
                model.getParty().randomPartyMemberSay(model, List.of("What a treat!"));
            } else {
                println("Each party member suffers 2 damage.");
                List<GameCharacter> died = new ArrayList<>();
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    gc.addToHP(-2);
                    if (gc.isDead()) {
                        died.add(gc);
                    }
                }
                for (GameCharacter gc : died) {
                    RiverEvent.characterDies(model, this, gc, " died from food poisoning.", true);
                }
            }
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("Aww, my stomach is growling..."));
        }
    }
}

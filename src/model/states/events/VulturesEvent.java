package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.weapons.BowWeapon;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class VulturesEvent extends DailyEventState {
    public VulturesEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "some large birds, vultures";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Vultures", "Vultures have been circling over the party for some time now. Their shadows looming bigger and bigger.");
        model.getParty().randomPartyMemberSay(model, List.of("Those big turkeys are really getting annoying!"));
        model.getParty().randomPartyMemberSay(model, List.of("They're probably hoping to have us for dinner."));
        int kills = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getEquipment().getWeapon() instanceof BowWeapon) {
                SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, gc, Skill.Bows, 7, 15, 0);
                if (result.isSuccessful()) {
                    kills++;
                    println(gc.getName() + " shot down a vulture with " + hisOrHer(gc.getGender()) + " bow!");
                } else {
                    println(gc.getName() + " missed " + hisOrHer(gc.getGender()) + " shot.");
                }
            }
        }
        if (kills > 0) {
            randomSayIfPersonality(PersonalityTrait.jovial, new ArrayList<>(),
                    "Looks like meat is back on the menu folks!");
            model.getParty().addToFood(kills * 5);
            println("The party gains " + (kills * 5) + " rations.");
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("I don't want to end up in some bird's stomach."));
        }
    }
}

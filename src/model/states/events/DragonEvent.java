package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.enemies.DragonEnemy;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class DragonEvent extends DailyEventState {
    public DragonEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a very large creature... holy hells, it's a dragon";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Suddenly a powerful gust of wind catches the party off " +
                "guard. Then, the horror...");
        model.getParty().randomPartyMemberSay(model, List.of("DRAGON!!!"));
        randomSayIfPersonality(PersonalityTrait.brave, new ArrayList<>(), "To me everybody! " +
                "Don't let your hearts falter, we can do this!");
        runCombat(List.of(DragonEnemy.generateDragon('A')));
    }

}

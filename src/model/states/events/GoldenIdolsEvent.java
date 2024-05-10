package model.states.events;

import model.Model;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.TempleGuardEnemy;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class GoldenIdolsEvent extends DailyEventState {
    public GoldenIdolsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("One of the priests takes you on a tour of the temple. You " +
                "pass by a room and glance inside. Something glimmering " +
                "catches your eye. While the priest is busy telling the " +
                "other party members about some ancient spirit, you sneak " +
                "into the room and find many large gold-plated idols lining " +
                "the walls.");
        leaderSay("These must be worth a fortune!");
        println("What do you do?");
        int res = multipleOptionArrowMenu(model, 24, 12, List.of("Continue the tour", "Quickly snatch some idols", "Return later and ransack"));
        if (res == 0) {
            leaderSay("Better not do anything stupid.");
        } else if (res == 1) {
            println("The party gains 35 gold.");
            model.getParty().addToGold(35);
        } else {
            println("You return at midnight and the party completely empties the room with the golden idols.");
            model.getParty().randomPartyMemberSay(model, List.of("What a treasure hoard!"));
            showRandomPortrait(model, Classes.TEMPLE_GUARD, "Temple Guards");
            println("The party gains 135 gold!");
            model.getParty().addToGold(135);
            portraitSay("Hey you. What do you think your doing?");
            model.getParty().randomPartyMemberSay(model, List.of("Uh-oh. Busted..."));
            List<Enemy> guards = new ArrayList<>();
            for (int i = 0; i < 9; ++i) {
                guards.add(new TempleGuardEnemy('A'));
            }
            model.getParty().banFromTemple(model.getCurrentHex().getLocation().getName());
            runCombat(guards);
            if (model.getParty().isWipedOut()) {
                return;
            }
            println("You have been banned from " + model.getCurrentHex().getLocation().getName() + ".");
        }
    }
}

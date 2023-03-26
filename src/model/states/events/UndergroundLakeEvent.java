package model.states.events;

import model.Model;
import model.combat.CaveTheme;
import model.enemies.Enemy;
import model.enemies.OctopusEnemy;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.ArrowMenuSubView;

import java.awt.*;
import java.util.List;

public class UndergroundLakeEvent extends DailyEventState {
    public UndergroundLakeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party discovers an underground lake.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                "This might be a good place to rest, and to fill up our water skins.");
        model.getParty().randomPartyMemberSay(model, List.of("I don't know though... that lake gives me the creeps."));

        do {
            List<String> optionList = List.of(
                    "Get a little water",
                    "Get lots of water",
                    "Throw a rock in the lake",
                    "Do not disturb the lake");
            int result = multipleOptionArrowMenu(model, 24, 14, optionList);
            switch (result) {
                case 0:
                    getRations(model, 1);
                    break;
                case 1:
                    getRations(model, 8);
                    break;
                case 2:
                    println("You find a fist sized rock and throw it in the lake.");
                    startCombat(model, 5);
                    break;
                default:
                    println("You turn away from the lake. Just as you do you notice a a few bubbles bursting on the surface. " +
                            "Where did they come from?");
                    return;
            }
        } while (true);
    }


    private void getRations(Model model, int i) {
        if (model.getParty().getFood() == model.getParty().rationsLimit()) {
            println("You cannot carry anymore rations!");
        } else {
            int amount = Math.min(i, model.getParty().rationsLimit() - model.getParty().getFood());
            model.getParty().addToFood(amount);
            println("The party gains " + amount + " rations.");
            startCombat(model, amount);
        }
    }

    private void startCombat(Model model, int probability) {
        if (MyRandom.rollD10() < probability) {
            model.getParty().randomPartyMemberSay(model, List.of("Wha... wha... what's that!?"));
            println("A large hulk of slimy tentacles heaves itself out of the black water.");
            model.getParty().randomPartyMemberSay(model, List.of("Didn't I say the lake was a bad idea?"));
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "It's coming right for us. Get ready to fight!");
            List<Enemy> enemies = List.of(new TentacleEnemy('A'),
                    new TentacleEnemy('A'), new OctopusEnemy('B'),
                    new TentacleEnemy('A'), new TentacleEnemy('A'));
            runCombat(enemies, new CaveTheme(), false);
            model.getParty().randomPartyMemberSay(model, List.of("Phew... what a fight."));
        } else {
            println("There are ripples on the dark water, but the large cavern is silent and cold.");
            model.getParty().randomPartyMemberSay(model, List.of("..."));
        }
    }

}

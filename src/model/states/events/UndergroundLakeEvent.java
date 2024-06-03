package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import view.combat.CaveTheme;
import model.enemies.Enemy;
import model.enemies.OctopusEnemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class UndergroundLakeEvent extends DailyEventState {
    private boolean combatTriggeredOnce = false;

    public UndergroundLakeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party discovers an underground lake.");
        leaderSay("This might be a good place to rest, and to fill up our water skins.");
        randomSayIfPersonality(PersonalityTrait.anxious, List.of(model.getParty().getLeader()),
                "I don't know though... that lake gives me the creeps.");

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


    private void getRations(Model model, int amount) {
        model.getParty().addToFood(amount);
        println("The party gains " + amount + " rations.");
        startCombat(model, amount);
    }

    private void startCombat(Model model, int probability) {
        if (MyRandom.rollD10() < probability && !combatTriggeredOnce) {
            combatTriggeredOnce = true;
            model.getParty().randomPartyMemberSay(model, List.of("Wha... wha... what's that!?"));
            println("A large hulk of slimy tentacles heaves itself out of the black water.");
            model.getParty().randomPartyMemberSay(model, List.of("Didn't I say the lake was a bad idea?"));
            leaderSay("It's coming right for us. Get ready to fight!");
            List<Enemy> enemies = OctopusEnemy.makeEnemyList();
            runCombat(enemies, new CaveTheme(), false);
            if (model.getParty().isWipedOut()) {
                return;
            }
            model.getParty().randomPartyMemberSay(model, List.of("Phew... what a fight."));
        } else {
            println("There are ripples on the dark water, but the large cavern is silent and cold.");
            model.getParty().randomPartyMemberSay(model, List.of("..."));
        }
    }

}

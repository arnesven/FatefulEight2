package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.RiverTrollKniferEnemy;
import model.enemies.RiverTrollNetterEnemy;
import model.enemies.RiverTrollSpearerEnemy;
import view.combat.RiverCombatTheme;

import java.util.List;

public class RiverTrollsEvent extends RiverEvent {
    private boolean gotToCross = false;

    public RiverTrollsEvent(Model model) {
        super(model, false);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Look for river trolls",
                "There are usually river trolls busy fishing in the water near here");
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return !gotToCross;
    }

    @Override
    protected void doRiverEvent(Model model) {
        println("As you come down to the river you spot three large river trolls in the water. " +
                "They seem to be fishing and have not yet noticed you.");
        int selected = multipleOptionArrowMenu(model, 24, 24,
                List.of("Sneak past them", "Approach them calmly", "Attack them"));
        List<Enemy> enemies = List.of(new RiverTrollSpearerEnemy('A'), new RiverTrollNetterEnemy('B'),
                new RiverTrollKniferEnemy('C'));
        if (selected == 0) {
            boolean success = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 6);
            if (success) {
                println("You carefully wade through the water and leave the trolls undisturbed.");
            } else {
                println("The trolls suddenly notice you, they seem startled and gruff loudly amongst themselves. " +
                        "Then they attack you!");
                runCombat(enemies, new RiverCombatTheme(), true);
            }
        } else if (selected == 1) {
            if (model.getParty().size() > 1) {
                leaderSay("Okay people, nobody make any sudden moves. I know how to deal with trolls.");
                randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()), "Really?");
            }
            println("You slowly approach the trolls.");
            leaderSay("Hello there. Are the fish biting today?");
            printQuote("Troll", "SCRAM! THIS OUR RIVER!");
            leaderSay("Of course. We would just like to cross it. However we would not dare to " +
                    "forget to pay for crossing your river.");
            printQuote("Troll", "WHAT PAY?");
            leaderSay("Uhm. We give you gold.");
            printQuote("Troll", "DON'T WANT GOLD. ONLY FISH.");
            boolean fishGiven = false;
            if (!model.getParty().getInventory().getFish().isEmpty()) {
                print("Do you give the trolls some fish? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().getInventory().getFish().remove(0);
                    println("You give the troll some fish.");
                    fishGiven = true;
                }
            }

            if (!fishGiven) {
                leaderSay("How about 5 gold?");
                printQuote("Troll", "NO GOLD STUPID!");
                partyMemberSay(model.getParty().getRandomPartyMember(), "Yeah... this went well...");
                runCombat(enemies, new RiverCombatTheme(), true);
            } else {
                printQuote("Troll", "GOOD FISH. NOW GO!");
                leaderSay("We're as good as gone.");
                println("The trolls let you pass them and you wade over to the other side of the river.");
            }
        } else {
            println("You rush up to the trolls with your weapons drawn. You've caught them off guard, " +
                    "time to press your advantage!");
            runSurpriseCombat(enemies, new RiverCombatTheme(), true);
        }
        if (!model.getParty().isWipedOut() && !haveFledCombat()) {
            gotToCross = true;
        }
    }
}

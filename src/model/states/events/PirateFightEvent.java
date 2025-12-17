package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.special.AllyFromEnemyCharacter;
import model.combat.CombatAdvantage;
import model.enemies.Enemy;
import model.enemies.PirateEnemy;
import model.states.DailyEventState;
import model.states.dailyaction.PirateShop;
import util.MyRandom;
import view.combat.MansionTheme;

import java.util.ArrayList;
import java.util.List;

public class PirateFightEvent extends DailyEventState {
    public PirateFightEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Pirate Brawl", "While getting a few refreshments at the tavern, two burly men start arguing. Each of them " +
                "belongs to a crew of pirates, some of which are also in the tavern. You feel a fight may be coming.");
        int choice = multipleOptionArrowMenu(model, 24, 24,
                List.of("Leave tavern", "Aide one group of pirates", "Fight them all"));
        if (choice == 0) {
            leaderSay("This is none of our business. We'd better just get out before the chairs start flying.");
            return;
        }

        List<Enemy> enemies = PirateShipEvent.makePirateEnemies(model);
        if (choice == 1) {
            println("You throw a few insults to align yourself with one side of the upcoming brawl. " +
                    "However, almost instantly you realize that you've allied with the smaller group.");
            leaderSay("Oh, well, too late to back down now. Ready yourselves!");
            List<GameCharacter> allies = new ArrayList<>();
            for (int i = 0; i < MyRandom.randInt(1, Math.min(6, enemies.size())); ++i) {
                allies.add(new AllyFromEnemyCharacter(new PirateEnemy('A')));
            }
            runCombat(enemies, new MansionTheme(), true, CombatAdvantage.Neither, allies);
        } else if (choice == 2) {
            leaderSay("You're all a bunch of feckless ingrates!");
            println("A sudden silence follows your insult. Then, with not so much as a warning, " +
                    "the entire group of pirates attack you!");
            for (int i = 0; i < MyRandom.randInt(1, Math.min(6, enemies.size())); ++i) {
                enemies.add(new PirateEnemy('A'));
            }
            runCombat(enemies);
        }
    }
}

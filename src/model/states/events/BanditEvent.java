package model.states.events;

import model.Model;
import model.classes.Classes;
import model.enemies.BanditArcherEnemy;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.enemies.ViperEnemy;
import model.races.Race;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class BanditEvent extends DailyEventState {
    private Race race;

    public BanditEvent(Model model) {
        super(model);
        this.race = Race.ALL;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected void doEvent(Model model) {
        println("You encounter a few ruffians at the side of the road. They rudely block your path.");
        showRandomPortrait(model, Classes.BANDIT, race,"Bandit");
        portraitSay(model, "There's a toll here. 20 gold. It's uh... a traveller's fee. Bring out your purse now, be a good chap!");
        if (model.getParty().getGold() < 20) {
            portraitSay(model, "What, you don't have 20 gold? Well pay us what you have and you can pay us the rest next time you pass.");
        }
        print("It's obviously a shakedown, do you wish to pay the gold (Y/N)? ");
        if (yesNoInput()) {
            int amount = Math.min(20, model.getParty().getGold());
            model.getParty().addToGold(-amount);
            println("You pay off the bandits and continue on your journey.");
        } else {
            portraitSay(model, "You refuse? Hey, lads, we need to teach this lot some manners!");
            runCombat(generateBanditEnemies(model));
        }
    }

    public static List<Enemy> generateBanditEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(2, model.getParty().partyStrength() / (new BanditEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            if (MyRandom.randInt(3) == 0) {
                enemies.add(new BanditArcherEnemy('A'));
            } else {
                enemies.add(new BanditEnemy('A'));
            }
        }
        return enemies;
    }
}

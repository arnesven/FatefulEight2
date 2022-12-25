package model.states.events;

import model.Model;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.enemies.ViperEnemy;
import model.states.CombatEvent;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class BanditEvent extends DailyEventState {
    public BanditEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You encounter a few ruffians at the side of the road. They rudely block your path.");
        println("\"There's a toll here. 20 gold. It's uh... a traveller's fee. Bring out your purse now, be a good chap!\"");
        if (model.getParty().getGold() < 20) {
            println("\"What, you don't have 20 gold? Well pay us what you have and you can pay us the rest next time you pass.\"");
        }
        print("It's obviously a shakedown, do you wish to pay the gold (Y/N)? ");
        if (yesNoInput()) {
            int amount = Math.min(20, model.getParty().getGold());
            model.getParty().addToGold(-amount);
            println("You pay off the bandits and continue on your journey.");
        } else {
            println("\"You refuse? Hey, lads, we need to teach this lot some manners!\"");
            List<Enemy> enemies = new ArrayList<>();
            int numberOfEnemies = Math.max(2, model.getParty().partyStrength() / (new BanditEnemy('A')).getThreat());
            for (int i = numberOfEnemies; i > 0; --i) {
                enemies.add(new BanditEnemy('A'));
            }
            CombatEvent combat = new CombatEvent(model, enemies);
            combat.run(model);
        }
    }
}

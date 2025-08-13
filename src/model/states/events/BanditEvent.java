package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.enemies.BanditArcherEnemy;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.races.Race;
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

    @Override
    public String getDistantDescription() {
        return "a few people, looks like bandits";
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected void doEvent(Model model) {
        println("You encounter a few ruffians at the side of the road. They rudely block your path.");
        showRandomPortrait(model, Classes.BANDIT, race,"Bandit");
        portraitSay("There's a toll here. 20 gold. It's uh... a traveller's fee. Bring out your purse now, be a good chap!");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.aggressive, new ArrayList<>(),
                "That's a bunch of hogwash! How dare you try to scam us?");
        if (!didSay) {
            randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()),
                    "These guys look kind of mean. Maybe we should give them what they want.");
        }
        if (model.getParty().getGold() < 20) {
            portraitSay("What, you don't have 20 gold? Well pay us what you have and you can pay us the rest next time you pass.");
        }
        print("It's obviously a shakedown, do you wish to pay the gold (Y/N)? ");
        if (yesNoInput()) {
            int amount = Math.min(20, model.getParty().getGold());
            model.getParty().loseGold(amount);
            println("You pay off the bandits and continue on your journey.");
        } else {
            portraitSay("You refuse? Hey, lads, we need to teach this lot some manners!");
            randomSayIfPersonality(PersonalityTrait.aggressive, List.of(model.getParty().getLeader()),
                    "You think you can take us? Come on then!");
            runCombat(generateBanditEnemies(model));
            possiblyGetHorsesAfterCombat("bandits", 3);
        }
    }

    public static List<Enemy> generateBanditEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(2, model.getParty().partyStrength() / (new BanditEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            if (MyRandom.randInt(3) == 0) {
                enemies.add(new BanditArcherEnemy('B'));
            } else {
                enemies.add(new BanditEnemy('A'));
            }
        }
        return enemies;
    }
}

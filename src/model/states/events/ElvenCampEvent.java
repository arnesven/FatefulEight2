package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.ElfEnemy;
import model.enemies.Enemy;
import model.enemies.WolfEnemy;
import model.races.ElvenRace;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class ElvenCampEvent extends DailyEventState {
    private boolean freeRations;

    public ElvenCampEvent(Model model) {
        super(model);
        freeRations = false;
    }

    @Override
    protected void doEvent(Model model) {
        println("The party stumbles upon a clearing where a roaring " +
                "campfire is burning and delicious meat is roasting on " +
                "a spit. Around the fire, elven faces are peering at the " +
                "newcomers.");
        int elves = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getRace() instanceof ElvenRace) {
                elves++;
            }
        }

        CombatEvent combat = setupCombatWithElves(model);

        int roll = MyRandom.rollD10() + elves;
        if (roll >= 8) {
            println("The elves are celebrating the voyage of the moon and sun and will gladly share their campsite" +
                    " and their feast with the party.");
            freeRations = true;
        } else if (roll >= 6) {
            println("The elves are simply traveling between dwellings and and do not mind the company.");
        } else if (roll >= 3) {
            println("The elves are on a mission and suspect the party of being agents of an enemy.");
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 8);
            if (success) {
                println("The elves have been persuaded and seem to relax. For the remainder of the evening they" +
                        " pay no further attention to the party.");
            } else {
                println("The elves attack you!");
                combat.run(model);
            }
        } else {
            println("You have apparently committed some kind of transgression and angered the elves. They attack you!");
            combat.run(model);
        }
    }

    private CombatEvent setupCombatWithElves(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (new ElfEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(new ElfEnemy('A'));
        }
        CombatEvent combat = new CombatEvent(model, enemies);
        return combat;
    }

    @Override
    public boolean isFreeRations() {
        return freeRations;
    }
}

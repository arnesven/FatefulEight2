package model.states.events;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.BearEnemy;
import model.enemies.Enemy;
import model.enemies.WildBoarEnemy;
import model.items.Item;
import model.items.weapons.BowWeapon;
import model.states.DailyEventState;
import util.MyRandom;
import view.combat.GrassCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class HuntingEvent extends DailyEventState {
    public HuntingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        leaderSay("There may be some game in this area.");
        print("Do you wish to go hunting? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        doHunting(model);
    }

    public void doHunting(Model model) {
        boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 7);
        if (!result) {
            println("You spend the day trying to track down some game, but are unable to do so.");
            return;
        }
        int subEvent = MyRandom.randInt(3);
        if (subEvent == 0) {
            println("You find the footprints of a deer and you soon spot it.");
            if (hasABow(model.getParty())) {
                leaderSay("Now to bring it down...");
                boolean result2 = model.getParty().doSoloSkillCheck(model, this, Skill.Bows, 7);
                if (result2) {
                    println("The deer is more meet than you can carry.");
                    leaderSay("We'll leave something for the vultures.");
                    println("The party gains 10 rations.");
                    model.getParty().addToFood(10);
                }
            } else {
                println("Unfortunately, you have no bow to shoot the deer with. As you attempt to come close to it, it scampers of.");
            }
        } else {
            println("You travel into a nearby copse of trees. Pretty soon you find a pig's trail and follow it.");
            List<Enemy> enemies = new ArrayList<>();
            if (MyRandom.randInt(4) == 0) {
                leaderSay("That's not a boar... that's a... BEAR!");
                enemies.add(new BearEnemy('A'));
            } else {
                leaderSay("Shhh... there... a wild boar!");
                int noOfBoar = MyRandom.randInt(1, 3);
                for (int i = 0; i < noOfBoar; ++i) {
                    enemies.add(new WildBoarEnemy('A'));
                }
            }
            runCombat(enemies, new GrassCombatTheme(), true);
        }
    }

    private boolean hasABow(Party party) {
        for (GameCharacter gc : party.getPartyMembers()) {
            if (gc.getEquipment().getWeapon() instanceof BowWeapon) {
                return true;
            }
        }
        for (Item it : party.getInventory().getWeapons()) {
            if (it instanceof BowWeapon) {
                return true;
            }
        }
        return false;
    }
}

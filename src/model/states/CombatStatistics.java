package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.enemies.Enemy;
import util.MyLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombatStatistics {

    private final Map<GameCharacter, List<Enemy>> destroyedEnemies;
    private int fledEnemies = 0;
    private int maxDamage = 0;
    private String maxDamager = "";
    private int killedEnemies = 0;
    private String mvp = "";
    private int accuracy = 0;
    private int numberOfHits = 0;
    private int nonMisses = 0;

    public CombatStatistics() {
        destroyedEnemies = new HashMap<>();
    }

    public void calculateStatistics() {
        killedEnemies = sumUp();
        findMVP();
        accuracy = (int)(Math.round((double)nonMisses * 100.0 / (double) numberOfHits));
    }

    public int getKilledEnemies() {
        return killedEnemies;
    }

    public int getFledEnemies() {
        return fledEnemies;
    }

    public void increaseFledEnemies() {
        fledEnemies++;
    }

    public int getMaximumDamage() {
        return maxDamage;
    }

    public void damageDealt(int damage, GameCharacter damager) {
        numberOfHits++;
        if (damage > 0) {
            nonMisses++;
        }
        if (damage > maxDamage) {
            maxDamage = damage;
            maxDamager = damager.getFirstName();
        }
    }

    public String getMaxDamager() {
        return maxDamager;
    }

    private int sumUp() {
        return MyLists.intAccumulate(new ArrayList<>(destroyedEnemies.keySet()),
                (GameCharacter gc) -> destroyedEnemies.get(gc).size());
    }

    private void findMVP() {
        int mostKills = 0;
        for (GameCharacter gc : destroyedEnemies.keySet()) {
            if (getKillsFor(gc) > mostKills) {
                mvp = gc.getFullName();
                mostKills = getKillsFor(gc);
            }
        }
    }

    public void registerCharacterKill(GameCharacter killer, Enemy enemy) {
        if (!destroyedEnemies.containsKey(killer)) {
            destroyedEnemies.put(killer, new ArrayList<>());
        }
        destroyedEnemies.get(killer).add(enemy);
    }

    public int getKillsFor(GameCharacter killer) {
        if (destroyedEnemies.containsKey(killer)) {
            return destroyedEnemies.get(killer).size();
        }
        return 0;
    }

    public List<CombatLoot> generateCombatLoot(Model model) {
        System.out.println("Generating combat loot");
        List<CombatLoot> loot = new ArrayList<>();
        for (GameCharacter gc : destroyedEnemies.keySet()) {
            System.out.println(gc.getName() + " has killed " + getKillsFor(gc) + " enemies");
            for (Enemy e : destroyedEnemies.get(gc)) {
                CombatLoot l = e.getLoot(model);
                System.out.println("  The " + e.getName() + "'s loot is " + l.getText() +
                        ", " + l.getGold() + " gold, " + l.getRations() + " rations.");
                loot.add(l);
            }
        }
        return loot;
    }

    public String getMVP() {
        return mvp;
    }

    public int getAccuracy() {
        return accuracy;
    }
}

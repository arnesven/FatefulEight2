package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.Combatant;
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
    private int nonMisses = 0;
    private int effectiveness = 0;
    private int expectedRounds = 1;
    private int totalRounds;
    private int totalDamage = 0;
    private List<Integer> hits = new ArrayList<>();
    private double avgDamage = 0.0;
    private int totalEnemyDamage = 0;
    private int maxEnemyDamage = 0;
    private int avoidedDamage = 0;
    private int reducedDamage = 0;

    public CombatStatistics() {
        destroyedEnemies = new HashMap<>();
    }

    public void startCombat(List<Enemy> enemies, List<GameCharacter> participants, List<GameCharacter> allies) {
        double totalEnemyHP = MyLists.intAccumulate(enemies, Combatant::getMaxHP);
        double expectedDamageOutputPerRound = MyLists.doubleAccumulate(participants, GameCharacter::calcAverageDamage);
        expectedDamageOutputPerRound += MyLists.doubleAccumulate(allies, GameCharacter::calcAverageDamage);
        expectedRounds = (int)(Math.ceil(totalEnemyHP / expectedDamageOutputPerRound));
    }

    public void calculateStatistics(int actualRounds) {
        killedEnemies = sumUp();
        findMVP();
        accuracy = (int)(Math.round((double)nonMisses * 100.0 / (double) hits.size()));
        totalRounds = actualRounds;
        avgDamage = ((double)MyLists.intAccumulate(hits, integer -> integer)) / (double) hits.size();
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
        hits.add(damage);
        totalDamage += damage;
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

    public int getRoundPar() {
        return expectedRounds;
    }

    public String getRoundsTakenWithBird() {
        return totalRounds + " (" + getBird(totalRounds - expectedRounds) + ")";
    }

    private String getBird(int i) {
        if (i < -4) {
            return "Extraordinary";
        }
        switch (i) {
            case -4:
                return "Condor";
            case -3:
                return "Albatross";
            case -2:
                return "Eagle";
            case -1:
                return "Birdie";
            case 0:
                return "Par";
            case 1:
                return "Bogey";
            case 2:
                return "Double Bogey";
            case 3:
                return "Triple Bogey";
            default:
                return "Disaster";
        }
    }

    public int getTotalDamage() {
        return totalDamage;
    }

    public double getAverageDamage() {
        return avgDamage;
    }

    public void addEnemyDamage(int damage) {
        totalEnemyDamage += damage;
        if (damage > maxEnemyDamage) {
            maxEnemyDamage = damage;
        }
    }

    public int getMaxEnemyDamage() {
        return maxEnemyDamage;
    }

    public int getTotalEnemyDamage() {
        return totalEnemyDamage;
    }

    public int getAvoidedDamage() {
        return avoidedDamage;
    }

    public void addToAvoidedDamage(int reduction) {
        avoidedDamage++;
    }

    public void addToReduced(int reduction) {
        reducedDamage++;
    }

    public int getReducedDamage() {
        return reducedDamage;
    }
}

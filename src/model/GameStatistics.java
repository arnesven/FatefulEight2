package model;

public class GameStatistics {

    private static Model model = null;

    public static void setModel(Model model) {
        GameStatistics.model = model;
    }

    private static void increment(String key, int addition) {
        int total = 0;
        if (model.getSettings().getMiscCounters().containsKey(key)) {
            total = model.getSettings().getMiscCounters().get(key);
        }
        total += addition;
        model.getSettings().getMiscCounters().put(key, total);
    }

    private static int get(String key) {
        if (model.getSettings().getMiscCounters().containsKey(key)) {
            return model.getSettings().getMiscCounters().get(key);
        }
        return 0;
    }

    private static void recordMax(String key, int value) {
        int current = get(key);
        if (value > current) {
            model.getSettings().getMiscCounters().put(key, value);
        }
    }

    public static void incrementTotalDamage(int damage) {
        increment("totalCombatDamage", damage);
    }

    public static int getTotalDamage() {
        return get("totalCombatDamage");
    }

    public static void incrementKills(int i) {
        increment("totalEnemiesKilled", i);
    }

    public static int getEnemiesKilled() {
        return get("totalEnemiesKilled");
    }

    public static void incrementDistanceTraveled(int i) {
        increment("distanceTraveled", i);
    }

    public static int getDistanceTraveled() {
        return get("distanceTraveled");
    }

    public static void incrementGoldEarned(int cost) {
        increment("goldEarned", cost);
    }

    public static void incrementGoldLost(int cost) {
        increment("goldLost", cost);
    }

    public static int getGoldEarned() {
        return get("goldEarned");
    }

    public static int getGoldLost() {
        return get("goldLost");
    }

    public static void incrementRationsConsumed(int i) {
        increment("rationsConsumed", i);
    }

    public static int getRationsConsumed() {
        return get("rationsConsumed");
    }

    public static void incrementTotalXP(int xp) {
        increment("totalXpGained", xp);
    }

    public static int getTotalXpGained() {
        return get("totalXpGained");
    }

    public static void incrementSoloSkillChecks(int i) {
        increment("soloSkillChecks", i);
    }

    public static int getSoloSkillChecks() {
        return get("soloSkillChecks");
    }

    public static void incrementCollaborativeSkillChecks(int i) {
        increment("collaborativeSkillChecks", i);
    }

    public static int getCollaborativeSkillChecks() {
        return get("collaborativeSkillChecks");
    }

    public static void incrementCollectiveSkillChecks(int i) {
        increment("collectiveSkillChecks", i);
    }

    public static int getCollectiveSkillChecks() {
        return get("collectiveSkillChecks");
    }

    public static void incrementTotalSkillChecks(int i) {
        increment("totalSkillChecks", i);
    }

    public static int getTotalSkillChecks() {
        return get("totalSkillChecks");
    }

    public static void recordMaximumNotoriety(int notoriety) {
        recordMax("notoriety", notoriety);
    }

    public static int getMaximumNotoriety() {
        return get("notoriety");
    }

    public static void recordMaximumDamage(int damage) {
        recordMax("combatdamage", damage);
    }

    public static int getMaximumDamage() {
        return get("combatdamage");
    }

    public static void incrementCombatEvents() {
        increment("combatEvents", 1);
    }

    public static int getCombatEvents() {
        return get("combatEvents");
    }

    public static void incrementSurpriseCombats() {
        increment("surpriseCombats", 1);
    }

    public static int getSurpriseCombats(){
        return get("surpriseCombats");
    }

    public static void incrementAmbushCombats() {
        increment("ambushCombats", 1);
    }

    public static int getAmbushCombats() {
        return get("ambushCombats");
    }

    public static void incrementRerollsUsed() {
        increment("rerollsUsed", 1);
    }

    public static int getRerollsUsed() {
        return get("rerollsUsed");
    }

    public static void incrementItemsSold(int i) {
        increment("itemsSold", i);
    }

    public static int getItemsSold() {
        return get("itemsSold");
    }

    public static void incrementItemsBought(int i) {
        increment("itemsBought", i);
    }

    public static int getItemsBought() {
        return get("itemsBought");
    }

    public static void incrementSpellsAttempts() {
        increment("spellAttempts", 1);
    }

    public static int getSpellCastsAttempted() {
        return get("spellAttempts");
    }

    public static void incrementSpellSuccesses() {
        increment("spellSuccesses", 1);
    }

    public static int getSpellSuccesses() {
        return get("spellSuccesses");
    }
}

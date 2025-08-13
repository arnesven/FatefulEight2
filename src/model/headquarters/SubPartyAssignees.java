package model.headquarters;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SubPartyAssignees extends ArrayList<GameCharacter> {

    private static final int GOLD_FACTOR = 5;
    private static final List<String> NON_COMBAT_EVENTS = makeNonCombatEvents();
    private static final List<MyPair<String, String>> COMBAT_EVENTS = makeCombatEvents();

    private final List<GameCharacter> awayCharacters = new ArrayList<>();

    private int tripLength = 1;
    private int daysLeftOnTrip = -1;

    public boolean isAway(GameCharacter selected) {
        return awayCharacters.contains(selected);
    }

    public int getTripLength() {
        return tripLength;
    }

    public void setTripLength(int i) {
        this.tripLength = i;
    }

    public List<GameCharacter> getAwayCharacters() {
        return awayCharacters;
    }

    public void performAssignments(Model model, Headquarters hq, StringBuilder logEntry) {
        if (isEmpty()) {
            return;
        }

        if (daysLeftOnTrip > 0) {
            daysLeftOnTrip--;
            if (awayCharacters.size() > size()) {
                List<GameCharacter> waiting = new ArrayList<>(this);
                waiting.removeAll(awayCharacters);
                logEntry.append(MyLists.commaAndJoin(waiting, GameCharacter::getName));
                logEntry.append(waiting.size() > 1 ? "are" : "is").append(" waiting for the sub-party to return from their adventuring trip.\n");

            }
        } else if (daysLeftOnTrip == 0) { // returned from trip
            comeBackFromAdventuring(model, hq, logEntry);
        } else { // Waiting to leave
            checkToSeeIfLeave(model, hq, logEntry);
        }
    }

    private void comeBackFromAdventuring(Model model, Headquarters hq, StringBuilder logEntry) {
        logEntry.append("The sub-party, consisting of ");
        logEntry.append(MyLists.commaAndJoin(awayCharacters, GameCharacter::getName));
        String subEventsText = doSubPartyEvents(model, hq);

        if (MyLists.all(awayCharacters, GameCharacter::isDead)) {
            logEntry.append(" ").append(subEventsText);
            logEntry.append(".\n");
        } else {
            logEntry.append(" came back from adventuring. They ");
            logEntry.append(subEventsText);
            logEntry.append(". Their bounty was: ");

            int tripDays = Headquarters.getTripLengthInDays(tripLength);
            int gold = 0;
            List<Item> its = new ArrayList<>();
            for (GameCharacter gc : awayCharacters) {
                if (!gc.isDead()) {
                    gold += MyRandom.randInt(0, tripDays * GOLD_FACTOR);
                    int roll = MyRandom.rollD10();
                    if (roll < tripDays + 1) {
                        its.add(model.getItemDeck().draw(1).get(0));
                    }
                    gc.addToSP(-tripDays);
                    gc.addToXP(tripDays * 10);
                }
            }

            logEntry.append(gold).append(" gold");
            if (its.size() > 0) {
                logEntry.append(" and ").append(its.size()).append(" item");
                if (its.size() > 1) {
                    logEntry.append("s.\n");
                } else {
                    logEntry.append(".\n");
                }
                hq.getItems().addAll(its);
            } else {
                logEntry.append(".\n");
            }
            hq.addToGold(gold);
            GameStatistics.incrementGoldEarned(gold);
        }

        List<GameCharacter> killed = new ArrayList<>();
        for (GameCharacter gc : new ArrayList<>(awayCharacters)) {
            if (gc.isDead()) {
                remove(gc);
                hq.getCharacters().remove(gc);
                killed.add(gc);
            }
        }
        if (!killed.isEmpty()) {
            logEntry.append(MyLists.commaAndJoin(killed, GameCharacter::getName));
            logEntry.append(" were killed while going adventuring.\n");
        }
        awayCharacters.clear();
        daysLeftOnTrip = -1;
    }

    private String doSubPartyEvents(Model model, Headquarters hq) {
        List<String> strs = new ArrayList<>();
        System.out.println("Simulating sub-party events");
        for (int i = 0; i < tripLength; ++i) {
            System.out.println(" Event #" + (i+1));
            int roll = MyRandom.rollD10();
            System.out.println(" Roll is " + roll);
            if (roll < 3 + tripLength) {
                MyPair<String, String> combatEvent = randomCombatEvent();
                int combatRoll = MyRandom.rollD10();
                System.out.println(" Combat encounter! Combat roll " + combatRoll + " vs Sub Party Strength " + getSubPartyStrength());
                if (1 < combatRoll && combatRoll < getSubPartyStrength()) {
                    System.out.println(" Success! '" + combatEvent.first + "'");
                    strs.add(combatEvent.first);
                } else {
                    System.out.println(" Failure! '" + combatEvent.second + "'");
                    strs.add(combatEvent.second);
                    for (GameCharacter gc : awayCharacters) {
                        int damage = Math.max(0,
                                MyRandom.rollD6() + MyRandom.rollD6() -
                                gc.getAP() / 2 - gc.getSpeed() / 3 - 1);
                        gc.addToHP(-damage);
                        System.out.println(" Dealing " + damage + " to " + gc.getName());
                    }
                }
            } else {
                System.out.println(" Non-combat event");
                String event = randomNonCombatEvent();
                strs.add(event);
            }
        }
        return MyLists.commaAndJoin(strs, (String s) -> s);
    }

    private int getSubPartyStrength() {
        return MyLists.intAccumulate(this, GameCharacter::getLevel) / 4;
    }


    private void checkToSeeIfLeave(Model model, Headquarters hq, StringBuilder logEntry) {
        if (!MyLists.all(this, character -> character.getSP() == character.getMaxSP())) {
            logEntry.append("The sub-party did not leave, since one or more assigned characters were not at full stamina.\n");
            return;
        }
        if (hq.getFood() < hq.getSubPartyFoodRequired()) {
            logEntry.append("The sub-party did not leave, since there was not " +
                    "enough rations in headquarters to bring on the trip (");
            logEntry.append(hq.getFood()).append("/").append(hq.getSubPartyFoodRequired()).append(").\n");
            return;
        }
        logEntry.append("The sub-party, consisting of ").append(MyLists.commaAndJoin(this, GameCharacter::getName));
        logEntry.append(", left on a ").append(Headquarters.getTripLengthString(tripLength).toLowerCase()).append(" trip taking ");
        logEntry.append(hq.getSubPartyFoodRequired()).append(" food with them.\n");
        hq.addToFood(-hq.getSubPartyFoodRequired());
        awayCharacters.addAll(this);
        daysLeftOnTrip = Headquarters.getTripLengthInDays(tripLength) - 1;
    }

    public int getETA() {
        return daysLeftOnTrip;
    }

    private String randomNonCombatEvent() {
        return MyRandom.sample(NON_COMBAT_EVENTS);
    }

    private MyPair<String, String> randomCombatEvent() {
        return MyRandom.sample(COMBAT_EVENTS);
    }

    private static List<MyPair<String, String>> makeCombatEvents() {
        return List.of(
                new MyPair<>("fought off a pack of wolves", "were badly wounded in a wolf attack"),
                new MyPair<>("defeated a daemon at a mountain top altar", "were ambushed by a daemon ata mountain top altar"),
                new MyPair<>("beat up a gang of bandits", "were beaten up by a gang of bandits"),
                new MyPair<>("vanquished a swarm of bats", "were forced to flee from a swarm of bats"),
                new MyPair<>("defeated a Black Knight while crossing a river", "were badly beaten by a Black Knight when trying to cross a river"),
                new MyPair<>("crossed a perilous chasm with great skill", "did not fare so well when trying to cross a deep chasm"),
                new MyPair<>("slayed several crocodiles in a swamp", "got many serious wounds from an encounter with crocodiles in a swamp"),
                new MyPair<>("cleared a crypt from ghosts", "entered a crypt but were forced to flee from the ghosts inside"),
                new MyPair<>("were beset by the elements but due to great skill and preparation, weathered them without issue",
                        "were beset by the elements and were weakened from the effects of them"),
                new MyPair<>("encountered a dragon, and drove it off", "encountered a dragon, and were severely burned by it"),
                new MyPair<>("fought with some frogmen and were victorious", "fought with some frogmen and were defeated"),
                new MyPair<>("encountered a giant but got away safely", "encountered a giant and were injured"),
                new MyPair<>("were attacked by goblins but drove them off", "were thrashed by a group of goblins"),
                new MyPair<>("went into a mine and fought off some crazed dwarves", "went into a mine and got badly hurt in a fight with some crazy dwarves"),
                new MyPair<>("found a nomad camp but negotiated well so there was no quarrel", "found a nomad camp and caused an uproar"),
                new MyPair<>("fought off a pack of orcs", "were badly beaten by a pack of orcs"),
                new MyPair<>("entered an orcish stronghold and negotiated some good deals", "entered an orcish stronghold and got punished for it"),
                new MyPair<>("defeated a bunch of scorpions", "got injured in a fight against a bunch of scorpions"),
                new MyPair<>("slaughtered a bunch of spiders", "got poisoned by a bunch of spiders"),
                new MyPair<>("defeated a troll", "got injured badly when taking on a troll"),
                new MyPair<>("rooted out a nest of vipers", "got bitten repeatedly in a nest of vipers")
        );
    }

    private static List<String> makeNonCombatEvents() {
        return List.of("found an abandoned shack",
                        "found some lovely berries",
                        "found a broken wagon",
                        "found some strange cairns on a hillside",
                        "found a buried chest",
                        "helped a farmer chop wood",
                        "looted the body of a dead adventurer",
                        "met with eagles, and flew with them through a valley",
                        "encountered an elven camp",
                        "saw some faeries in the forest",
                        "helped a farmer plow in the fields",
                        "found a treasure map and followed it to where the treasure was buried",
                        "met with a fisherman",
                        "met a strange hermit",
                        "went into a Lotto House and had some good fortune",
                        "found a clearing with lots of lovely flowers",
                        "found a lumber mill and met a friendly lumberjack who lived there",
                        "went into a mine and find some useful equipment",
                        "saw a majestic monument",
                        "found some great tasting mushrooms",
                        "met a nobleman who gladly gave away some gold",
                        "found a pegasus and flew on it for a stretch",
                        "met with a priest on the road",
                        "found a holy shrine",
                        "found a sorcerer's tower and talked to the sorcerer within",
                        "explored a tall spire",
                        "explored a secret garden",
                        "saw a unicorn",
                        "spent a night in an old watchtower",
                        "met a witch in a hut in the forest"
        );
    }
}

package model.headquarters;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SubPartyAssignees extends ArrayList<GameCharacter> {

    private static final int GOLD_FACTOR = 5;
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
        logEntry.append(" came back from adventuring. Their bounty was: ");

        int tripDays = Headquarters.getTripLengthInDays(tripLength);
        int gold = 0;
        List<Item> its = new ArrayList<>();
        for (GameCharacter gc : awayCharacters) {
            gold += MyRandom.randInt(0, tripDays * GOLD_FACTOR);
            int roll = MyRandom.rollD10();
            if (roll < tripDays+1) {
                its.add(model.getItemDeck().draw(1).get(0));
            }
            gc.addToSP(-tripDays); // TODO: Add damage and potentially kill characters
            gc.addToXP(tripDays * 10);
        }

        logEntry.append(gold).append(" gold");
        if (its.size() > 0) {
            logEntry.append(" and ").append(its.size()).append(" items.\n");
            hq.getItems().addAll(its);
        } else {
            logEntry.append(".\n");
        }
        hq.addToGold(gold);
        awayCharacters.clear();
        daysLeftOnTrip = -1;
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
}

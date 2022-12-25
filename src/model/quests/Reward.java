package model.quests;

import model.Party;

public class Reward {
    private final int rep;
    private final int gold;

    public Reward(int partyRep, int gold) {
        this.rep = partyRep;
        this.gold = gold;
    }

    public int getReputation() {
        return rep;
    }

    public int getGold() {
        return gold;
    }

    public void giveYourself(Party party, int numberOfPartyMembers) {
        party.addToGold(gold * numberOfPartyMembers);
        party.addToReputation(rep);
    }
}

package model.quests;

import model.Party;
import model.characters.GameCharacter;

import java.io.Serializable;

public class Reward implements Serializable {
    private final int rep;
    private final int gold;
    private final int exp;

    public Reward(int partyRep, int gold, int exp) {
        this.rep = partyRep;
        this.gold = gold;
        this.exp = exp;
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
        party.getPartyMembers().forEach((GameCharacter gc) -> gc.addToXP(exp));
    }

    public String getDescription() {
        String experience = "";
        if (exp > 0) {
            experience = ", each party member will gain " + exp + " experience";
        }
        String reputation = ".";
        if (rep > 0) {
            reputation = ", and your reputation will increase.";
        } else if (rep < 0) {
            reputation = ", but your reputation will DECREASE.";
        }
        return "you will be paid " + gold + " gold per party member" + experience + reputation;
    }
}
